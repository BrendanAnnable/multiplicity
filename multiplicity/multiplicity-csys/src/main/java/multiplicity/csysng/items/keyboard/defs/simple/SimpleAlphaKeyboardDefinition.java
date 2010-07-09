package multiplicity.csysng.items.keyboard.defs.simple;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import multiplicity.csysng.items.keyboard.KeyMaker;
import multiplicity.csysng.items.keyboard.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.KeyboardKey;

public class SimpleAlphaKeyboardDefinition extends KeyboardDefinition {
	
	public SimpleAlphaKeyboardDefinition() {
		
		int standardKeyWidth = 64;
		int standardKeyHeight = 64;
		
		int gapBetweenKeys = 5;
		int gapBetweenLines = 5;
		int borderSize = 20;
		
		List<KeyboardKey> qwertyKeys = KeyMaker.createSquareKeysLine("QWERTYUIOP", standardKeyWidth, standardKeyHeight, borderSize, borderSize, gapBetweenKeys);
		addKeys(qwertyKeys);
		
		KeyboardKey p = getKey("P");
		Rectangle2D rect = new Rectangle2D.Float(p.getMaxX() + gapBetweenKeys, p.getMinY(), standardKeyWidth * 2, standardKeyHeight);
		addKey(new KeyboardKey("<---", KeyEvent.VK_BACK_SPACE, rect));
	
		List<KeyboardKey> asdfKeys = KeyMaker.createSquareKeysLine("ASDFGHJKL", standardKeyWidth, standardKeyHeight, borderSize + standardKeyWidth/3, standardKeyHeight + gapBetweenLines + borderSize, gapBetweenKeys);
		addKeys(asdfKeys);
		KeyboardKey l = getKey("L");
		rect = new Rectangle2D.Float(l.getMaxX() + gapBetweenKeys, l.getMinY(), standardKeyWidth * 2, standardKeyHeight);
		addKey(new KeyboardKey("Enter", KeyEvent.VK_ENTER, rect));
				
		List<KeyboardKey> zxcKeys = KeyMaker.createSquareKeysLine("ZXCVBNM", standardKeyWidth, standardKeyHeight, borderSize + standardKeyWidth/2, (standardKeyHeight + gapBetweenLines) * 2 + borderSize, gapBetweenKeys);
		addKeys(zxcKeys);
		KeyboardKey m = getKey("M");
		rect = new Rectangle2D.Float(m.getMaxX() + gapBetweenKeys, m.getMinY(), standardKeyWidth * 1.5f, standardKeyHeight);
		KeyboardKey rightShift = new KeyboardKey("SHIFT", KeyEvent.VK_SHIFT, rect);
		rightShift.setModifier(true);
		addKey(rightShift);
		
		rect = new Rectangle2D.Float(200, (standardKeyHeight + gapBetweenLines) * 3 + borderSize, 200, standardKeyHeight);		
		KeyboardKey spaceBar = new KeyboardKey("Space", KeyEvent.VK_SPACE, rect);
		addKey(spaceBar);
	}


	
}
