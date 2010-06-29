package multiplicity.csysng.behaviours.button;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class ButtonBehaviour implements IBehaviour {

	private IItem item;

	public void setItemActingOn(final IItem item) {
		this.item = item;	
	}

	public void addListener(final IButtonBehaviourListener l) {
		if(l != null) {
			final IButtonBehaviourListener bb = (IButtonBehaviourListener) l;
			item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorReleased(MultiTouchCursorEvent event) {
					bb.buttonReleased(item);				
				}

				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {
					bb.buttonPressed(item);
				}

				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {
					bb.buttonClicked(item);				
				}
			});
		}
	}

    @Override
    public void removeItemActingOn() {
        
    }
}
