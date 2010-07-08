package multiplicity.csysng.items.keyboard;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KeyMaker {
	public static List<KeyboardKey> createSquareKeysLine(String keysOnePerChar, int width, int height, int leftIndent, int topIndent, int gapBetweenKeys) {
		List<KeyboardKey> keys = new ArrayList<KeyboardKey>();
		
		for(int i = 0; i < keysOnePerChar.length(); i++) {
			char keyChar = keysOnePerChar.charAt(i);
			int code;
			try {
				code = getKeyCode(keyChar);
				int x = leftIndent + (i * (width + gapBetweenKeys));
				int y = topIndent;
				Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);
				KeyboardKey k = new KeyboardKey(keyChar + "", code, rect);
				keys.add(k);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return keys;
	}
	
	public static int getKeyCode(char c) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
	    Field f = KeyEvent.class.getField("VK_" + Character.toUpperCase(c));
	    f.setAccessible(true);
	    return (Integer) f.get(null);
	}

}
