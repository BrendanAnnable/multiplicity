package multiplicity3.jme3csys.items.item;

import java.util.logging.Logger;

import multiplicity.csysng.annotations.ImplementsContentItem;
import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.items.item.IItemDelegate;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

@ImplementsContentItem(target = IItem.class)
public abstract class JMEItemDelegate extends Node implements IItemDelegate {
	private static final Logger log = Logger.getLogger(JMEItemDelegate.class.getName());
	
	private static final long serialVersionUID = 3335181666188989601L;
	
	protected Quaternion rot = new Quaternion();
	
	private Geometry maskGeometry;
	
	public JMEItemDelegate() {
		super();	
	}
	
	@Override
	public void setMaskGeometry(Geometry maskGeometry) {
		this.maskGeometry = maskGeometry;		
	}

	@Override
	public Geometry getMaskGeometry() {
		return maskGeometry;
	}	
		
	private Vector3f tempLoc3f = new Vector3f(); // avoid the memory burn ;-)
	@Override
	public void setRelativeLocation(Vector2f newLoc) {
		tempLoc3f.set(newLoc.x, newLoc.y, getLocalTranslation().z);
		setLocalTranslation(tempLoc3f);
	}

	@Override
	public void setRelativeRotation(float angle) {
		rot.fromAngleAxis(angle, Vector3f.UNIT_Z);
		setLocalRotation(rot);
	}

	@Override
	public void setRelativeScale(float scale) {
		setLocalScale(scale);
	}
	
	@Override
	public Spatial getTreeRootSpatial() {
		return this;
	}
	
	@Override
	public void addItem(IItem item) {
		log.finer("Adding item " + item + " to " + this);
		JMEItemDelegate itemDelegate = (JMEItemDelegate) item.getDelegate();		
		attachChild(itemDelegate.getTreeRootSpatial());
	}
	
	@Override
	public void removeItem(IItem item) {
		log.finer("Removing item " + item + " from " + this);
		JMEItemDelegate itemDelegate = (JMEItemDelegate) item.getDelegate();		
		detachChild(itemDelegate.getTreeRootSpatial());
	}
	
	@Override
	public void setZOrder(int zOrder) {
		Vector3f newZOrder = getLocalTranslation().clone();
		setLocalTranslation(newZOrder.x, newZOrder.y, zOrder);
	}
	
	/**
	 * Subclasses should use initializeGeometry to create
	 * the geometry/content. Content should be added by
	 * using attachChild()
	 * @param assetManager 
	 */
	public abstract void initializeGeometry(AssetManager assetManager);
	
	@Override
	public void setVisible(boolean isVisible) {
		if(isVisible) {			
			this.setCullHint(CullHint.Never);
		}else{
			this.setCullHint(CullHint.Always);
		}
	}	
}

