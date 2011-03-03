package multiplicity3.jme3csys.picking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme3.scene.Spatial;

import multiplicity.csysng.items.item.ItemImpl;
import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.threedee.IThreeDeeContent;
import multiplicity3.jme3csys.threed.JMEThreeDeeContent;

public class ItemMap {
	private static Map<UUID, List<IItem>> itemMap;
	private static Map<UUID, List<IThreeDeeContent>> item3DMap;
	
	static {
		itemMap = new HashMap<UUID, List<IItem>>();
		item3DMap = new HashMap<UUID, List<IThreeDeeContent>>();
	}

	public static List<IItem> getItem(UUID uuid) {
		return itemMap.get(uuid);
	}
	
	public static List<IThreeDeeContent> getThreeD(UUID uuid) {
		return item3DMap.get(uuid);
	}

	/**
	 * Associates a spatial item to a content item. This is used
	 * by the <code>PickedItemDispatcher</code> to find out where
	 * to dispatch events to when a spatial is pressed on.
	 * @param spatial
	 * @param item
	 */
	public static void register(Spatial spatial, IItem item) {
		spatial.setUserData(ItemImpl.KEY_JMEITEMDATA, item.getUUID().toString());
		List<IItem> list = itemMap.get(item.getUUID());
		if(list == null) list = new ArrayList<IItem>();
		list.add(item);
		itemMap.put(item.getUUID(), list);
	}
	
	public static void register(Spatial spatial, IThreeDeeContent threeD) {
		spatial.setUserData(JMEThreeDeeContent.KEY_JMETHREEDEEITEMDATA, threeD.getUUID().toString());
		List<IThreeDeeContent> list = item3DMap.get(threeD.getUUID());
		if(list == null) list = new ArrayList<IThreeDeeContent>();
		list.add(threeD);
		item3DMap.put(threeD.getUUID(), list);
	}
	
	public static void unregister(Spatial spatial, IItem item) {
		itemMap.remove(item.getUUID());		
	}
	
	public static void unregister(Spatial spatial, IThreeDeeContent threeD) {
		itemMap.remove(threeD.getUUID());
	}
	
	public static String view() {
		String s = "ItemMap";
		for(UUID id : itemMap.keySet()) {
			List<IItem> list = itemMap.get(id);
			s += "\n  " + id + " -> " + list;	
		}
		return s;
	}

}
