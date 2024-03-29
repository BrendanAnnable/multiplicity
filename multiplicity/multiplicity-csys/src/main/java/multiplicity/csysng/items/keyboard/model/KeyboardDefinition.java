package multiplicity.csysng.items.keyboard.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KeyboardDefinition {
	
	private List<KeyboardKey> keysCollection = new ArrayList<KeyboardKey>();
	private Map<String,KeyboardKey> stringToKeyMap = new HashMap<String,KeyboardKey>();

	public Iterable<KeyboardKey> getKeysIterator() {
		return keysCollection;
	}
	
	public void addKeys(Collection<KeyboardKey> qwertyKeys) {
		for(KeyboardKey k : qwertyKeys) {
			addKey(k);
		}		
	}
	
	public void addKey(KeyboardKey k) {
		keysCollection.add(k);
		stringToKeyMap.put(k.getKeyStringRepresentation(), k);
	}
	
	public KeyboardKey getKey(String stringRep) {
		return stringToKeyMap.get(stringRep);
	}
	
	public Rectangle2D getBounds() {
		Rectangle2D rect = new Rectangle2D.Float();
		if(keysCollection.size() > 0) {
			rect.setRect(keysCollection.get(0).getKeyShape().getBounds());
		}
		for(KeyboardKey k : keysCollection) {
			rect = rect.createUnion(k.getKeyShape().getBounds());
		}
		rect.setRect(rect.getX(), rect.getY(), rect.getWidth() + rect.getMinX(), rect.getHeight() + rect.getMinY());
		return rect;
	}

	public KeyboardKey getKeyAt(Point2D p) {
		for(KeyboardKey k : keysCollection) {
			if(k.getKeyShape().contains(p)) {
				return k;
			}
		}
		return null;
	}
}
