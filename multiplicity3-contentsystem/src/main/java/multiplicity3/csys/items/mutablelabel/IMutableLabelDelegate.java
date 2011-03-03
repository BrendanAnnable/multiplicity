package multiplicity3.csys.items.mutablelabel;

import multiplicity3.csys.items.item.IItemDelegate;

public interface IMutableLabelDelegate extends IItemDelegate {
	void setText(String text);
	void setFont(String resourcePath);
}
