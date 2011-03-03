package multiplicity.csysng.behaviours.button;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.stage.IStage;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class ButtonBehaviour implements IBehaviour {

	private IItem item;

	@Override
	public void setEventSource(IItem eventSourceItem) {
		this.item = eventSourceItem;		
	}
	
	public void setItemActingOn(final IItem item) {
		
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
	public void setStage(IStage stage) {
		// TODO Auto-generated method stub
		
	}


}
