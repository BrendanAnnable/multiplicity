package multiplicity.csysngjme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme.scene.Spatial;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.threedee.IThreeDeeContent;
import multiplicity.csysngjme.items.JMEItem;
import multiplicity.csysngjme.picking.JMEItemUserData;
import multiplicity.csysngjme.threedee.JMEThreeDeeContent;

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
		JMEItemUserData id = new JMEItemUserData(item.getUUID());
		spatial.setUserData(JMEItem.KEY_JMEITEMDATA, id);
		List<IItem> list = itemMap.get(item.getUUID());
		if(list == null) list = new ArrayList<IItem>();
		list.add(item);
		itemMap.put(item.getUUID(), list);
	}
	
	public static void register(Spatial spatial, IThreeDeeContent threeD) {
		JMEItemUserData id = new JMEItemUserData(threeD.getUUID());
		spatial.setUserData(JMEThreeDeeContent.KEY_JMETHREEDEEITEMDATA, id);
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
