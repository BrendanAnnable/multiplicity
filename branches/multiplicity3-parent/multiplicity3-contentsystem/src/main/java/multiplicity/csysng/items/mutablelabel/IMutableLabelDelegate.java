package multiplicity.csysng.items.mutablelabel;

import multiplicity.csysng.items.item.IItemDelegate;

public interface IMutableLabelDelegate extends IItemDelegate {
	void setText(String text);
	void setFont(String resourcePath);
}
