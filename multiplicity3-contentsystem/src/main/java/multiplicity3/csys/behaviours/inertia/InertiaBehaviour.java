package multiplicity3.csys.behaviours.inertia;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.animation.elements.behaviourelements.InertiaAnimationElement;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class InertiaBehaviour implements IBehaviour, IMultiTouchEventListener {
	
	ItemPositionHistory positionHistory;
	InertiaAnimationElement iae;
	
	int cursorCount = 0;
	private IItem eventSource;
	private IItem targetItem;
	protected IStage stage;
	
	@Override
	public void setEventSource(IItem eventSourceItem) {
		if(eventSourceItem == null && eventSource != null) {
			eventSource.getMultiTouchDispatcher().remove(this);
		}
		
		eventSource = eventSourceItem;
		eventSource.getMultiTouchDispatcher().addListener(this);
	}

	@Override
	public void setItemActingOn(IItem item) {
		this.targetItem = item;
		iae = new InertiaAnimationElement(item);
		positionHistory = new ItemPositionHistory(item);		
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {		
		if(cursorCount == 1) {
			positionHistory.add(event.getPosition(), System.currentTimeMillis());
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		cursorCount++;		
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		if(cursorCount == 1) {
			positionHistory.add(event.getPosition(), System.currentTimeMillis());			
			Vector2f worldToLocalV = targetItem.convertWorldVelocityToLocalVelocity(positionHistory.getVelocity());
			iae.moveWithVelocity(worldToLocalV);
			AnimationSystem.getInstance().add(iae);
			positionHistory.clear();
		}
		cursorCount--;
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

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;		
		positionHistory.setStage(stage);
	}



}
