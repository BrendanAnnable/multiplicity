package multiplicity.appgallery.stitcher.listeners;

import java.util.ArrayList;
import java.util.List;

import multiplicity.appgallery.stitcher.IStitcherContants;
import multiplicity.appgallery.stitcher.StitcherApp;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IImageRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.jmeutils.UnitConversion;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Spatial;

public class ImageItemListener extends ItemListenerAdapter {

    private final static Logger logger = Logger.getLogger(ImageItemListener.class.getName());

    private ArrayList<HotSpotFrame> highlightedFrames = new ArrayList<HotSpotFrame>();

    private StitcherApp stitcherApp;

    public ImageItemListener(StitcherApp stitcherApp) {
        this.stitcherApp = stitcherApp;
    }
    @Override
    public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
        super.itemCursorChanged(item, event);
//        String message = "item moved over: ";
//        boolean firstFrameFound = false;
//        boolean offParent = true;
//        Vector2f locStore = new Vector2f();
//        UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);
//
//        List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);
//        
//        if (item.getParentItem() instanceof JMEFrame) {
//            JMEFrame frame = (JMEFrame) item.getParentItem();
//            for (IItem pickedItem : findItemsOnTableAtPosition) {
//                
//                if( pickedItem.equals(frame)) {
//                    offParent = false;
//                    message = message + "it's parent frame SCANS.";
//                }
//                
////              if (pickedSpatial.getSpatial().equals(frame.getMaskGeometry())) {
////                  offParent = false;
////                  message = message + "it's parent frame SCANS.";
////              }
//            }
//        }
//
//        if ( (item.getParentItem() instanceof IRepositoryFrame) && offParent) {
//            for (IItem foundItem : findItemsOnTableAtPosition) {
//                if (foundItem instanceof JMEFrame && firstFrameFound == false) {
//                    try {
//                        Geometry geometry = (Geometry) foundItem.getManipulableSpatial();
//
//                        if (foundItem instanceof HotSpotFrame) {
//                            HotSpotFrame targetFrame = (HotSpotFrame)foundItem;
//
//                            if (targetFrame.getName().contains("hotspotf-") && !highlightedFrames.contains(targetFrame)) {
//                                message = message + targetFrame.getName() + ": let's turn it red";
//                                firstFrameFound = true;
//                                IBorder border = targetFrame.getBorder();
//                                border.setColor(new ColorRGBA(1f, 0f, 0f, 0.6f));
//                                // targetFrame.setBorder(getHighlightedFrame());
//                                highlightedFrames.add(targetFrame);
//                            } else if (highlightedFrames.contains(targetFrame)) {
//                                message = message + "a hotspot frameprobably already red";
//                                for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
//                                    if (!hotSpotFrame.equals(targetFrame)) {
//                                        IBorder border = hotSpotFrame.getBorder();
//                                        border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
//                                        // hotSpotFrame.setBorder(getNormalFrame());
//                                        highlightedFrames.remove(hotSpotFrame);
//                                    }
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
//                    }
//                }
//            }
//        }
//
//        if (!firstFrameFound && offParent) {
//            firstFrameFound = false;
//            message = message + "something else than a hotspotframe or the parent (i.e. the mist)";
//
//            for (IItem foundItem : findItemsOnTableAtPosition) {
//                if (foundItem instanceof JMEFrame && firstFrameFound == false) {
//                    try {
//
//                        if (foundItem instanceof HotSpotFrame) {
//                            IHotSpotFrame targetFrame = (IHotSpotFrame)foundItem;
//                            if (highlightedFrames.contains(targetFrame)) {
//                                firstFrameFound = true;
//                                IBorder border = targetFrame.getBorder();
//                                border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
//                                // targetFrame.setBorder(getNormalFrame());
//                                highlightedFrames.remove(targetFrame);
//                            }
//                        }
//                    } catch (Exception e) {
//                        logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
//                    }
//                }
//            }
//
//            if (!firstFrameFound) {
//                clearAllHighlightedHotSpotFrames();
//            }
//        }
//
//        logger.info(message);
    }

    @Override
    public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
        super.itemCursorPressed(item, event);
        logger.info("item pressed" + item.getBehaviours() + "parent: " + item.getParentItem());
    }

    @Override
    public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
        super.itemCursorClicked(item, event);
        logger.info("item clicked" + item.getBehaviours());
    }

    @Override
    public void itemCursorReleased(IItem releasedItem, MultiTouchCursorEvent event) {
        super.itemCursorReleased(releasedItem, event);
        logger.debug("Image cursor released event");

        Vector2f locStore = new Vector2f();
        UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);
        List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);
        
        if( releasedItem.getParentItem() != null ) {
            
            //check to see if it was released on the parent.
            if ( releasedItem.getParentItem() instanceof IRepositoryFrame) {
                IRepositoryFrame repositoryFrame = (IRepositoryFrame) releasedItem.getParentItem();
                for (IItem foundItem : findItemsOnTableAtPosition) {
                     
                    if( isHotspotFrame(foundItem)) {
                        if( releasedItem.getParentItem() instanceof IImageRepositoryFrame ) {
                            logger.debug("image released on hotspot");
                            doBackgroundFrameDrop(releasedItem, (IHotSpotFrame) foundItem);
                        }
                        return;
                    } else if( isRepository(foundItem)) {
                        logger.debug("image released on its parent");
                        return;
                    }
                }// for
            }// if
        
            
            if( releasedItem.getParentItem() instanceof IBackgroundRepositoryFrame) {
                //is a background
                logger.debug("background released on main surface");
           
                
                if (releasedItem instanceof IImage) {
                    IRepositoryFrame backgroundFrameRepository = (IRepositoryFrame) releasedItem.getParentItem();
                    backgroundFrameRepository.removeItem(releasedItem);

                   
                   
                    // create copy
                    
                    
                    
//                    
                    IImage copyImage = copyImage((IImage) releasedItem, backgroundFrameRepository);
                    Vector2f position = StitcherUtils.generateRandomPosition((JMEFrame) backgroundFrameRepository, copyImage);
                    copyImage.setRelativeLocation(position);

                    backgroundFrameRepository.addItem(copyImage);
                    
                    stitcherApp.createNewFrame(releasedItem, new Vector2f(0.0f,
                            300.0f), IStitcherContants.BACKGROUND + "-" + releasedItem.getUUID(),
                            IStitcherContants.BACKGROUND);


                    backgroundFrameRepository.close();
//                    backgroundFrameRepository.removeItem(releasedItem);

                    return;
                }
              
            }
        }
    }

    /**
     * copy an image
     * 
     * @param imageItem
     * @param frame
     * @return
     */
    private IImage copyImage(IImage imageItem, IFrame frame) {
        IImage copy = StitcherUtils.createPhotoImage(imageItem
                .getImageUrl());

//        float scale = (Float) getScale(((JMERectangularItem) copy)
//                .getSize());
        copy.setRelativeScale(imageItem.getRelativeScale());
        copy.addItemListener(new ImageItemListener(stitcherApp));
        frame.addItem(copy);
        return copy;

    }
    
    /**
     * drop an item on the frame
     * 
     * @param releasedItem
     * @param frameDroppedOn
     */
    private void doBackgroundFrameDrop(IItem releasedItem, IItem frameDroppedOn) {
        if( releasedItem.getParentItem() instanceof IImageRepositoryFrame ) {
            logger.debug("doing a background frame drop");
            IHotSpotFrame hotspotFrame = (IHotSpotFrame)frameDroppedOn;
            IRepositoryFrame repository = (IRepositoryFrame) releasedItem.getParentItem();

            if( hotspotFrame.isLocked() == false ) {
                repository.removeItem(releasedItem);
                Vector2f itemWorldPos = releasedItem.getWorldLocation();
                hotspotFrame.addItem(releasedItem);
                releasedItem.setWorldLocation(itemWorldPos);
                hotspotFrame.getZOrderManager().bringToTop(releasedItem, null);
                hotspotFrame.bringHotSpotsToTop();
                hotspotFrame.bringPaletToTop();

                IImage copyImage = copyImage((IImage) releasedItem, repository);
                repository.addItem(copyImage);
                repository.open();
                repository.close();
            } else {
                //take it back
                releasedItem.centerItem();
            }
        }// if
    }
 
    /**
     * remove all the highlights
     */
    private void clearAllHighlightedHotSpotFrames() {
        for (IHotSpotFrame hotSpotFrame : highlightedFrames) {
            IBorder border = hotSpotFrame.getBorder();
            border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
        }
        highlightedFrames = new ArrayList<HotSpotFrame>();
    }

    /**
     * drop an item on a hotspot frame
     * 
     * @param item
     * @param hotSpotFrame
     */
    protected void dropOnHotSpotFrame(IItem item, IHotSpotFrame hotSpotFrame) {
        IFrame frame = (IFrame) item.getParentItem();
        frame.removeItem(item);

        hotSpotFrame.addItem(item);
//        item.setRelativeScale(1.0f);
        ((JMERectangularItem) item).setSize(IStitcherContants.HOTSPOT_FRAME_DIMENSION, IStitcherContants.HOTSPOT_FRAME_DIMENSION);
        item.centerItem();
        clearAllHighlightedHotSpotFrames();
        hotSpotFrame.setLocked(true);
        hotSpotFrame.getPalet().lockPalet(hotSpotFrame.isLocked());
        hotSpotFrame.bringPaletToTop();
        hotSpotFrame.bringHotSpotsToTop();
        hotSpotFrame.getZOrderManager().sendToBottom(item, null);

//        stitcherApp.bumpHotSpotConnections();
    }
    
    /**
     * check if it is a hotspot frame or not
     * 
     * @param item
     * @return
     */
    public boolean isHotspotFrame(IItem item) {
        if( item != null && ( item instanceof IHotSpotFrame ) ) {
            return true;
        }
        return false;
    }
    
    /**
     * check to see if this item is a repo
     * 
     * @param item
     * @return
     */
    public boolean isRepository(IItem item) {
        
        if( item instanceof IRepositoryFrame ) {
            return true;
        }
        
        if(item.getParentItem() != null){
            isRepository(item.getParentItem());
        }
        
        return false;
    }
    
   

}
