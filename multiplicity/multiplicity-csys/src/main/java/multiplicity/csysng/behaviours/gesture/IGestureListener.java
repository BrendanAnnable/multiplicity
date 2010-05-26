package multiplicity.csysng.behaviours.gesture;

import multiplicity.csysng.items.IItem;

public interface IGestureListener {
	public void gestureDetected(GestureMatch match, IItem item);
}
