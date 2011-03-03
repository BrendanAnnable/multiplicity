package multiplicity.csysng.draganddrop;

import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.stage.IStage;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class ItemDragAndDropWorker implements IMultiTouchEventListener {
	private IItem item;
	private IStage stage;

	public ItemDragAndDropWorker(IStage stage, IItem item) {
		this.stage = stage;
		this.item = item;
		item.getMultiTouchDispatcher().addListener(this);
	}
	
	public void stopWorking() {
		item.getMultiTouchDispatcher().remove(this);
	}
	
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		stage.getContentSystem().getDragAndDropSystem().dropOccurred(event, item);		
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}
}
