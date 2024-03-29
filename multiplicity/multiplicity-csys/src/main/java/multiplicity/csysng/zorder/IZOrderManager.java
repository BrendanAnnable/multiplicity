package multiplicity.csysng.zorder;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.input.events.MultiTouchCursorEvent;

public interface IZOrderManager extends IItemListener {
	public void setItemZOrder(int zValue);
	public int getItemZOrder();
	void childZSpaceRequirementChanged(IItem itemBeingManaged, IZOrderManager defaultZOrderManager);
	int getZSpaceRequirement();
	void setCapacity(int c);
	void sendToBottom(IItem item, MultiTouchCursorEvent event);
	//void bumpTop(IItem item);
	//void bumpBottom(IItem item);
	void bringToTop(IItem itemPressed, MultiTouchCursorEvent event);
	void registerForZOrdering(IItem item);
	public void unregisterForZOrdering(IItem i);
	void updateZOrdering();
	public void neverBringToTop(IItem item);
	
}
