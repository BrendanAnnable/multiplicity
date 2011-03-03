package multiplicity.csysng.items.item;

import java.util.List;
import java.util.UUID;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.INestable;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.events.MultiTouchEventDispatcher;
import multiplicity.csysng.zorder.INestedZOrderManager;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public interface IItem extends INestable {
	public IItemDelegate getDelegate();
	public UUID getUUID();
	public void setParentItem(IItem parent);
	public IItem getParentItem();	
	public String getName();
	public MultiTouchEventDispatcher getMultiTouchDispatcher();
	
	public void addItemListener(IItemListener itemListener);
	public void removeItemListener(IItemListener itemListener);
	public List<IItemListener> getItemListeners();
	public void setItemListeners(List<IItemListener> itemListeners);
	
	public void setWorldLocation(Vector2f loc);
	public Vector2f getWorldLocation();
	
	/**
	 * Get the world location of an arbitrary point
	 * @param v
	 * @return
	 */
	public Vector2f getWorldLocation(Vector2f v);
	public void setRelativeLocation(Vector2f loc);
	public Vector2f getRelativeLocation();
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc);
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc);
	public void centerItem();	
	public void setRelativeRotation(float angle);
	public float getRelativeRotation();
	public float getWorldRotationDegrees();
	public void setRelativeScale(float scale);
	public float getRelativeScale();
	
	//TODO: really don't want this here...
	public Node getTransformDelegate();
	
	public INestedZOrderManager getZOrderManager();
	

	public void setInteractionEnabled(boolean b);
	
	// 'undocumented' API methods
	//public Spatial getTreeRootSpatial();
	//public Spatial getManipulableSpatial();
	
	public void behaviourAdded(IBehaviour behaviour);
	public List<IBehaviour> getBehaviours();
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz);

    public void setVisible(boolean isVisible);
	public boolean isVisible();
	void setZOrder(int zOrder);
	

	
}
