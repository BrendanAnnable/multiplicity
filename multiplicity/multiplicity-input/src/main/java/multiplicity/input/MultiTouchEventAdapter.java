package multiplicity.input;

import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class MultiTouchEventAdapter implements IMultiTouchEventListener {

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

}
