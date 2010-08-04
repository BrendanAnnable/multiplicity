package multiplicity.csysng.picksystem;

import java.util.List;

import multiplicity.csysng.items.IItem;

import com.jme.math.Vector2f;

public interface IPickSystem {
	public List<IItem> findItemsOnTableAtPosition(Vector2f position);
}
