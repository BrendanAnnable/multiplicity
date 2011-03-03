package multiplicity3.csys.draganddrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.picksystem.IPickSystem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class DragAndDropSystem {

	private IPickSystem pickSystem;
	private Map<UUID,ItemDragAndDropWorker> workers = new HashMap<UUID,ItemDragAndDropWorker>();
	private Map<UUID,List<DragAndDropListener>> destinationListeners = new HashMap<UUID,List<DragAndDropListener>>();
	private IStage stage;
	
	public DragAndDropSystem(IStage stage) {
		this.stage = stage;
	}
	
	public void registerDragSource(IItem dragSource) {
		workers.put(dragSource.getUUID(), new ItemDragAndDropWorker(stage, dragSource));
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
		stage.getContentSystem().getDisplayManager().tableToScreen(event.getPosition(), cursorPosition);
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
