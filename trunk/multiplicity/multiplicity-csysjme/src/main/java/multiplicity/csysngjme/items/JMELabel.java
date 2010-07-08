package multiplicity.csysngjme.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.UUID;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

import multiplicity.csysng.items.ILabel;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.zordering.SimpleZOrderManager;

public class JMELabel extends JMERectangularItem implements ILabel {
	private static final long serialVersionUID = 7699849986105817403L;
	private static float defaultHeight = 50f;
	
	private TextureState ts;
	private Quad textQuad;
	private Font font;
	protected String text;
	private BlendState blendState;
	private Color textColor;
	private Color transparentBackgroundColor;
	
	public JMELabel(String name, UUID uuid) {
		super(name, uuid);
	}
	
	@Override
	public void initializeGeometry() {
		text = "";
		font = new Font("Arial", Font.PLAIN, 10);
		transparentBackgroundColor = new Color(1, 1, 1, 0);
		createQuad(defaultHeight);
		attachChild(textQuad);
	}

	@Override
	public void setFont(Font f) {
		this.font = f;	
		update();
	}

	@Override
	public void setText(String text) {
		this.text = text;
		update();
	}
	
	@Override
	public void setTextColour(Color c) {
		this.textColor = c;
		update();
	}

	// private
    
    /**
     * 
     * @param scaleFactors is set to the factors needed to adjust texture coords
     * to the next-power-of-two- sized resulting image
     */
    protected BufferedImage getImage(Vector2f imageSize, Vector2f contentSize){
        BufferedImage tmp0 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) tmp0.getGraphics();
        Font drawFont = font;
        g2d.setFont(drawFont);
        Rectangle2D b = g2d.getFontMetrics().getStringBounds(text, g2d);
     
        int actualX = (int)b.getWidth() + 8;
        int actualY = (int)b.getHeight() + 8; // TODO: remove +8 fudges
        contentSize.x = (int)b.getWidth();
        contentSize.y = (int)b.getHeight();
        
        int desiredX = FastMath.nearestPowerOfTwo(actualX);
        int desiredY = FastMath.nearestPowerOfTwo(actualY);
        imageSize.x = desiredX;
        imageSize.y = desiredY;
        
        tmp0 = new BufferedImage(desiredX, desiredY, BufferedImage.TYPE_INT_ARGB);
        
        g2d = (Graphics2D) tmp0.getGraphics();
        g2d.setColor(transparentBackgroundColor);
        g2d.fillRect(0, 0, desiredX, desiredY);
        g2d.setFont(drawFont);
        g2d.setColor(textColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(text, 0, g2d.getFontMetrics().getMaxAscent());
        
        return tmp0;
    }
    
    protected void createQuad(float height){
    	textQuad = new Quad(getName() + "_quad");
    	textQuad.setModelBound(new OrthogonalBoundingBox());
    	textQuad.updateModelBound();
    	ItemMap.register(textQuad, this);
    	ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    	blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();		
		blendState.setSourceFunction(SourceFunction.SourceAlpha);
		blendState.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		blendState.setBlendEnabled(true);
        textQuad.setRenderState(ts);
        textQuad.setRenderState(blendState);
        textQuad.updateRenderState();        				
        update();
    }
    
	protected void update() {
		Vector2f imageSize = new Vector2f();
		Vector2f contentSize = new Vector2f();
		
		BufferedImage img = getImage(imageSize, contentSize);
		float yscale = contentSize.y / imageSize.y;
        textQuad.updateGeometry(contentSize.x, contentSize.y);
        setSize(contentSize.x, contentSize.y);
        textQuad.updateModelBound();
        Vector2f[] texCoords={
                new Vector2f(0,1),
                new Vector2f(0,1-yscale),
                new Vector2f(contentSize.x/imageSize.x,1-yscale),
                new Vector2f(contentSize.x/imageSize.x,1)
            };
        
        TexCoords texcords = new TexCoords(BufferUtils.createFloatBuffer(texCoords));
        textQuad.setTextureCoords(texcords);
		Texture tex = TextureManager.loadTexture(img, MinificationFilter.BilinearNoMipMaps, MagnificationFilter.Bilinear, true);
		ts.clearTextures();
        ts.setTexture(tex);
        ts.setEnabled(true);
        textQuad.updateRenderState();
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
		return textQuad;
	}
}
