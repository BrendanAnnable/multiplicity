package multiplicity3.csys.items.line;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.item.IItemDelegate;

public interface ILineDelegate extends IItemDelegate {

	void setLineWidth(float width);
	void setSourceItem(IItem item);
	void setDestinationItem(IItem item);
	void setEndPosition(Vector2f v);
	void setStartPosition(Vector2f v);	

}
