package multiplicity3.csys.zorder;

import multiplicity3.csys.items.item.IItem;

public interface INestedZOrderManager extends IZOrderManager {
	void setAutoBringToTop(boolean enabled);
	void setBringToTopPropagatesUp(boolean should);
	void bringToTop(IItem item);
	void sendToBottom(IItem item);
	void childAttached(IItem item);
	void childRemoved(IItem item);
	void ignore(IItem background);
}
