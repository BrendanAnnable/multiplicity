package multiplicity.csysngjme.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.INode.IChildrenChangedListener;
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
	
//	protected final List<IItemListener> itemListeners = new ArrayList<IItemListener>();
	private List<IItemListener> itemListeners = new CopyOnWriteArrayList<IItemListener>();
	protected IItem parentItem;
	protected UUID uuid;
	protected Quaternion rot = new Quaternion();
	protected float angle;
	private IZOrderManager zOrderManager;
	private String itemName;

	public JMEItem(String name, UUID uuid) {
		super(name);
		this.itemName = name;
		this.uuid = uuid;
		setUserData(KEY_JMEITEMDATA, new JMEItemUserData(uuid));		
		setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		final IItem instance = this;
		dispatcher.addListener(new IMultiTouchEventListener() {			
			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {
					for(IItemListener l : getItemListeners()) l.itemCursorReleased(instance, event);
			}
			
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) {
					l.itemCursorPressed(instance, event);
				}
			}
			
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) l.itemCursorClicked(instance, event);	
			}
			
			@Override
			public void cursorChanged(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) l.itemCursorChanged(instance, event);
			}
			
			@Override public void objectRemoved(MultiTouchObjectEvent event) {}
			@Override public void objectChanged(MultiTouchObjectEvent event) {}
			@Override public void objectAdded(MultiTouchObjectEvent event) {}
		});
		
		zOrderManager = createZOrderManager();
	}
	

	public String getName() {
	    return name;
	    
	}
	public UUID getUUID() {
		return uuid;
	}
	
	
	Vector3f in = new Vector3f(); // temp, used for Vector2f to Vector3f functions
	Vector3f out = new Vector3f(); // temp, used for Vector2f to Vector3f functions
	
	@Override
	public void setWorldLocation(Vector2f newLoc) {
		in.set(newLoc.x, newLoc.y, 0);
		if(this.getParent() == null) {
			getLocalTranslation().set(newLoc.x, newLoc.y, 0f);
		}else{
			this.getParent().worldToLocal(in, getLocalTranslation());
		}
		
		for(IItemListener l : getItemListeners()) {
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
		if(this.getParent() == null) return getRelativeLocation();
		this.getParent().localToWorld(getLocalTranslation(), out);
		return new Vector2f(out.x, out.y);
	}
	
	@Override
	public Vector2f getWorldLocation(Vector2f v) {
		if(this.getParent() == null) return v;
		this.getParent().localToWorld(new Vector3f(v.x, v.y, 0), out);
		return new Vector2f(out.x, out.y);
	}

	@Override
	public void setRelativeLocation(Vector2f newLoc) {
		getLocalTranslation().set(newLoc.x, newLoc.y, 0);
		for(IItemListener l : getItemListeners()) {
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
		for(IItemListener l : getItemListeners()) {
			l.itemRotated(this);
		}
	}

	@Override
	public void setRelativeScale(float scale) {
		setLocalScale(scale);
		for(IItemListener l : getItemListeners()) {
			l.itemScaled(this);
		}
	}

	@Override
	public float getRelativeRotation() {
		return angle;
	}
	
	private Vector3f worldRotationStore = new Vector3f();
	@Override
	public float getWorldRotationDegrees() {
		getWorldRotation().toAngleAxis(worldRotationStore);
		return worldRotationStore.z;
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
	public void addItem(IItem item) {
		getItemChildren().add(item);
		attachChild(item.getTreeRootSpatial());
		item.setParentItem(this);
		getZOrderManager().registerForZOrdering(item);
		zOrderManager.updateZOrdering();
		notifyChildrenChanged();
	}
	
	@Override
	public void removeItem(IItem item) {
		getItemChildren().remove(item);
		detachChild(item.getTreeRootSpatial());
		item.setParentItem(null);
		getZOrderManager().unregisterForZOrdering(item);
		notifyChildrenChanged();
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
		if(!getItemListeners().contains(itemListener)) {
			getItemListeners().add(itemListener);
		}	
	}
	
	public void removeItemListener(IItemListener itemListener) {
		getItemListeners().remove(itemListener);
	}
	
	/**
	 * Subclasses should use initializeGeometry to create
	 * the geometry/content. Content should be added by
	 * using attachChild()
	 */
	public abstract void initializeGeometry();
	
	
	protected abstract IZOrderManager createZOrderManager();
	
	public IZOrderManager getZOrderManager() {
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
			return ((IItem) obj).getUUID().equals(getUUID());
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
	
	public List<IBehaviour> getBehaviours(Class<? extends IBehaviour> clazz) {
		List<IBehaviour> bs = new ArrayList<IBehaviour>();
		for(IBehaviour b : getBehaviours()) {
			if(b.getClass().equals(clazz)) {
				bs.add(b);
			}
		}
		return bs;
	}

    public List<IItem> getItemChildren() {
        return itemChildren;
    }
    
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
    public List<IItemListener> getItemListeners() {
        return itemListeners;
    }

	@Override
	public void setItemListeners(List<IItemListener> itemListeners){
	    this.itemListeners = itemListeners;
	}
	
	@Override
	public void setVisible(boolean isVisible) {
		if(isVisible) {			
			this.setCullHint(CullHint.Never);
		}else{
			this.setCullHint(CullHint.Always);
		}
	}
	
	@Override
	public boolean isVisible() {
		return this.getCullHint() != CullHint.Always;
	}
	
	
	List<IChildrenChangedListener> childrenChangedListeners = new ArrayList<IChildrenChangedListener>();
	
	private void notifyChildrenChanged() {
		for(IChildrenChangedListener l : childrenChangedListeners) {
			l.childrenChanged(this, itemChildren);
		}
	}
	
	public void registerChildrenChangedListener(IChildrenChangedListener listener) {
		if(!childrenChangedListeners.contains(listener)) childrenChangedListeners.add(listener);
	}
	
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener) {
		childrenChangedListeners.remove(listener);
	}
	
}

