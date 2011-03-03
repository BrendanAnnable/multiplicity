package multiplicity.csysng.items.image;

import com.jme3.math.Vector2f;

import multiplicity.csysng.items.item.IItemDelegate;

public interface IImageDelegate extends IItemDelegate {
	void setSize(Vector2f size);
	void setImage(String imageResource);
}
