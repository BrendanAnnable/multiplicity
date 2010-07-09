package multiplicity.csysngjme.items;

import java.awt.geom.Rectangle2D;
import java.util.UUID;

import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.IKeyboardRenderer;
import multiplicity.csysng.items.keyboard.KeyboardDefinition;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;

public class JMEKeyboard extends JMERectangularItem implements IKeyboard {
	private static final long serialVersionUID = -7594752755843316351L;
	private Quad imageQuad;
	private TextureState textureState;
	private Texture2D texture;
	private ImageGraphics graphics;
	private KeyboardDefinition keyboardDefinition;
	private IKeyboardRenderer keyboardRenderer;

	public JMEKeyboard(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	protected void createZOrderManager() {
		zOrderManager = new SimpleZOrderManager(this);
	}

	@Override
	public void initializeGeometry() {
		imageQuad = new Quad(name + "_quad", 32, 32);
		imageQuad.setModelBound(new OrthogonalBoundingBox());
		ItemMap.register(imageQuad, this);

		textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		textureState.setCorrectionType(TextureState.CorrectionType.Perspective);		
		imageQuad.setRenderState(textureState);
		texture = new Texture2D();
		texture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);

		attachChild(imageQuad);
	}

	@Override
	public Spatial getManipulableSpatial() {
		return imageQuad;
	}

	@Override
	public void setKeyboardDefinition(KeyboardDefinition kd) {
		this.keyboardDefinition = kd;

		Rectangle2D bounds = kd.getBounds();

		graphics = ImageGraphics.createInstance((int)bounds.getMaxX(), (int)bounds.getMaxY(),0);	

		texture.setImage( graphics.getImage() );
		texture.setScale( new Vector3f( 1f, -1f, 1 ) );		
		textureState.setTexture( texture );
		textureState.apply();		

		imageQuad.updateGeometry((float)bounds.getMaxX(), (float)bounds.getMaxY());
		imageQuad.updateModelBound();
		imageQuad.updateGeometricState(0f, true);
		imageQuad.updateRenderState();

		if(keyboardRenderer != null) {
			keyboardRenderer.drawKeyboard(graphics);		
			graphics.update(texture, false);
		}
	}

	@Override
	public KeyboardDefinition getKeyboardDefinition() {
		return keyboardDefinition;
	}

	@Override
	public void setKeyboardRenderer(IKeyboardRenderer keyboardRenderer) {
		this.keyboardRenderer = keyboardRenderer;
		keyboardRenderer.drawKeyboard(graphics);		
		graphics.update(texture, false);
	}

	@Override
	public void reDrawKeyboard() {
		if(keyboardRenderer != null) {
			keyboardRenderer.drawKeyboard(graphics);		
			graphics.update(texture, false);
		}
	}

}
