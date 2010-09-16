package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Disk;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

public class JMEColourCircle extends JMECircularItem {

	protected static final long serialVersionUID = -2734936339048374247L;
	protected Disk d;
	protected BlendState diskBlend;	
	protected float diskRadius = 10f;
	protected ColorRGBA diskColour = new ColorRGBA(0f, 0f, 0f, 1f);

	public JMEColourCircle(String name, UUID uuid, float diskRadius) {
		super(name, uuid);
		this.diskRadius = diskRadius;
	}
	

	public JMEColourCircle(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid);
		this.diskRadius = radius;
		this.diskColour = colorRGBA;
	}


	@Override
	public void initializeGeometry() {
		d = new Disk("disk", 2, 64, diskRadius);
		d.setSolidColor(diskColour);
		ItemMap.register(d, this);
		d.setModelBound(new OrthogonalBoundingBox());
		d.updateModelBound();
		diskBlend = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		diskBlend.setSourceFunction(SourceFunction.SourceAlpha);
		diskBlend.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		diskBlend.setBlendEnabled(true);	
		d.setRenderState(diskBlend);
		attachChild(d);
		updateModelBound();
	}
	

	@Override
	protected IZOrderManager createZOrderManager() {
		return new SimpleZOrderManager(this);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return d;
	}


	public void changeBackgroundColor(ColorRGBA colorRGBA) {
		d.setSolidColor(colorRGBA);		
	}

}
