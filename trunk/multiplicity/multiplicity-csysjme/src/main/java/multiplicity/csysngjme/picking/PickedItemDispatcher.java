package multiplicity.csysngjme.picking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.scene.Node;

import multiplicity.jmeutils.UnitConversion;
import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.ItemMap;
import multiplicity.csysngjme.items.JMEItem;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class PickedItemDispatcher implements IMultiTouchEventListener {
	private Node pickRoot;
	private Map<Long, IItem> cursorItemAssociation = new HashMap<Long, IItem>();

	public PickedItemDispatcher(Node pickRoot) {
		this.pickRoot = pickRoot;
	}

	private void associate(long cursorID, IItem item) {
		cursorItemAssociation.put(cursorID, item);
	}

	private void disassociate(long cursorID) {
		cursorItemAssociation.remove(cursorID);
	}

	private IItem getAssociatedItem(long cursorID) {
		return cursorItemAssociation.get(cursorID);
	}


	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {		
		List<IItem> items = getPickedItemListener(event.getPosition().x, event.getPosition().y);
		if(items != null) {
			for(IItem item : items) {
				associate(event.getCursorID(), item);
				item.getMultiTouchDispatcher().cursorPressed(event);
			}
		}
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorReleased(event);
			disassociate(event.getCursorID());
		}
	}	

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorClicked(event);
		}
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorChanged(event);
		}
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().objectAdded(event);
		}
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().objectRemoved(event);
		}
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().objectChanged(event);
		}
	}



	Vector2f locStore = new Vector2f();
	protected List<IItem> getPickedItemListener(float x, float y) {
		UnitConversion.tableToScreen(x, y, locStore);
		List<PickedSpatial> picked = AccuratePickingUtility.pickAllOrthogonal(pickRoot, locStore);
		if(picked.size() > 0) {
			try {					
				JMEItemUserData itemData = (JMEItemUserData) picked.get(0).getSpatial().getUserData(JMEItem.KEY_JMEITEMDATA);
				if(itemData != null) {
					UUID uuid = itemData.getUUID();
					return ItemMap.getItem(uuid);					
				}
				return null;
			}catch(IllegalArgumentException ex) {
				// TODO: propagate?
			}
		}
		return null;
	}


}
