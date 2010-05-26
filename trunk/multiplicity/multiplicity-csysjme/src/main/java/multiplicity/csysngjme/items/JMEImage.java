package multiplicity.csysngjme.items;

import java.net.URL;
import java.util.UUID;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.math.Vector2f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import multiplicity.csysng.items.IImage;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

public class JMEImage extends JMERectangularItem implements IImage {
	private static final long serialVersionUID = 2919988942437522767L;

	private Quad imageQuad;
	private TextureState textureState;

	private BlendState alphaBlending;
	private URL resourceURL;
	
	public JMEImage(String name, UUID uuid) {
		super(name, uuid);	
	}
	
	@Override
	public void initializeGeometry() {
		createImageQuad(1,1);
		attachChild(imageQuad);
	}
	
	private void createImageQuad(int width, int height) {		
		imageQuad = new Quad(name + "_quad", width, height);
		ItemMap.register(imageQuad, this);	
		textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		textureState.setCorrectionType(TextureState.CorrectionType.Perspective);
		imageQuad.setRenderState(textureState);
		imageQuad.updateRenderState();
		imageQuad.setModelBound(new OrthogonalBoundingBox());
		imageQuad.updateModelBound();
		
		alphaBlending = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alphaBlending.setBlendEnabled(true);
		alphaBlending.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaBlending.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaBlending.setTestEnabled(true);
		alphaBlending.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaBlending.setEnabled(false);
		imageQuad.setRenderState(alphaBlending);
	}

	public void setImage(URL resource) {
		setImage(resource, 1f);
	}
	
	@Override
	public void setImage(URL resource, float scale) {	
	    this.resourceURL = resource;
		Texture t = new Texture2D();
		t.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps); // for some reason, Trilinear causes strange problems
		t.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		t.setImage(TextureManager.loadImage(resource, true));
		int imgWidth = (int)(t.getImage().getWidth() * scale);
		int imgHeight = (int)(t.getImage().getHeight() * scale);
		setSize(imgWidth, imgHeight);
		textureState.setTexture(t);
		imageQuad.updateRenderState();
	}
	
	@Override
	public void setSize(Vector2f size) {
		super.setSize(size);
		imageQuad.updateGeometry(size.x, size.y);
		imageQuad.updateModelBound();
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		imageQuad.updateGeometry(width, height);
		imageQuad.updateModelBound();
	}

	@Override
	public void setAlphaBlending(boolean blendingOn) {
		alphaBlending.setEnabled(blendingOn);
		imageQuad.updateRenderState();
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
		return imageQuad;
	}

    @Override
    public URL getImageUrl() {
        return this.resourceURL;
    }
}
