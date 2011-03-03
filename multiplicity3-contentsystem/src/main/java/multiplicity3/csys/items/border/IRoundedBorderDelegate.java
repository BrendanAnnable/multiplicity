package multiplicity3.csys.items.border;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.item.IItemDelegate;

public interface IRoundedBorderDelegate extends IItemDelegate {
	void setSize(float width, float height);
	void setBorderWidth(float borderSize);
	void setColor(ColorRGBA color);
}
