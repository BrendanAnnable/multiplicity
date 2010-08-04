package multiplicity.csysng.draganddrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme.math.Vector2f;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.picksystem.IPickSystem;
import multiplicity.input.events.MultiTouchCursorEvent;

public class DragAndDropSystem {
	private static DragAndDropSystem instance;	

	public static DragAndDropSystem getInstance() {
		synchronized(DragAndDropSystem.class) {
			if(instance == null) instance = new DragAndDropSystem();
			return instance;
		}
	}

	private IPickSystem pickSystem;
	private Map<UUID,ItemDragAndDropWorker> workers = new HashMap<UUID,ItemDragAndDropWorker>();
	private Map<UUID,List<DragAndDropListener>> destinationListeners = new HashMap<UUID,List<DragAndDropListener>>();
	
	private DragAndDropSystem() {
		
	}
	
	public void registerDragSource(IItem dragSource) {
		workers.put(dragSource.getUUID(), new ItemDragAndDropWorker(dragSource));
	}
	
	public void unregisterDragSource(IItem dragSource) {
		ItemDragAndDropWorker worker = workers.get(dragSource.getUUID());
		if(worker != null) {
			worker.stopWorking();
			workers.remove(dragSource.getUUID());
		}
	}
	
	public void registerDragDestinationListener(IItem dragDestination, DragAndDropListener listener) {
		List<DragAndDropListener> listeners = destinationListeners.get(dragDestination.getUUID());
		if(listeners == null) {
			listeners = new ArrayList<DragAndDropListener>();
			destinationListeners.put(dragDestination.getUUID(), listeners);
		}
		listeners.add(listener);
	}
	
	private Vector2f cursorPosition = new Vector2f();

	public void dropOccurred(MultiTouchCursorEvent event, IItem itemDropped) {
		ContentSystem.getContentSystem().getDisplayManager().tableToScreen(event.getPosition(), cursorPosition);
		List<IItem> items = pickSystem.findItemsOnTableAtPosition(cursorPosition);
		int index = 0;
		for(IItem x : items) {
			List<DragAndDropListener> listeners = destinationListeners.get(x.getUUID());
			if(listeners != null) {				
				for(DragAndDropListener listener : listeners) {
					listener.itemDraggedAndDropped(itemDropped, x, index);
				}
			}
			index++;
		}	
	}

	public void setPickSystemForApp(IPickSystem pickSystem) {
		this.pickSystem = pickSystem;		
	}


}
