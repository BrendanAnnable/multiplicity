package multiplicity.csysng.picksystem;

import java.util.List;

import multiplicity.csysng.items.item.IItem;

import com.jme3.math.Vector2f;

public interface IPickSystem {
	/**
	 * Find items at a position in screen coordinates.
	 * @param position
	 * @return
	 */
	public List<IItem> findItemsOnTableAtPosition(Vector2f position);
}
