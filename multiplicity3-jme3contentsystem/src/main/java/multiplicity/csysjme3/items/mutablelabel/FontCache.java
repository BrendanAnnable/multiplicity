package multiplicity.csysjme3.items.mutablelabel;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;

public class FontCache {
	private static FontCache instance;

	public static FontCache get() {
		synchronized(FontCache.class) { 
			if(instance == null) instance = new FontCache();
			return instance;
		}
	}
	
	private Map<String,BitmapFont> cacheMap;
	
	private FontCache() {
		cacheMap = new HashMap<String, BitmapFont>();
	}
	
	public BitmapFont getFont(String resource, AssetManager assetManager) {
		BitmapFont font = cacheMap.get(resource);
		if(font == null) {
			font = assetManager.loadFont(resource);
			cacheMap.put(resource, font);
		}
		return font;
	}
}
