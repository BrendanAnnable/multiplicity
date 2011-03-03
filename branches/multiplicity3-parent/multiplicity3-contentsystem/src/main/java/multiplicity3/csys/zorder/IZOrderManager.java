package multiplicity3.csys.zorder;

import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;

public interface IZOrderManager extends IItemListener {
	public void setItemZOrder(int zValue);
	public int getItemZOrder();
	void childZSpaceRequirementChanged(IItem itemBeingManaged, IZOrderManager defaultZOrderManager);
	int getZSpaceRequirement();
	void setCapacity(int c);
	//void sendToBottom(IItem item, MultiTouchCursorEvent event);
	//void bringToTop(IItem itemPressed, MultiTouchCursorEvent event);
	//void registerForZOrdering(IItem item);
	//public void unregisterForZOrdering(IItem i);
	void updateZOrdering();
	//public void neverBringToTop(IItem item);	
}
