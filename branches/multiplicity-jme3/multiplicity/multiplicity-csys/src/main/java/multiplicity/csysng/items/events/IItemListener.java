package multiplicity.csysng.items.events;

import multiplicity.csysng.items.IItem;
import multiplicity.input.events.MultiTouchCursorEvent;

public interface IItemListener {
	public void itemMoved(IItem item);
	public void itemRotated(IItem item);
	public void itemScaled(IItem item);
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event);
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event);
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event);
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event);
	public void itemZOrderChanged(IItem item);
}
