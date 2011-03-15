package multiplicity3.csys.items.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.zorder.INestedZOrderManager;
import multiplicity3.csys.zorder.NestedZOrderManager;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public abstract class ItemImpl implements IItem {
	private static final Logger log = Logger.getLogger(ItemImpl.class.getName());

	public static final String KEY_JMEITEMDATA = "KEY_JMEITEMDATA";
//	public static final Node transformDelegateParent = new Node("_______ ROOT _______");

	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();
	protected UUID uuid;
	protected IItem parentItem;
	protected INestedZOrderManager zOrderManager;
	private IItemDelegate delegate;
	protected Node transformDelegate;
	private List<IItemListener> itemListeners = new CopyOnWriteArrayList<IItemListener>();
	private String itemName;
	private List<IItem> itemChildren = new ArrayList<IItem>();
	private float relativeRotation;
	private List<IBehaviour> behaviours = new ArrayList<IBehaviour>();
	private boolean isVisible;	
	private Quaternion trot = new Quaternion(); // temp
	private Vector3f tempWorldRotation = new Vector3f(); // temp

	public ItemImpl(String name, UUID uuid) {
		this.itemName = name;
		this.uuid = uuid;
		transformDelegate = new Node("td " + name + ":" + uuid);
		zOrderManager = new NestedZOrderManager(this, 5);
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

	}


	@Override
	public void setRelativeLocation(Vector2f newLoc) {
		
		transformDelegate.setLocalTranslation(newLoc.x, newLoc.y, transformDelegate.getLocalTranslation().z);
		delegate.setRelativeLocation(newLoc);

		for(IItemListener l : getItemListeners()) {
			l.itemMoved(this);
		}
	}

	@Override
	public void setWorldLocation(Vector2f loc) {
		log.finer("Setting world location to " + loc);
		if(transformDelegate.getParent() == null) {
			setRelativeLocation(new Vector2f(loc.x, loc.y));
		}else{
			Vector3f store = new Vector3f();
			transformDelegate.getParent().worldToLocal(new Vector3f(loc.x, loc.y, transformDelegate.getLocalTranslation().z), store);
			setRelativeLocation(new Vector2f(store.x, store.y));
		}		
	}

	@Override
	public Vector2f getRelativeLocation() {
		return new Vector2f(transformDelegate.getLocalTranslation().x, transformDelegate.getLocalTranslation().y);
	}

	private Vector3f out = new Vector3f();
	@Override
	public Vector2f getWorldLocation() {
		if(transformDelegate.getParent() == null) return getRelativeLocation();
		transformDelegate.getParent().localToWorld(transformDelegate.getLocalTranslation(), out);
		return new Vector2f(out.x, out.y);
	}

	@Override
	public Vector2f getWorldLocation(Vector2f v) {
		if(transformDelegate.getParent() == null) return v;
		transformDelegate.getParent().localToWorld(new Vector3f(v.x, v.y, transformDelegate.getLocalTranslation().z), out);
		return new Vector2f(out.x, out.y);
	}
	
	/**
	 * Converts an arbitrary location in world coordinates into local coordinates
	 * relative to this item.
	 */
	@Override
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc) {
		if(transformDelegate.getParent() == null) {
			return worldloc;
		}else{		
			Vector3f in = new Vector3f(worldloc.x, worldloc.y, transformDelegate.getLocalTranslation().z);
			transformDelegate.getParent().worldToLocal(in, out);
			return new Vector2f(out.x, out.y);
		}
	}

	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc) {		
		if(transformDelegate.getParent() == null) return null;
		Vector3f in = new Vector3f(loc.x, loc.y, transformDelegate.getLocalTranslation().z);
		Vector3f store = transformDelegate.getWorldRotation().inverse().mult(in);				
		return new Vector2f(store.x, store.y);
	}

	@Override
	public void setRelativeRotation(float angle) {
		this.relativeRotation = angle;
		trot.fromAngleAxis(angle, Vector3f.UNIT_Z);
		transformDelegate.setLocalRotation(trot);		
		delegate.setRelativeRotation(angle);

		for(IItemListener l : getItemListeners()) {
			l.itemRotated(this);
		}
	}

	@Override
	public float getRelativeRotation() {
		return relativeRotation;
	}


	@Override
	public float getWorldRotationDegrees() {
		transformDelegate.getWorldRotation().toAngleAxis(tempWorldRotation);
		return tempWorldRotation.z;
	}

	@Override
	public void setRelativeScale(float scale) {
		transformDelegate.setLocalScale(scale);
		delegate.setRelativeScale(scale);
		for(IItemListener l : getItemListeners()) {
			l.itemScaled(this);
		}
	}

	//TODO: support x, y, z scale
	@Override
	public float getRelativeScale() {
		return transformDelegate.getLocalScale().z;
	}


	public String getName() {
		return itemName;

	}

	public UUID getUUID() {
		return uuid;
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


	List<IChildrenChangedListener> childrenChangedListeners = new ArrayList<IChildrenChangedListener>();
	

	@Override
	public void registerChildrenChangedListener(IChildrenChangedListener listener) {
		if(!childrenChangedListeners.contains(listener)) childrenChangedListeners.add(listener);
	}

	@Override
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener) {
		childrenChangedListeners.remove(listener);
	}

	@Override
	public INestedZOrderManager getZOrderManager() {
		return zOrderManager;
	}

	@Override
	public void setInteractionEnabled(boolean b) {
		this.dispatcher.setEnabled(b);
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
		this.transformDelegate.removeFromParent();
		if(parent != null) {
			parent.getTransformDelegate().attachChild(transformDelegate);
		}
	}
	
	//TODO: really don't want this
	@Override
	public Node getTransformDelegate() {
		return transformDelegate;
	}

	@Override
	public void addItem(IItem item) {
		getItemChildren().add(item);
		getZOrderManager().childAttached(item);
		delegate.addItem(item);
		item.setParentItem(this);
		notifyChildrenChanged();
	}

	@Override
	public void removeItem(IItem item) {
		getItemChildren().remove(item);
		getZOrderManager().childRemoved(item);
		delegate.removeItem(item);
		item.setParentItem(null);
		notifyChildrenChanged();
	}

	@Override
	public void removeAllItems(boolean recursive) {
		if(recursive) {
			for(IItem item : getItemChildren()) {
				item.removeAllItems(recursive);
			}
		}
		
		for(IItem item : getItemChildren()) {
			removeItem(item);
		}
	}
	
	@Override
	public int getChildrenCount() {
		return getItemChildren().size();
	}

	@Override
	public boolean hasChildren() {
		return getItemChildren().size() > 0;
	}

	@Override
	public void addItemListener(IItemListener itemListener) {
		if(!getItemListeners().contains(itemListener)) {
			getItemListeners().add(itemListener);
		}	
	}

	public void removeItemListener(IItemListener itemListener) {
		getItemListeners().remove(itemListener);
	}



	@Override
	public void behaviourAdded(IBehaviour behaviour) {
		behaviours.add(behaviour);
	}

	@Override
	public List<IBehaviour> getBehaviours() {
		return behaviours;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz) {
		List<T> bs = new ArrayList<T>();
		for(IBehaviour b : getBehaviours()) {
			if(b.getClass().equals(clazz)) {				
				bs.add((T)b);
			}
		}
		return bs;
	}

	@Override
	public void centerItem() {
		Vector2f center = new Vector2f(0, 0);
		setRelativeLocation(center);
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		delegate.setVisible(isVisible);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setDelegate(IItemDelegate delegate) {
		this.delegate = delegate;
	}

	public IItemDelegate getDelegate() {
		return delegate;
	}
	

	@Override
	public void setZOrder(int zOrder) {
		delegate.setZOrder(zOrder);
	}
	
	private void notifyChildrenChanged() {
		for(IChildrenChangedListener l : childrenChangedListeners) {
			l.childrenChanged(this, itemChildren);
		}
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof IItem) {
			return ((IItem) obj).getUUID().equals(getUUID());
		}
		return false;
	}
	
	public String toString() {
		return this.getClass().getName() + " (" + getItemName() + ")";
	}



}
