package multiplicity.csysng.behaviours.gesture;

import multiplicity.csysng.items.item.IItem;

public interface IGestureListener {
	public void gestureDetected(GestureMatch match, IItem item);
}
