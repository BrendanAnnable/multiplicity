package multiplicity.csysngjme.items;

import java.awt.Color;
import java.util.UUID;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

import multiplicity.csysng.ItemMap;
import multiplicity.csysng.gfx.ColourUtils;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysngjme.utils.JMEUtils;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

public class JMEColourRectangle extends JMERectangularItem implements IColourRectangle {
	private static final long serialVersionUID = 3930879484748356787L;

	private Quad quad;
	private BlendState alphaBlending;	
	
	public JMEColourRectangle(String name, UUID uuid, int width, int height) {
		super(name, uuid);
		setSize(width, height);
	}
	
	@Override
	public void initializeGeometry() {
		quad = new Quad("colourrectangle");
		quad.setSolidColor(ColorRGBA.black);
		ItemMap.register(quad, this);
		quad.setModelBound(new OrthogonalBoundingBox());
		quad.updateModelBound();
		quad.resize(getWidth(), getHeight());
		alphaBlending = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();		
		alphaBlending.setSourceFunction(SourceFunction.SourceAlpha);
		alphaBlending.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		alphaBlending.setBlendEnabled(true);
		quad.setRenderState(alphaBlending);
		attachChild(quad);
		updateModelBound();
	}

	
	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quad;
	}

	@Override
	public void setGradientBackground(Gradient g) {
		JMEUtils.applyGradientToQuad(quad, g);
	}

	@Override
	public void setSolidBackgroundColour(Color c) {
		quad.setSolidColor(ColourUtils.colourConvert(c));		
	}
}
