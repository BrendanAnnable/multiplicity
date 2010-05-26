package multiplicity.csysngjme.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.events.MultiTouchEventDispatcher;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.csysngjme.picking.JMEItemUserData;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public abstract class JMEItem extends Node implements IItem {
	private static final long serialVersionUID = 3335181666188989601L;
	public static final String KEY_JMEITEMDATA = "KEY_JMEITEMDATA";
	
	private List<IItem> itemChildren = new ArrayList<IItem>();
	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();
	protected final List<IItemListener> itemListeners = new ArrayList<IItemListener>();
	protected IItem parentItem;
	protected UUID uuid;
	protected Quaternion rot = new Quaternion();
	protected float angle;
	protected IZOrderManager zOrderManager;


	public JMEItem(String name, UUID uuid) {
		super(name);
		this.uuid = uuid;
		setUserData(KEY_JMEITEMDATA, new JMEItemUserData(uuid));		
		setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		final JMEItem instance = this;
		dispatcher.addListener(new IMultiTouchEventListener() {			
			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) l.itemCursorReleased(instance, event);
			}
			
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) {
					l.itemCursorPressed(instance, event);
				}
			}
			
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) l.itemCursorClicked(instance, event);	
			}
			
			@Override
			public void cursorChanged(MultiTouchCursorEvent event) {
				for(IItemListener l : itemListeners) l.itemCursorChanged(instance, event);
			}
			
			@Override public void objectRemoved(MultiTouchObjectEvent event) {}
			@Override public void objectChanged(MultiTouchObjectEvent event) {}
			@Override public void objectAdded(MultiTouchObjectEvent event) {}
		});
		
				
	}
	


	public UUID getUUID() {
		return uuid;
	}
	
	
	Vector3f in = new Vector3f(); // temp, used for Vector2f to Vector3f functions
	Vector3f out = new Vector3f(); // temp, used for Vector2f to Vector3f functions
	
	@Override
	public void setWorldLocation(Vector2f newLoc) {
		in.set(newLoc.x, newLoc.y, 0);
		this.getParent().worldToLocal(in, getLocalTranslation());
		
		for(IItemListener l : itemListeners) {
			l.itemMoved(this);
		}
	}
	
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc) {		
		if(getParent() == null) return null;
		Vector3f in = new Vector3f(loc.x, loc.y, 0);
		Vector3f store = getParent().getWorldRotation().inverse().mult(in);				
		return new Vector2f(store.x, store.y);
	}
	
	@Override
	public Vector2f getWorldLocation() {
		this.getParent().localToWorld(getLocalTranslation(), out);
		return new Vector2f(out.x, out.y);
	}

	@Override
	public void setRelativeLocation(Vector2f newLoc) {
		getLocalTranslation().set(newLoc.x, newLoc.y, 0);
		for(IItemListener l : itemListeners) {
			l.itemMoved(this);
		}
	}
	
	@Override
	public Vector2f getRelativeLocation() {
		return new Vector2f(getLocalTranslation().x, getLocalTranslation().y);
	}	

	@Override
	public void setRelativeRotation(float angle) {
		this.angle = angle;
		rot.fromAngleAxis(angle, Vector3f.UNIT_Z);
		setLocalRotation(rot);
		for(IItemListener l : itemListeners) {
			l.itemRotated(this);
		}
	}

	@Override
	public void setRelativeScale(float scale) {
		setLocalScale(scale);
		for(IItemListener l : itemListeners) {
			l.itemScaled(this);
		}
	}


	@Override
	public float getRelativeRotation() {
		return angle;
	}

	@Override
	public float getRelativeScale() {
		return getLocalScale().getZ();
	}
	
	@Override
	public Spatial getTreeRootSpatial() {
		return this;
	}
	
	@Override
	public MultiTouchEventDispatcher getMultiTouchDispatcher() {
		return dispatcher;
	}
		
	@Override
	public IItem getParentItem() {
		return parentItem;
	}

	@Override
	public void setParentItem(IItem parent) {
		parentItem = parent;
	}
	
	@Override
	public void add(IItem item) {
		getItemChildren().add(item);
		attachChild(item.getTreeRootSpatial());
		item.setParentItem(this);
		getZOrderManager().registerForZOrdering(item);
	}
	
	@Override
	public int getChildrenCount() {
		return getItemChildren().size();
	}

	@Override
	public boolean hasChildren() {
		return getItemChildren().size() > 0;
	}
	
	public void addItemListener(IItemListener itemListener) {
		if(!itemListeners.contains(itemListener)) {
			itemListeners.add(itemListener);
		}	
	}
	
	public void removeItemListener(IItemListener itemListener) {
		itemListeners.remove(itemListener);
	}
	
	public abstract void initializeGeometry();
	protected abstract void createZOrderManager();
	
	public IZOrderManager getZOrderManager() {
		if(zOrderManager == null) createZOrderManager();
		return zOrderManager;
	}
	
	public void setInteractionEnabled(boolean b) {
		this.dispatcher.setEnabled(b);
	}

	public String toString() {
		return this.getClass().getName() + "(" + getManipulableSpatial() + ")";
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof JMEItem) {
			return ((JMEItem) obj).getUUID().equals(getUUID());
		}
		
		return false;
	}
	
	public void centerItem() {
		Vector2f center = new Vector2f(0, 0);
		setRelativeLocation(center);
	}
	
	private List<IBehaviour> behaviours = new ArrayList<IBehaviour>();	
	public void behaviourAdded(IBehaviour behaviour) {
		behaviours.add(behaviour);
	}
	
	public List<IBehaviour> getBehaviours() {
		return behaviours;
	}

    public List<IItem> getItemChildren() {
        return itemChildren;
    }
}

