package multiplicity.csysngjme.items.hotspots.listeners;

import java.util.List;

import org.apache.log4j.Logger;

import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class OverlayMultiTouchListener extends MultiTouchEventAdapter {

    private final static Logger logger = Logger.getLogger(OverlayMultiTouchListener.class.getName());

    private IColourRectangle overlay;

    public OverlayMultiTouchListener(IColourRectangle overlay) {
        this.overlay = overlay;
        this.overlay.getMultiTouchDispatcher().addListener(this);
    }
    
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        logger.debug("overlay pressed");
        IHotSpotFrame hotspotFrame = (IHotSpotFrame)overlay.getParentItem();
        hotspotFrame.bringHotSpotsToTop();
        hotspotFrame.bringPaletToTop();
    }
    
    @Override
    public void cursorChanged(MultiTouchCursorEvent event) {
        super.cursorChanged(event);
        logger.debug("overlay changed");
        IHotSpotFrame hotspotFrame = (IHotSpotFrame)overlay.getParentItem();
        
        HotSpotUtils.updateHotLinkSegments(hotspotFrame);

        hotspotFrame.updateOverLay();
    }
}
