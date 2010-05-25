package multiplicity.csysngjme.zordering;

import java.util.ArrayList;
import java.util.List;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.input.events.MultiTouchCursorEvent;

public class NestedZOrderManager extends ItemListenerAdapter implements IZOrderManager {
	/*
	 * Smaller numbers closer, bigger numbers further away
	 * negative numbers nearer, positive numbers further!
	 */
	
	protected List<IItem> registeredItems = new ArrayList<IItem>();
	protected int startZOrder = 0;
	protected int usedZSpace = 1;
	protected int capacity = 1;
	protected IZOrderManager parentManager = null;
	protected IItem itemBeingManaged;

	public NestedZOrderManager(IItem item, int initialCapacity) {
		capacity = initialCapacity;
		itemBeingManaged = item;
		if(item != null && item.getParentItem() != null) {
			parentManager = item.getParentItem().getZOrderManager();
		}
	}
	
	@Override
	public void itemCursorPressed(IItem itemPressed, MultiTouchCursorEvent event) {
		bringToTop(itemPressed, event);
	}
	
	// IZorderManager methods
	
	@Override
	public void setCapacity(int c) {
		if(c > capacity) {
			this.capacity = c;
		}
	}
	
	@Override
	public int getZSpaceRequirement() {
		return capacity;
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
			if(parentManager != null) {
				parentManager.childZSpaceRequirementChanged(itemBeingManaged, this);
			}	
		}
	}
	
	@Override
	public void registerForZOrdering(IItem item) {
		if(!registeredItems.contains(item)) {
			registeredItems.add(0, item);
			item.getZOrderManager().setItemZOrder(usedZSpace);
			usedZSpace += item.getZOrderManager().getZSpaceRequirement();
			if(usedZSpace > capacity) {
				capacity *= 2;
				if(parentManager != null) {
					parentManager.childZSpaceRequirementChanged(itemBeingManaged, this);
				}	
			}
			item.addItemListener(this);			
		}
	}

	@Override
	public void bringToTop(IItem itemPressed, MultiTouchCursorEvent event) {
		registeredItems.remove(itemPressed);
		registeredItems.add(0, itemPressed);
		updateZOrdering();
	}
	
	@Override
	public void sendToBottom(IItem item, MultiTouchCursorEvent event) {
		registeredItems.remove(item);
		registeredItems.add(item);
		updateZOrdering();
	}	
	
//	@Override
//	public void bumpTop(IItem item) {
//	    int indexOf = registeredItems.indexOf(item);
//	    if( registeredItems.get(indexOf - 1) != null ) {
//	        IItem swap = registeredItems.get(indexOf - 1);
//	        
//	        registeredItems.remove(indexOf);
//	        registeredItems.remove(indexOf-1);
//	        
//	        registeredItems.add(indexOf-1, item);
//	        registeredItems.add(indexOf, swap);
//	        updateZOrdering();
//
//	    }// if
//	    
//	}
//	@Override
//	   public void bumpBottom(IItem item) {
//	        int indexOf = registeredItems.indexOf(item);
//	        if( registeredItems.size() > indexOf ) {
//	            IItem swap = registeredItems.get(indexOf + 1);
//	            
//	            registeredItems.remove(indexOf);
//	            registeredItems.remove(indexOf+1);
//	            
//	            registeredItems.add(indexOf+1, item);
//	            registeredItems.add(indexOf, swap);
//	            updateZOrdering();
//
//	        }// if
//	        
//	    }
	   
	@Override
	public void updateZOrdering() {
		if(itemBeingManaged != null && itemBeingManaged.getTreeRootSpatial() != null) {
			itemBeingManaged.getTreeRootSpatial().setZOrder(startZOrder);
		}
		int z = startZOrder;
		for(IItem i : registeredItems) {
			i.getZOrderManager().setItemZOrder(z);
			z += i.getZOrderManager().getZSpaceRequirement();
		}		
	}


	@Override
	public int getItemZOrder() {
		return this.startZOrder;
	}

	@Override
	public void setItemZOrder(int zValue) {
		this.startZOrder = zValue;
		updateZOrdering();
	}

	@Override
	public void neverBringToTop(IItem item) {
		item.removeItemListener(this);
	}	
}
