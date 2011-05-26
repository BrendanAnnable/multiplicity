package multiplicity3.csys.zorder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class NestedZOrderManager implements INestedZOrderManager {
	private static final Logger log = Logger.getLogger(NestedZOrderManager.class.getName());

	protected List<IItem> registeredItems = new ArrayList<IItem>();
	private int capacity = 1;
	protected int startZOrder = 1000;
	protected int usedZSpace = 1;
	private IItem itemBeingManaged;
	private boolean autoBringToTop = true;
	private boolean bringToTopPropagatesUp = true;

	public NestedZOrderManager(IItem itemBeingManaged, int initialCapacity) {
		capacity = initialCapacity;
		this.itemBeingManaged = itemBeingManaged;
	}

	public INestedZOrderManager getParentItemManager() {
		if(itemBeingManaged != null && itemBeingManaged.getParentItem() != null) {
			return itemBeingManaged.getParentItem().getZOrderManager();
		}
		return null;
	}

	@Override
	public void itemMoved(IItem item) {}

	@Override
	public void itemRotated(IItem item) {}

	@Override
	public void itemScaled(IItem item) {}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		log.fine("Press on " + item);
		if(autoBringToTop) {
			log.fine("Bringing " + item + " to the top");
			bringToTop(item);
		}
		if(bringToTopPropagatesUp) {
			if(getParentItemManager() != null) {
				getParentItemManager().bringToTop(item.getParentItem());
			}
		}

	}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemZOrderChanged(IItem item) {}

	@Override
	public int getItemZOrder() {
		return this.startZOrder;
	}

	@Override
	public void setItemZOrder(int zValue) {
		this.startZOrder = zValue;
		this.itemBeingManaged.setZOrder(zValue);
		updateZOrdering();
	}

	@Override
	public void childZSpaceRequirementChanged(IItem item, IZOrderManager manager) {
		int zReq = capacity;
		for(IItem i : registeredItems) {
			zReq += i.getZOrderManager().getZSpaceRequirement();
		}
		this.usedZSpace = zReq;
		if(usedZSpace > capacity) {
			capacity *= 2;
			if(getParentItemManager() != null) {
				getParentItemManager().childZSpaceRequirementChanged(itemBeingManaged, this);
			}	
		}
	}


	@Override
	public int getZSpaceRequirement() {
		return capacity;
	}

	@Override
	public void setCapacity(int c) {
		if(c > capacity) {
			this.capacity = c;
		}
	}

	@Override
	public void updateZOrdering() {		
		if(itemBeingManaged != null) {
			itemBeingManaged.setZOrder(startZOrder);
		}
		int z = startZOrder;
		for(IItem i : registeredItems) {
			i.getZOrderManager().setItemZOrder(z);
			z -= i.getZOrderManager().getZSpaceRequirement();
		}		
	}

	@Override
	public void childAttached(IItem item) {		
		if(!registeredItems.contains(item)) {
			registeredItems.add(0, item);
			item.getZOrderManager().setItemZOrder(usedZSpace);
			usedZSpace += item.getZOrderManager().getZSpaceRequirement();
			if(usedZSpace > capacity) {
				capacity *= 2;
				if(getParentItemManager() != null) {
					getParentItemManager().childZSpaceRequirementChanged(itemBeingManaged, this);
				}	
			}
			item.addItemListener(this);			
		}
		updateZOrdering();
	}

	@Override
	public void childRemoved(IItem item) {
		unregisterForZOrdering(item);
	}
	
	@Override
	public void unregisterForZOrdering(IItem item) {
		if(registeredItems.contains(item)) {
			registeredItems.remove(item);
			usedZSpace -= item.getZOrderManager().getZSpaceRequirement();
			item.removeItemListener(this);
		}
		updateZOrdering();		
	}	

	@Override
	public void bringToTop(IItem item) {
		registeredItems.remove(item);
		registeredItems.add(0, item);
		updateZOrdering();
	}

	@Override
	public void sendToBottom(IItem item) {
		registeredItems.remove(item);
		registeredItems.add(item);
		updateZOrdering();
	}

	@Override
	public void setAutoBringToTop(boolean enabled) {
		this.autoBringToTop  = enabled;

	}

	@Override
	public void setBringToTopPropagatesUp(boolean should) {
		this.bringToTopPropagatesUp  = should;		
	}

	@Override
	public void ignore(IItem item) {
		item.removeItemListener(this);
	}



}
