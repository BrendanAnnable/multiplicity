package multiplicity3.jme3csys.items.image;

import java.util.logging.Logger;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.image.IImage;
import multiplicity.csysng.items.image.IImageDelegate;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.item.JMEItemDelegate;
import multiplicity3.jme3csys.picking.ItemMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

@ImplementsContentItem(target = IImage.class)
public class JMEImageDelegate extends JMEItemDelegate implements IImageDelegate {
	private static final Logger log = Logger.getLogger(JMEImageDelegate.class.getName());
	private CenteredQuad quad;
	private Geometry quadGeometry;
	private Material mat;
	private IImage item;
	private AssetManager assetManager;
	
	public JMEImageDelegate(IImage img) {
		this.item = img;
		setName(item.getName() + ":" + item.getUUID() + "_" + getClass().getName());
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
		quad = new CenteredQuad(100, 100);	
		quadGeometry = new Geometry("quad_geom", quad);
		
		// reminder of where to find j3md stuff: jme3/src/core-data
		mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		Texture tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
		mat.setTexture("m_ColorMap", tex);
		
		quadGeometry.setMaterial(mat);
		
		ItemMap.register(quadGeometry, item);
		log.fine("Attaching image quad geometry!");
		attachChild(quadGeometry);
	}
	
//	@Override
//	public void setZOrder(int zOrder) {
//		super.setZOrder(zOrder);
//		Vector3f newZOrder = quadGeometry.getLocalTranslation().clone();
//		quadGeometry.setLocalTranslation(newZOrder.x, newZOrder.y, zOrder);
//	}
	
	@Override
	public void setZOrder(int zOrder) {
		super.setZOrder(zOrder);
		Vector3f newZOrder = quadGeometry.getWorldTranslation().clone();
		newZOrder.z = zOrder;
		quadGeometry.getParent().worldToLocal(newZOrder, quadGeometry.getLocalTranslation());
	}


	@Override
	public void setSize(Vector2f size) {
		quad = new CenteredQuad(size.x, size.y);
		quadGeometry.setMesh(quad);
	}

	@Override
	public void setImage(String imageResource) {
		Texture tex = assetManager.loadTexture(imageResource);		
		mat.setTexture("m_ColorMap", tex);	
	}

	@Override
	public Spatial getManipulableSpatial() {
		return quadGeometry;
	}
}
