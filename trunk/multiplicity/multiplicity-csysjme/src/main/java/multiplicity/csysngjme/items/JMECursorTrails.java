package multiplicity.csysngjme.items;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import multiplicity.csysng.gfx.ColourUtils;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.zordering.GlobalZOrder;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.data.CursorPositionRecord;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.Line.Mode;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

public class JMECursorTrails extends JMEItem implements ICursorTrailsOverlay, IMultiTouchEventListener {
	private static final long serialVersionUID = 7359258481754712453L;
	
	private List<Line> freeTrailLines = new ArrayList<Line>();
	private Map<Long, Line> trailLinesInUse = new HashMap<Long, Line>();
	private ColorRGBA trailColour = new ColorRGBA(1f, 1f, 1f, 0.8f);
	private int initialNumberOfTrails;

	private boolean solid = false;
	

	public JMECursorTrails(String name, UUID uuid, int initialNumberOfTrails) {
		super(name, uuid);
		this.initialNumberOfTrails = initialNumberOfTrails;
	}
	

	@Override
	public void initializeGeometry() {
		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		setLocalTranslation(-r.getWidth()/2, -r.getHeight()/2, 0);
		for(int i = 0; i < initialNumberOfTrails; i++) {
			createTrailLine(i);
		}		
	}
	
	private Line createTrailLine(int i) {
		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = new Vector3f(20f, 20f, 0f);
		vertices[1] = new Vector3f(40f, 60f, 0f);
		Line line = new Line("trail" + i, vertices, null, null, null);
		line.setZOrder(GlobalZOrder.overlayZOrder);
		line.setMode(Mode.Connected);
		line.setLineWidth(3f);
		line.setSolidColor(ColorRGBA.black);		
		hideLine(line);
		attachChild(line);
		return line;
	}
	
	private Line getTailLineForCursor(long id) {
		Line l = trailLinesInUse.get(id);
		if(l == null) {
			if(freeTrailLines.size() > 0) {
				l = freeTrailLines.remove(0);
			}else{
				l = createTrailLine(freeTrailLines.size());
			}
			trailLinesInUse.put(id, l);
		}
		return l;
	}
	
	private void hideLine(Line line) {
		line.setCullHint(CullHint.Always);
	}
	
	private void hideLine(MultiTouchCursorEvent event) {
		Line l = getTailLineForCursor(event.getCursorID());
		hideLine(l);
		trailLinesInUse.remove(event.getCursorID());
		freeTrailLines.add(l);	
	}
	
	private void showNewTrailLine(MultiTouchCursorEvent event) {
		Line line = getTailLineForCursor(event.getCursorID());
		line.clearBuffers();
		line.setCullHint(CullHint.Never);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		List<CursorPositionRecord> history = event.getPositionHistory();
		Line line = getTailLineForCursor(event.getCursorID());
		FloatBuffer v = BufferUtils.createFloatBuffer(history.size() * 3);
		FloatBuffer c = BufferUtils.createFloatBuffer(history.size() * 4);
		v.rewind();
		int i = 0;
		for(CursorPositionRecord x : history) {			
			v.put(x.getPosition().x).put(x.getPosition().y).put(0);
			if(solid) {
				c.put(trailColour.r).put(trailColour.g).put(trailColour.b).put(trailColour.a);
			}else{
				c.put(trailColour.r).put(trailColour.g).put(trailColour.b).put(i/(float)history.size());
			}
			i++;
		}

		line.reconstruct(v, null, c, null);
	}
	
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {		
		showNewTrailLine(event);
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {	
		hideLine(event);		
	}


	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
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


	@Override
	public void setFadingColour(Color c) {
		this.trailColour = ColourUtils.colourConvert(c);
		solid = false;		
	}


	@Override
	public void setSolidColour(Color c) {
		this.trailColour = ColourUtils.colourConvert(c);
		solid  = true;
	}
}
