package multiplicity.csysngjme.factory.hotspot;

import java.util.UUID;

import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysngjme.factory.ContentItemFactoryUtil;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;

public class HotSpotContentItemFactory implements IHotSpotContentFactory {
    
    @Override
    public IFrame createHotSpotFrame(String name, UUID uuid, int width, int height) {
        HotSpotFrame frame = new HotSpotFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }

}
