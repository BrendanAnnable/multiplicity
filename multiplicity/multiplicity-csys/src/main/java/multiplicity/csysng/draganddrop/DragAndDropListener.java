package multiplicity.csysng.draganddrop;

import multiplicity.csysng.items.IItem;

public interface DragAndDropListener {
	/**
	 * 
	 * @param itemDropped Item that was dropped onto drag destination.
	 * @param onto Drag destination dropped onto.
	 * @param indexOfDrop If the item was on top, this will be zero, otherwise, how far underneath the top item.
	 */
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop);
}
