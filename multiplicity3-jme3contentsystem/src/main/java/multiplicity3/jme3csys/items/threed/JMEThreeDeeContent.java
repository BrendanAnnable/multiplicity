package multiplicity3.jme3csys.items.threed;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.items.threed.IThreeDeeContent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;

@ImplementsContentItem(target = IThreeDeeContent.class)
public class JMEThreeDeeContent extends JMEItem implements IThreeDeeContent, IInitable {
	private static final Logger log = Logger.getLogger(JMEThreeDeeContent.class.getName());

	public static final String KEY_JMETHREEDEEITEMDATA = "KEY_JMETHREEDEEITEMDATA";
	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();
	private String name;
	private UUID uuid;
	private Spatial spatial;

	private AssetManager assetManager;
	private String modelResource = "";
	private String textureResource = "";

	public JMEThreeDeeContent(String name, UUID uuid) {
		super(name, uuid);	
	}

	@Override
	public Spatial getSpatial() {		
		return spatial;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public MultiTouchEventDispatcher getMultiTouchDispatcher() {
		return dispatcher;
	}

	@Override
	public Spatial getManipulableSpatial() {
		return spatial;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	@Override
	public void setModel(File modelFile) {
		File parent = modelFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(), FileLocator.class);
		setModel(modelFile.getName());
	}
	
	@Override
	public void setModel(String modelResource) {
		this.modelResource = modelResource;
		spatial = assetManager.loadModel(modelResource);	
		spatial.setUserData(KEY_JMETHREEDEEITEMDATA, uuid.toString());
		ItemMap.register(spatial, this);
		log.fine("Attaching spatial geometry!");
		attachChild(spatial);
	}
	
	@Override
	public String getModel() {
		return modelResource;
	}
	
	@Override
	public void setTexture(File textureFile) {
		File parent = textureFile.getParentFile();
		assetManager.registerLocator(parent.getAbsolutePath(), FileLocator.class);
		setModel(textureFile.getName());
	}
	
	@Override
	public void setTexture(String textureResource) {
		this.textureResource = textureResource;
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap", assetManager.loadTexture(textureResource));
        spatial.setMaterial(material);
	}
	
	@Override
	public String getTexture() {
		return textureResource;
	}
	
	@Override
	public void setSize(float width, float height, float depth) {
		((BoundingBox)spatial.getWorldBound()).setXExtent(width);
		((BoundingBox)spatial.getWorldBound()).setYExtent(height);
		((BoundingBox)spatial.getWorldBound()).setZExtent(depth);		
	}

	@Override
	public Vector3f getSize() {
		float width = ((BoundingBox)spatial.getWorldBound()).getXExtent();
		float height = ((BoundingBox)spatial.getWorldBound()).getYExtent();
		float depth = ((BoundingBox)spatial.getWorldBound()).getZExtent();
		return new Vector3f(width, height, depth);		
	}

}
