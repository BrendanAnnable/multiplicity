package multiplicity.csysngjme.items;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity.csysng.gfx.ColourUtils;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILinkingLine;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;
import multiplicity.input.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

public class JMELinkingLine extends JMEItem implements ILinkingLine, IItemListener {
	
	private static final long serialVersionUID = -1171728273129051547L;
	Line line;
	private IItem destination;
	private IItem source;
	private Vector3f[] twoPoints;
	
	public JMELinkingLine(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);		
	}

	@Override
	public void initializeGeometry() {
		twoPoints = new Vector3f[2];
		twoPoints[0] = new Vector3f();
		twoPoints[1] = new Vector3f();
		
		line = new Line(name, twoPoints, null, null, null);
		
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();		
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		bs.setBlendEnabled(true);	
		
		line.setRenderState(bs);
		line.updateRenderState();
		
		attachChild(line);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return line;
	}

	@Override
	public void setSourceItem(IItem item) {
		if(item == null) return;
		if(source != null) {
			// replacing source item, so unlink old one
			source.removeItemListener(this);
			this.source = null;
		}		
		this.source = item;
		source.addItemListener(this);
		updatePosition(item, twoPoints[0]);
		updateLine();
	}

	@Override
	public void setDestinationItem(IItem item) {
		if(item == null) return;
		if(destination != null) {
			// replacing destination item, so unlink old one
			item.removeItemListener(this);
			this.destination = null;
		}
		this.destination = item;	
		item.addItemListener(this);		
		updatePosition(item, twoPoints[1]);
		updateLine();
	}

	@Override
	public void setLineColour(Color c) {
		line.setDefaultColor(ColourUtils.colourConvert(c));
		//line.setSolidColor();		
	}

	@Override
	public void setLineWidth(float width) {
		line.setLineWidth(width);		
	}

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
		if(item == source) {
			updatePosition(item, twoPoints[0]);
			updateLine();
		}else if(item == destination) {
			updatePosition(item, twoPoints[1]);
			updateLine();
		}
		
	}
	
	private void updatePosition(IItem item, Vector3f into) {		
		Vector2f worldLoc = item.getWorldLocation();
		into.x = worldLoc.x;
		into.y = worldLoc.y;
	}
	
	private void updateLine() {
		FloatBuffer vb = BufferUtils.createFloatBuffer(twoPoints);
		line.reconstruct(vb, null, null, null);
		line.updateGeometricState(0f, true);
	}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemMoved(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemRotated(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemScaled(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemZOrderChanged(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IItem getDestinationItem() {
		return destination;
	}

	@Override
	public IItem getSourceItem() {
		return source;
	}

	@Override
	public float getLength() {
		return twoPoints[0].distance(twoPoints[1]);
	}

	@Override
	public void updateEndPoints() {
		updatePosition(source, twoPoints[0]);
		updatePosition(destination, twoPoints[1]);
		updateLine();		
	}

}
