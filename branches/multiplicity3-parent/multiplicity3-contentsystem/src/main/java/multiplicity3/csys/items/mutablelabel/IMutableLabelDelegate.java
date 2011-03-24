package multiplicity3.csys.items.mutablelabel;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItemDelegate;

public interface IMutableLabelDelegate extends IItemDelegate {
	void setText(String text);
	void setFont(String resourcePath);
	Vector2f getTextSize();
}
