package multiplicity.csysng.zorder;

import multiplicity.csysng.items.item.IItem;

public interface INestedZOrderManager extends IZOrderManager {
	void setAutoBringToTop(boolean enabled);
	void bringToTop(IItem item);
	void sendToBottom(IItem item);
	void childAttached(IItem item);
	void childRemoved(IItem item);
	void bringToTopPropagatesUp(boolean should);
}
