package multiplicity.csysng.items.events;

import multiplicity.csysng.items.IItem;
import multiplicity.input.events.MultiTouchCursorEvent;

public class ItemListenerAdapter implements IItemListener {

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemMoved(IItem item) {}

	@Override
	public void itemRotated(IItem item) {}

	@Override
	public void itemScaled(IItem item) {}

	@Override
	public void itemZOrderChanged(IItem item) {}

}
