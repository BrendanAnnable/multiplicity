package multiplicity.csysngjme.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

import multiplicity.app.utils.UnitConversion;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysngjme.zordering.GlobalZOrder;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class JMEDiskCursorOverlay extends JMEItem implements ICursorOverlay, IMultiTouchEventListener {
	private static final long serialVersionUID = -1761459355243287775L;
	
	private BlendState diskBlend;	
	private List<Disk> freeDisks = new ArrayList<Disk>();
	private Map<Long, Disk> disksInUse = new HashMap<Long,Disk>();

	private int initialNumberOfCursors;	
	
	public JMEDiskCursorOverlay(String name, UUID uuid, int initialNumberOfCursors) {
		super(name, uuid);		
		this.initialNumberOfCursors = initialNumberOfCursors;
	}
	
	@Override
	public void initializeGeometry() {
		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		setLocalTranslation(-r.getWidth()/2, -r.getHeight()/2, 0);
		createBlendState();
		for(int i = 0; i < initialNumberOfCursors; i++) {
			createDisk(i);
		}		
	}

	private void createBlendState() {
		diskBlend = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		diskBlend.setSourceFunction(SourceFunction.SourceAlpha);
		diskBlend.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		diskBlend.setBlendEnabled(true);	
	}

	private ColorRGBA diskColour = new ColorRGBA(1f, 1f, 1f, 0.5f);
	protected float diskRadius = 10f;
	
	private Disk createDisk(int index) {
		Disk d = new Disk("cursor" + index, 2, 16, diskRadius);
		d.setSolidColor(diskColour);
		d.setRenderState(diskBlend);
		d.setZOrder(GlobalZOrder.overlayZOrder);
		d.updateRenderState();
		hideDisk(d);
		attachChild(d);
		return d;
	}


	private Disk getDiskForCursor(long id) {
		Disk d = disksInUse.get(id);
		if(d == null) {
			if(freeDisks.size() > 0) {
				d = freeDisks.remove(0);
			}else{
				d = createDisk(freeDisks.size());
			}
			disksInUse.put(id, d);
		}
		return d;
	}
	
	private void hideDisk(MultiTouchCursorEvent event) {
		Disk d = getDiskForCursor(event.getCursorID());
		hideDisk(d);
		disksInUse.remove(event.getCursorID());
		freeDisks.add(d);
	}
		
	private void hideDisk(Disk d) {
		d.setCullHint(CullHint.Always);
	}

	private void showDisk(MultiTouchCursorEvent event) {
		Disk d = getDiskForCursor(event.getCursorID());
		UnitConversion.tableToScreen(event.getPosition(), d.getLocalTranslation());
		d.setCullHint(CullHint.Never);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		showDisk(event);
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		showDisk(event);		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		showDisk(event);
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		hideDisk(event);
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);		
	}



	@Override
	public Spatial getManipulableSpatial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void respondToItem(IItem item) {
		item.getMultiTouchDispatcher().addListener(this);		
	}

	@Override
	public void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer) {
		multiTouchEventProducer.registerMultiTouchEventListener(this);		
	}

}
