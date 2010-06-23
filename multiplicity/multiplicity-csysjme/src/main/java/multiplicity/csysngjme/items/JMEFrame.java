package multiplicity.csysngjme.items;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.picking.JMEItemUserData;
import multiplicity.csysngjme.utils.JMEUtils;
import multiplicity.csysngjme.zordering.FrameZOrderManager;
import multiplicity.input.events.MultiTouchCursorEvent;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.StencilState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.StencilState.StencilFunction;
import com.jme.scene.state.StencilState.StencilOperation;
import com.jme.system.DisplaySystem;

public class JMEFrame extends JMERectangularItem implements IFrame {
	private static final long serialVersionUID = -4475581295160543158L;

	protected static boolean[] usedStencilValues = new boolean[255]; // booleans are false by default

	// TODO probably want to support more than 255 windows/stencilled items, eventually.
	protected static int getNextFreeStencilValue() {
		// purposefully don't use 0...
		for(int i = 1; i < 255; i++) {
			if(!usedStencilValues[i]) {
				usedStencilValues[i] = true;
				return i;
			}
		}
		return -1;
	}

	protected static void freeStencilValue(int i) {
		usedStencilValues[i] = false;
	}

	protected IBorder border;

	protected BlendState maskBlendState;
	protected StencilState maskStencil;
	protected Node drawableContent;
	protected Quad maskGeometry;

	protected StencilState drawableArea;
	protected int stencilValue;
	protected ColorRGBA backgroundColor = new ColorRGBA(0, 0, 0, 1);

	private float originalBorderWidth;

	@Override
	public void finalize() {
		freeStencilValue(stencilValue);
	}

	public JMEFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid);
		setSize(width, height);
	}


	@Override
	public void initializeGeometry() {				
		drawableContent = new Node("drawable content");
		maskGeometry = new Quad("maskGeometry", getWidth(), getHeight());
		maskGeometry.setModelBound(new OrthogonalBoundingBox()); // needed to make sure that items underneath don't get picked
		maskGeometry.updateModelBound();
		maskGeometry.setSolidColor(backgroundColor);
		setupStencil();
		maskGeometry.setRenderState(maskStencil);	 
		maskGeometry.setRenderState(maskBlendState); 
		maskGeometry.updateRenderState();				
		attachChild(maskGeometry);
		drawableContent.setRenderState(drawableArea);
		drawableContent.updateRenderState();
		attachChild(drawableContent);	
		updateRenderState();
		getFrameZOrderManager().updateZOrdering();
		rebuild();
		updateRenderState();
	}

	private FrameZOrderManager getFrameZOrderManager() {
		if(zOrderManager == null) createZOrderManager();
		return (FrameZOrderManager)zOrderManager;
	}

	private void setupStencil() {
		synchronized(JMEFrame.class) {
			stencilValue = getNextFreeStencilValue();
		}
		maskStencil = DisplaySystem.getDisplaySystem().getRenderer().createStencilState();
		maskStencil.setEnabled(true);
		maskStencil.setUseTwoSided(false);
		maskStencil.setStencilFunction(StencilFunction.Always);
		maskStencil.setStencilReference(stencilValue);
		maskStencil.setStencilMask(0xF);
		maskStencil.setStencilOpFail(StencilOperation.Replace); // ignore this
		maskStencil.setStencilOpZFail(StencilOperation.Replace); // ignore this
		maskStencil.setStencilOpZPass(StencilOperation.Replace);

		maskBlendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();		
		maskBlendState.setSourceFunction(SourceFunction.SourceAlpha);
		maskBlendState.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		maskBlendState.setBlendEnabled(true);

		drawableArea = DisplaySystem.getDisplaySystem().getRenderer().createStencilState();
		drawableArea.setEnabled(true);
		drawableArea.setUseTwoSided(false);
		drawableArea.setStencilFunction(StencilFunction.EqualTo);
		drawableArea.setStencilReference(stencilValue);
		drawableArea.setStencilMask(0xF);
		drawableArea.setStencilOpFail(StencilOperation.Keep);
		drawableArea.setStencilOpZFail(StencilOperation.Keep);
		drawableArea.setStencilOpZPass(StencilOperation.Keep);		
	}

	private void setMaskGeometry(Vector2f size) {
		if(maskGeometry == null) return;
		maskGeometry.updateGeometry(size.x, size.y);
		maskGeometry.updateGeometricState(0f, true);
		maskGeometry.updateRenderState();
	}

	
	@Override
	public void removeItem(IItem item) {
//	    List<IItem> cs = getItemChildren();
//	    for (IItem i : cs) {
//            if( i.getUUID().equals(item.getUUID())) {
//                drawableContent.detachChild(item.getTreeRootSpatial());
//                getItemChildren().remove(item);
//            }
//        }
	    
	    zOrderManager.unregisterForZOrdering(item);	
	    zOrderManager.updateZOrdering();
	    updateRenderState();
	}
	
	@Override
	public void add(IItem item) {
		getItemChildren().add(item);
		JMEItemUserData itemData = (JMEItemUserData) item.getManipulableSpatial().getUserData(JMEItem.KEY_JMEITEMDATA);
		itemData.setMaskGeometry(maskGeometry);
		item.getTreeRootSpatial().setRenderState(drawableArea);
		item.getTreeRootSpatial().updateRenderState();
		drawableContent.attachChild(item.getTreeRootSpatial());		
		item.setParentItem(this);
		zOrderManager.registerForZOrdering(item);
		final IItem instance = this;
		item.addItemListener(new ItemListenerAdapter() {
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) {
					l.itemCursorPressed(instance, event);
				}
			}
		});
		zOrderManager.updateZOrdering();
		updateRenderState();		
	}

	public IBorder getBorder() {
		return border;
	}

	@Override
	public void setBorder(IBorder b) {		
		if(this.border != null) {
			this.detachChild(this.border.getTreeRootSpatial());
		}
		this.border = b;
		this.originalBorderWidth = border.getBorderWidth();
		this.border.setSize(this.getSize());

		// need to send events for the border to our listeners
		final IItem instance = this;
		this.border.addItemListener(new ItemListenerAdapter() {
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) {
					l.itemCursorPressed(instance, event);
				}
			}
		});
		setMaskGeometry(this.border.getSize());
		border.getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
		ItemMap.register(border.getManipulableSpatial(), this);
		rebuild();		
		attachChild(this.border.getTreeRootSpatial());
		getZOrderManager().updateZOrdering();
	}

	@Override
	public IItemListener maintainBorderSizeDuringScale() {
		IItemListener l = new ItemListenerAdapter() {
			@Override
			public void itemScaled(IItem item) {
				if(getBorder() != null) {
					getBorder().setBorderWidth(originalBorderWidth / getRelativeScale());
				}
			}
		};
		addItemListener(l);
		return l;
	}

	@Override
	public void behaviourAdded(IBehaviour behaviour) {
		border.getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
	}

	@Override
	public void setSize(Vector2f size) {
		super.setSize(size);
		rebuild();
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		rebuild();		
	}

	private void rebuild() {
		setMaskGeometry(this.getSize());
		if(getBorder() != null) {
			getBorder().setSize(this.getSize());
		}
	}

	@Override
	protected void createZOrderManager() {
		zOrderManager = new FrameZOrderManager(this, 50);
	}

	public Spatial getMaskGeometry() {
		return maskGeometry;
	}

	@Override
	public boolean hasBorder() {
		return getBorder() != null;
	}

	@Override
	public Spatial getManipulableSpatial() {
		return getBorder().getManipulableSpatial();
	}

	@Override
	public void setSolidBackgroundColour(Color c) {
		this.backgroundColor  = new ColorRGBA(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
		maskGeometry.setSolidColor(backgroundColor);
	}

	@Override
	public void setGradientBackground(Gradient g) {
		JMEUtils.applyGradientToQuad(maskGeometry, g);		
	}

}
