package multiplicity.csysngjme.items;

import java.util.UUID;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.scene.RoundedRectangleFrame;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

public class JMERoundedRectangleBorder extends JMERectangularItem implements IBorder {
	private static final long serialVersionUID = 4878959946724055012L;

	protected RoundedRectangleFrame frame;

	protected float borderSize;
	protected int cornerDivisions;
	protected IItem parentItem;
	protected ColorRGBA transparentWhite = new ColorRGBA(1f, 1f, 1f, 0.6f);


	public JMERoundedRectangleBorder(String name, UUID uuid) {
		this(name, uuid, 100, 100, 10, 8);
	}

	public JMERoundedRectangleBorder(String name, UUID uuid, float borderSize, int cornerDivisions) {
		this(name, uuid, 1, 1, borderSize, cornerDivisions);
	}

	public JMERoundedRectangleBorder(String name, UUID uuid, float width, float height, float borderSize, int cornerDivisions) {
		super(name, uuid);
		
		this.borderSize = borderSize;
		this.cornerDivisions = cornerDivisions;
		setSize(width, height);
	}

	@Override
	public void initializeGeometry() {
		setGeometry(borderSize, cornerDivisions);
	}

	public void setGeometry(float borderSize, int cornerDivisions) {		
		this.borderSize = borderSize;
		this.cornerDivisions = cornerDivisions;
		rebuild();		
	}

	private void rebuild() {
		//		removeCurrentFrame();
		if(frame == null) {
			BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();		
			bs.setSourceFunction(SourceFunction.SourceAlpha);
			bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
			bs.setBlendEnabled(true);	


			frame = new RoundedRectangleFrame(getName() + "_rrectframe", getWidth(), getHeight(), borderSize, cornerDivisions);
			ItemMap.register(frame, this);
			frame.setSolidColor(transparentWhite);
			frame.setRenderState(bs);
			frame.setModelBound(new OrthogonalBoundingBox());
			frame.updateModelBound();
			frame.updateRenderState();
			attachChild(frame);	
		}
		frame.updateWithSizeAndBorder(getWidth(), getHeight(), this.borderSize);
		frame.updateModelBound();
	}

	@Override
	public void setBorderWidth(float borderSize) {
		this.borderSize = borderSize;
		rebuild();
	}

	@Override
	public float getBorderWidth() {
		return this.borderSize;
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

	@Override
	public Spatial getTreeRootSpatial() {
		return this;
	}

	@Override
	public void setParentItem(IItem parent) {
		this.parentItem = parent;
	}

	@Override
	public IItem getParentItem() {
		return parentItem;
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return frame;
	}

	@Override
	public void setColor(ColorRGBA color) {
		((RoundedRectangleFrame) getManipulableSpatial()).setSolidColor(color);
	}
}
