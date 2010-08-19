package multiplicity.csysngjme.factory.hotspot;

import java.util.UUID;

import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.factory.ContentItemFactoryUtil;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.csysngjme.items.hotspots.HotSpotItem;
import multiplicity.csysngjme.items.hotspots.HotSpotText;

import com.jme.renderer.ColorRGBA;

public class HotSpotContentItemFactory implements IHotSpotContentFactory {
    
    @Override
    public IHotSpotFrame createHotSpotFrame(String name, UUID uuid, int width, int height) {
        HotSpotFrame frame = new HotSpotFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        frame.addTouchOverlay();
        return frame;
    }

	@Override
	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius) {
		IHotSpotItem pi = new HotSpotItem(name, uuid, radius);
        pi.initializeGeometry();
        return pi;
	}

	@Override
	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		IHotSpotItem pi = new HotSpotItem(name, uuid, radius, colorRGBA);
        pi.initializeGeometry();
        return pi;
	}


    @Override
    public IHotSpotText createEditableHotSpotText(String name, UUID uuid) {
        IHotSpotText text = new HotSpotText(name, uuid);
        text.initializeGeometry();
        return text;
    }
}
