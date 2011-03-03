package multiplicity3.jme3csys.picking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.csys.threedee.IThreeDeeContent;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class PickedItemDispatcher implements IMultiTouchEventListener {
	private static final Logger log = Logger.getLogger(PickedItemDispatcher.class.getName());	
	
	//TODO: enable 3d picking too
	//	private Node threeDeePickRoot;
	private Map<Long, IItem> cursorItemAssociation = new HashMap<Long, IItem>();
	private Map<Long, IThreeDeeContent> cursor3DItemAssociation = new HashMap<Long, IThreeDeeContent>();

	private IStage stage;

	public PickedItemDispatcher(Node threeDeePickRoot, IStage stage) {
		this.stage = stage;
	}

	private void associate(long cursorID, IItem item) {
		cursorItemAssociation.put(cursorID, item);
	}

	private void associateThreeD(long cursorID, IThreeDeeContent contentItem) {
		cursor3DItemAssociation.put(cursorID, contentItem);
	}

	private void disassociate(long cursorID) {
		cursorItemAssociation.remove(cursorID);
	}

	private void disassociateThreeD(long cursorID) {
		cursor3DItemAssociation.remove(cursorID);
	}

	private IItem getAssociatedItem(long cursorID) {
		return cursorItemAssociation.get(cursorID);
	}

	private IThreeDeeContent getAssociatedThreeD(long cursorID) {
		return cursor3DItemAssociation.get(cursorID);
	}


	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {	
		log.fine("Attempting to pick from cursor event: " + event);
		
		// 2D
		List<IItem> items = getPickedItemListener(event.getPosition().x, event.getPosition().y);
		if(items != null && items.size() > 0) {
			//for(IItem item : items) {
			IItem item = items.get(0);
			associate(event.getCursorID(), item);
			item.getMultiTouchDispatcher().cursorPressed(event);
			//}
		}

		// 3D
		List<IThreeDeeContent> threeDContent = getPickedThreeDContent(event.getPosition().x, event.getPosition().y);
		if(threeDContent != null) {
			for(IThreeDeeContent contentItem : threeDContent) {
				associateThreeD(event.getCursorID(), contentItem);
				contentItem.getMultiTouchDispatcher().cursorPressed(event);
			}
		}
	}


	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorReleased(event);
			disassociate(event.getCursorID());
		}

		// 3D
		IThreeDeeContent threeD = getAssociatedThreeD(event.getCursorID());
		if(threeD != null) {
			threeD.getMultiTouchDispatcher().cursorReleased(event);
			disassociateThreeD(event.getCursorID());
		}
	}	

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorClicked(event);
		}

		// 3D
		IThreeDeeContent threeD = getAssociatedThreeD(event.getCursorID());
		if(threeD != null) {
			threeD.getMultiTouchDispatcher().cursorClicked(event);
		}
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if(item != null) {
			item.getMultiTouchDispatcher().cursorChanged(event);
		}

		// 3D
		IThreeDeeContent threeD = getAssociatedThreeD(event.getCursorID());
		if(threeD != null) {
			threeD.getMultiTouchDispatcher().cursorChanged(event);
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
		return stage.getContentSystem().getPickSystem().findItemsOnTableAtPosition(new Vector2f(x, y));
	}



	private List<IThreeDeeContent> getPickedThreeDContent(float x, float y) {
		//TODO: implement
		//		UnitConversion.tableToScreen(x, y, locStore);
		//		List<PickedSpatial> picked3D = AccuratePickingUtility.pickAll(threeDeePickRoot, locStore);
		//		if(picked3D.size() > 0) {
		//			JMEItemUserData itemData = (JMEItemUserData) picked3D.get(0).getSpatial().getUserData(JMEThreeDeeContent.KEY_JMETHREEDEEITEMDATA);
		//			if(itemData != null) {
		//				UUID uuid = itemData.getUUID();
		//				return ItemMap.getThreeD(uuid);
		//			}
		//		}
		return null;
	}
}
