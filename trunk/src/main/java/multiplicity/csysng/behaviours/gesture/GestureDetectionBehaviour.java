package multiplicity.csysng.behaviours.gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class GestureDetectionBehaviour implements IBehaviour, IMultiTouchEventListener {

	private IItem item;
	private List<IGestureListener> listeners = new ArrayList<IGestureListener>();
	private Map<Long, Gesture> currentGestures = new HashMap<Long,Gesture>();

	@Override
	public void setItemActingOn(IItem item) {
		this.item = item;		
	}
	
//	@Override
	public void addListener(IGestureListener l) {
		listeners.add((IGestureListener) l);
		item.getMultiTouchDispatcher().addListener(this);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		Gesture g = currentGestures.get(event.getCursorID());
		if(g != null) {
			g.addPoint(event.getPosition());
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		Gesture g = new Gesture("c" + event.getCursorID());
		g.addPoint(event.getPosition());
		currentGestures.put(event.getCursorID(), g);
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		Gesture g = currentGestures.get(event.getCursorID());
		if(g != null) {
			g.addPoint(event.getPosition());
		}
		detectGesture(g, event);		
		currentGestures.remove(event.getCursorID());
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
	
	private void detectGesture(Gesture g, MultiTouchCursorEvent event) {
		GestureMatch match = GestureLibrary.getInstance().findGestureMatch(g, 0.1f);
		if(match != null) {
			for(IGestureListener l : listeners) {
				l.gestureDetected(match, item);
			}
		}
	}

}
