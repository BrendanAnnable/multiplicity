package multiplicity3.csys.behaviours;

import java.util.logging.Logger;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class ThreeDRotateInteraction implements IBehaviour, IMultiTouchEventListener {
	private static final Logger log = Logger.getLogger(ThreeDRotateInteraction.class.getName());

	private long currentCursorID;
	private Vector2f cursorPressedPosition;

	private IItem eventSourceItem;
	private IItem affectedItem;

	@SuppressWarnings("unused")
	private IStage stage;
	private boolean active = true;

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		this.currentCursorID = -1;
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {			
		if(!active ) return;
		this.currentCursorID = event.getCursorID();
		this.cursorPressedPosition = event.getPosition();
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {			
		if(!active ) return;
		if(currentCursorID == event.getCursorID()) {
			
			Vector2f change = event.getPosition().subtract(cursorPressedPosition);
               
	       float[] angles = {-change.y/2, change.x/2, 0};
	       Quaternion q = new Quaternion();
	       q.fromAngles(angles);           
	       q.multLocal(affectedItem.getManipulableSpatial().getLocalRotation());
	       affectedItem.getManipulableSpatial().setLocalRotation(q);
		}
	}


	@Override
	public void setEventSource(IItem newSourceItem) {
		if(newSourceItem == eventSourceItem) {
			// no change
			return;
		}
		
		// already have an event source, so unregister it
		if(this.eventSourceItem != null) {
			this.eventSourceItem.getMultiTouchDispatcher().remove(this);
		}
		
		eventSourceItem = newSourceItem;
		eventSourceItem.getMultiTouchDispatcher().addListener(this);
	}
	
	@Override
	public void setItemActingOn(IItem item) {
		log.fine("Adding rotate translate scale behaviour to " + item);
		this.affectedItem = item;		
	}


	@Override
	public void setStage(IStage stage) {
		this.stage = stage;		
	}


	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}
}