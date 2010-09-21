package multiplicity.csysngjme.items.hotspots.listeners;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

public class OverlayBehavior extends MultiTouchEventAdapter implements IBehaviour {

    private final static Logger logger = Logger.getLogger(OverlayBehavior.class.getName());

    private IColourRectangle overlay;

    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        logger.debug("overlay pressed");
        IHotSpotFrame hotspotFrame = (IHotSpotFrame)overlay.getParentItem();
//        hotspotFrame.updateOverLay();
    }
    
    @Override
    public void cursorChanged(MultiTouchCursorEvent event) {
        super.cursorChanged(event);
        logger.debug("overlay changed");
        IHotSpotFrame hotspotFrame = (IHotSpotFrame)overlay.getParentItem();
        
        HotSpotUtils.updateHotLinkSegments(hotspotFrame);

        hotspotFrame.updateOverLay();
    }
    
    @Override
    public void removeItemActingOn() {
        if(overlay != null) {
            overlay.getMultiTouchDispatcher().remove(this);
        }
        this.overlay = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IColourRectangle) {
            this.overlay = (IColourRectangle) item;
            this.overlay.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }
}
