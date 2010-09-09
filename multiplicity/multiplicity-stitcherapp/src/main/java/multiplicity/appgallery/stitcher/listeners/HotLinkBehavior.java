package multiplicity.appgallery.stitcher.listeners;

import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

public class HotLinkBehavior extends MultiTouchEventAdapter implements IBehaviour {
    
    private final static Logger logger = Logger.getLogger(HotLinkBehavior.class.getName());
    private IHotLink hotLink;
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        logger.debug("hotlink PRESSED");
        
        //hide show
//        if( hotLink.tap() > 4 ) {
//          logger.debug("deleting hotlink");
//          
//          
//          IHotSpotItem hotSpotItem = hotLink.getHotSpotItem();
//          
//          hotSpotItem.removeHotLink(hotLink);
//          
//          IHotSpotFrame sourceFrame = (IHotSpotFrame) hotLink.getHotSpotItem().getParentItem();
//          IHotSpotFrame hotSpotFrameContent = hotLink.getHotSpotItem().getHotSpotFrameContent();
//          if(!(hotSpotFrameContent instanceof IHotSpotText) ) {
//              hotSpotFrameContent.removeHotSpot(hotSpotItem);
//          }
//          
//          //remove hotspot
//          sourceFrame.removeHotSpot(hotSpotItem);
//          
//          
//          StitcherUtils.removeHotThing(hotLink);
//          StitcherUtils.removeHotThing(hotSpotFrameContent);
//;
//        
//          sourceFrame.bringHotSpotsToTop();
//          
//        }
    }

    @Override
    public void removeItemActingOn() {
        if(hotLink != null) {
            hotLink.getMultiTouchDispatcher().remove(this);
        }
        this.hotLink = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IHotLink) {
            this.hotLink = (IHotLink) item;
            hotLink.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }

}
