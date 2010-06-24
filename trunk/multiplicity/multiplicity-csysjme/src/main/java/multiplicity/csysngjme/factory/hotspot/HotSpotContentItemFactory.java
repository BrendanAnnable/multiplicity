package multiplicity.csysngjme.factory.hotspot;

import java.util.UUID;

import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.factory.ContentItemFactoryUtil;
import multiplicity.csysngjme.items.HotSpotItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;

public class HotSpotContentItemFactory implements IHotSpotContentFactory {
    
    @Override
    public IFrame createHotSpotFrame(String name, UUID uuid, int width, int height) {
        HotSpotFrame frame = new HotSpotFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IHotSpotItem createHotSpotItem(String name, UUID uuid, int width, int height) {
        HotSpotItem hs = new HotSpotItem(name, uuid, width, height);
        hs.initializeGeometry();
        return hs;
    }

}
