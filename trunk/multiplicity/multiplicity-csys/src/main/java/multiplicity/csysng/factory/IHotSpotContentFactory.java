package multiplicity.csysng.factory;

import java.util.UUID;

import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;

public interface IHotSpotContentFactory {
    
    public IFrame createHotSpotFrame(String name, UUID uuid, int width, int height);

    public IHotSpotItem createHotSpotItem(String name, UUID uuid, int width, int height);

    public IHotSpotItem createHotSpotItem(IHotSpotFrame hotSpotFrameContent,
            String string, UUID uuid, int width, int height);

}
