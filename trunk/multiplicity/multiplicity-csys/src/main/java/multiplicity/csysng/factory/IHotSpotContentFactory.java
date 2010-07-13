package multiplicity.csysng.factory;

import java.util.UUID;

import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;

import com.jme.renderer.ColorRGBA;

public interface IHotSpotContentFactory {
    
    public IHotSpotFrame createHotSpotFrame(String name, UUID uuid, int width, int height);
  
    public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius);

	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA);

}