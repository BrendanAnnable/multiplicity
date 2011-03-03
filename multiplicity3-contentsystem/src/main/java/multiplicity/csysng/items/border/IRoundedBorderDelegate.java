package multiplicity.csysng.items.border;

import com.jme3.math.ColorRGBA;

import multiplicity.csysng.items.item.IItemDelegate;

public interface IRoundedBorderDelegate extends IItemDelegate {
	void setSize(float width, float height);
	void setBorderWidth(float borderSize);
	void setColor(ColorRGBA color);
}
