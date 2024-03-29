package multiplicity.csysng.factory;

import java.net.URL;
import java.util.UUID;

import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;

import com.jme.renderer.ColorRGBA;

public interface IHotSpotContentFactory {
    
    public IHotSpotFrame createHotSpotFrame(String name, UUID uuid, int width, int height);
    
    public IHotSpotRepo createHotSpotRepo(String name, UUID uuid, int width, int height);
  
    public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius);

	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA);
	
	public IHotSpotItem createHotSpotItemImage(String name, UUID uuid, URL imageResource);

    public IHotSpotText createEditableHotSpotText(String name, UUID uuid);
    
   
    public IHotSpotFrame createEditableHotSpotTextFrame(String name, UUID uuid,
            int width, int height, URL keyboardImage);


}
