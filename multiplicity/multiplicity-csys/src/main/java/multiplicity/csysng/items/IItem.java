package multiplicity.csysng.items;

import java.util.List;
import java.util.UUID;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.events.MultiTouchEventDispatcher;
import multiplicity.csysng.zorder.IZOrderManager;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

public interface IItem extends INode {
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
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc);
	public void centerItem();	
	public void setRelativeRotation(float angle);
	public float getRelativeRotation();
	public float getWorldRotationDegrees();
	public void setRelativeScale(float scale);
	public float getRelativeScale();
	
	public IZOrderManager getZOrderManager();
	

	public void setInteractionEnabled(boolean b);
	
	// 'undocumented' API methods
	public Spatial getTreeRootSpatial();
	public Spatial getManipulableSpatial();
	
	public void behaviourAdded(IBehaviour behaviour);
	public List<IBehaviour> getBehaviours();
	public List<IBehaviour> getBehaviours(Class<? extends IBehaviour> clazz);

    public void setVisible(boolean isVisible);
	public boolean isVisible();

	
}
