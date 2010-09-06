package multiplicity.appgallery.stitcher.listeners;

import java.util.List;

import multiplicity.appgallery.stitcher.StitcherApp;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.hotspots.HotLink;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;
import multiplicity.jmeutils.UnitConversion;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;

public class HotSpotItemMultiTouchListener extends MultiTouchEventAdapter {
    
    private final static Logger logger = Logger.getLogger(HotSpotItemMultiTouchListener.class.getName());

    
    private IHotSpotItem hotSpotItem;

    public HotSpotItemMultiTouchListener(IHotSpotItem hotSpotItem) {
        this.hotSpotItem = hotSpotItem;
        this.hotSpotItem.getMultiTouchDispatcher().addListener(this);
    }
    
    @Override
    public void objectChanged(MultiTouchObjectEvent event) {
        super.objectChanged(event);
        logger.debug("Hotspot CHANGED clicked");
    }
    @Override
    public void cursorChanged(MultiTouchCursorEvent event) {
        super.cursorChanged(event);
        if(hotSpotItem.getParentItem() != null && ( hotSpotItem.getParentItem() instanceof IHotSpotFrame )) {
             logger.debug("hotspot updating on change");
              hotSpotItem.updateHotSpot();
        }
    }
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        logger.debug("Hotspot cursor pressed");

        if (hotSpotItem.getParentItem() instanceof IHotSpotFrame) {

           
        }
    }
    
    @Override
    public void cursorReleased(MultiTouchCursorEvent event) {
        super.cursorReleased(event);
        logger.debug("HotspotItem cursor released");
        boolean offParent = true;

        JMEFrame hotSpotRepo = (JMEFrame) hotSpotItem.getParentItem();
        Vector2f locStore = new Vector2f();
        UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

        List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);

        
        
        for (IItem foundItem : findItemsOnTableAtPosition) {
            if ( foundItem.equals(hotSpotItem.getParentItem())) {

                logger.debug("HotspotItem  released on parent");
                if (foundItem instanceof IHotSpotRepo) {
                    hotSpotItem.centerItem();
                } else {
                    //do regular hotspot stuff
                    hotSpotItem.tap();
//                    hotSpotItem.getHotLink().changeBackgroundColor(new ColorRGBA(1f, 0f, 0f, .9f));
                    StitcherUtils.bringToTop(hotSpotItem.getHotSpotFrameContent());
                    IHotSpotFrame parentFrame = (IHotSpotFrame) hotSpotItem
                            .getParentItem();

      
                    if (!parentFrame.isLocked()) {
                        parentFrame.sendOverlayToBottom();
                    }

                    parentFrame.bringPaletToTop();

//                    stitcherApp.getZOrderManager().bringToTop(hotSpotItem.getHotLink(),null);
//                    if( hotSpotItem.getHotSpotFrameContent() instanceof IHotSpotText) {
//                        //redraw it
//                        HotSpotItemMultiTouchListener.updateHotSpots((IHotSpotFrame) hotSpotItem.getParentItem());
//                        parentFrame.sendHotLinksToTop();
//                        ((IHotSpotFrame) hotSpotItem.getHotSpotFrameContent()).sendHotLinksToTop();
//                    }
                    
                    // hide show
                    logger.debug("num of hspot taps " + hotSpotItem.getTapCount());
                    if (hotSpotItem.getTapCount() > 2 ) {
                        hotSpotItem.toggle();
                        hotSpotItem.resetTaps();
                        hotSpotItem.updateHotSpot();
                    }
                }

                return;

                
            } else if (foundItem instanceof IHotSpotFrame) {
                try {
                    if( (foundItem instanceof IHotSpotFrame) ) {
                         JMEFrame sourceFrame = (JMEFrame)foundItem;
                            IFrame originFrame = (IFrame) hotSpotItem.getParentItem();
                            originFrame.removeItem(hotSpotItem);

                            Vector2f itemWorldPos = hotSpotItem.getWorldLocation();
                            sourceFrame.addItem(hotSpotItem);
                            hotSpotItem.setWorldLocation(itemWorldPos);
                            sourceFrame.getZOrderManager().bringToTop(hotSpotItem, null);

                            // add HS to the array attached to the frame
                            IHotSpotItem hsItem = (IHotSpotItem)hotSpotItem;
                            ((IHotSpotFrame) sourceFrame).addHotSpot(hsItem);

                            IHotSpotFrame hotSpotFrameContent = StitcherUtils.createNewHotSpotFrame(hsItem.getType());
//                            IHotSpotFrame hotSpotFrameContent = stitcherApp.createNewHotSpotContentFrame(hsItem.getType());

                            hsItem.setHotSpotFrameContent(hotSpotFrameContent);

                            IHotLink l = (HotLink) hsItem.createHotLink();
                    
                            StitcherUtils.addHotLink(l);
                            

                            logger.debug("dropped hotspotitem on " + sourceFrame.getName());

                            // create a new hotspot candidate
                            StitcherUtils.fillHotSpotRepo(originFrame,hsItem.getType());
                             
                            StitcherUtils.addHotSpotContentFrame(hotSpotFrameContent);
                            
                            StitcherUtils.updateHotShotContentFrames();
                            
                            if( hotSpotFrameContent instanceof IHotSpotText ) {
                                StitcherUtils.bringToTop(l);
                            }
                    }

//              

                } catch (Exception e) {
                    logger.debug("HotSpotItem Exception: " + e);
                }// try
                
                return;
            }// if
        }// for

        //move no place
            hotSpotItem.centerItem();
            logger.debug("hotspotitem in the mist");

    }

}
