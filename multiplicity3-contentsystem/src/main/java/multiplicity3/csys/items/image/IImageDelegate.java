package multiplicity3.csys.items.image;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItemDelegate;

public interface IImageDelegate extends IItemDelegate {
	void setSize(Vector2f size);
	void setImage(String imageResource);
}
