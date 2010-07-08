package multiplicity.csysngjme.items;

import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.items.hotspots.HotSpotItem;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

public class JMELine extends JMELineItem {

	private static final long serialVersionUID = -8078610021819270289L;
	private Line l;
	private BlendState diskBlend;	
	private Vector3f[] vertices;
	private ColorRGBA lineColour = new ColorRGBA(0f, 0f, 0f, 1f);
	private float lineWidth = 4f;
	private HotSpotItem hotSpotItem = null;
	private boolean visible = true;

	public JMELine(String name, UUID uuid, Vector3f[] vertices, ColorRGBA lineColour, float lineWidth, HotSpotItem hotSpotItem) {
		super(name, uuid);
		this.vertices = vertices;
		this.lineColour = lineColour;
		this.lineWidth = lineWidth;
		this.hotSpotItem = hotSpotItem;
	}
	
	@Override
	public void initializeGeometry() {
		l = new Line("Link", vertices, null, null, null);
		l.setSolidColor(lineColour);
		l.setLineWidth(lineWidth);
		l.setAntialiased(true);
		ItemMap.register(l, this);
		l.setModelBound(new OrthogonalBoundingBox());
		l.updateModelBound();
		diskBlend = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		diskBlend.setSourceFunction(SourceFunction.SourceAlpha);
		diskBlend.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		diskBlend.setBlendEnabled(true);	
		l.setRenderState(diskBlend);
		attachChild(l);
		updateModelBound();
	}
	

	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return l;
	}


	public void changeBackgroundColor(ColorRGBA colorRGBA) {
		l.setSolidColor(colorRGBA);		
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if(visible) {
			l.setSolidColor(lineColour);	
		}
		else {
			l.setSolidColor(new ColorRGBA(0f, 0f, 0f, 0f));	
		}
	}
	
	@Override
	public void redrawLine(Vector3f[] vertices) {
		if(visible) {
			FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
			l.reconstruct(fBuffer, null, null, null);
			l.setSolidColor(lineColour);  			
		}
	}


	@Override
	public void redrawTargetLocation(Vector2f relativeLocation) {
		FloatBuffer fBuffer = BufferUtils.createFloatBuffer(relativeLocation);                    
		l.reconstruct(fBuffer, null, null, null);
		l.setSolidColor(lineColour);  
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	@Override
	public HotSpotItem getHotSpotItem() {
		return hotSpotItem;
	}
}
