package multiplicity.csysngjme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme.scene.Spatial;

import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.items.JMEItem;
import multiplicity.csysngjme.picking.JMEItemUserData;

public class ItemMap {
	private static Map<UUID, List<IItem>> itemMap;
	
	static {
		itemMap = new HashMap<UUID, List<IItem>>();
	}

	public static List<IItem> getItem(UUID uuid) {
		return itemMap.get(uuid);
	}

	public static void register(Spatial spatial, JMEItem item) {
		JMEItemUserData id = new JMEItemUserData(item.getUUID());
		spatial.setUserData(JMEItem.KEY_JMEITEMDATA, id);
		List<IItem> list = itemMap.get(item.getUUID());
		if(list == null) list = new ArrayList<IItem>();
		list.add(item);
		itemMap.put(item.getUUID(), list);
	}
	
	public static void unregister(Spatial spatial, JMEItem item) {
		itemMap.remove(item.getUUID());		
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
