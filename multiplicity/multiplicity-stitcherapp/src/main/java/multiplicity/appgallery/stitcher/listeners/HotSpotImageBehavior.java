package multiplicity.appgallery.stitcher.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;

import multiplicity.appgallery.stitcher.IStitcherContants;
import multiplicity.appgallery.stitcher.StitcherApp;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IImageRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.jmeutils.UnitConversion;

public class HotSpotImageBehavior extends MultiTouchEventAdapter implements IBehaviour {
    
    private final static Logger logger = Logger.getLogger(ImageItemListener.class.getName());

    private ArrayList<HotSpotFrame> highlightedFrames = new ArrayList<HotSpotFrame>();

    private IImage mainImage;
    
    @Override
    public void cursorReleased(MultiTouchCursorEvent event) {
        super.cursorReleased(event);
        logger.debug("Image cursor released event");

        
        Vector2f screenPos = new Vector2f();
        ContentSystem.getContentSystem().getDisplayManager().tableToScreen(event.getPosition(), screenPos);
        List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(screenPos);
        logger.debug("items found on drop: " + findItemsOnTableAtPosition.toString());
        
        if( findItemsOnTableAtPosition.isEmpty())
            mainImage.centerItem();
        
        if( mainImage.getParentItem() != null ) {
            
            
            if( mainImage.getParentItem() instanceof IHotSpotFrame ) {
                logger.debug("image hotspot frame released on itself");
                IHotSpotFrame hotspotFrame = (IHotSpotFrame) mainImage.getParentItem();
               
              
                hotspotFrame.bringHotSpotsToTop();
               
                
              
        
               
                
                StitcherUtils.bringAllHotLinksToTheTop(hotspotFrame);
                StitcherUtils.bringAllHotSpotFramesToTheTop();
                hotspotFrame.bringPaletToTop();
                return;
            }
            //check to see if it was released on the parent.
            if ( mainImage.getParentItem() instanceof IRepositoryFrame) {
                IRepositoryFrame repositoryFrame = (IRepositoryFrame) mainImage.getParentItem();
                for (IItem foundItem : findItemsOnTableAtPosition) {
                     
                    if( isHotspotFrameText(foundItem))
                        return;
                    
                    if( isHotspotFrame(foundItem)) {
                        if( mainImage.getParentItem() instanceof IImageRepositoryFrame ) {
                            logger.debug("image released on hotspot");
                            doBackgroundFrameDrop(mainImage, (IHotSpotFrame) foundItem);
                        } else if(mainImage.getParentItem() instanceof IHotSpotRepo) {
                            logger.debug("coming from a repo not cool");

                          IHotSpotFrame hsFrame = (IHotSpotFrame)foundItem;
                          IFrame frame = (IFrame) mainImage.getParentItem();
                          frame.removeItem(mainImage);

                          Vector2f itemWorldPos = mainImage.getWorldLocation();
                          mainImage.setRelativeScale(IStitcherContants.INITIAL_DROP_SCALE);
                          hsFrame.addItem(mainImage);
                          mainImage.setWorldLocation(itemWorldPos);
                          hsFrame.getZOrderManager().bringToTop(mainImage, null);

//                            stitcher.bumpHotSpotConnections();
                          hsFrame.bringHotSpotsToTop();
                          hsFrame.bringPaletToTop();
                        }
                        return;
                    } else if( isRepository(foundItem)) {
                        logger.debug("image released on its parent");
                        return;
                    }// if
                }// for
                
              if( repositoryFrame instanceof IBackgroundRepositoryFrame) {
                  logger.debug("creating background frame....");
                    //mainImage.getParentItem().removeItem(mainImage);
                  IImage copyImage = copyImage(mainImage);
                    StitcherUtils.createBackgroundFrame(copyImage);
                           
//                    copyImage(mainImage, repositoryFrame);
                    mainImage.centerItem();
                    repositoryFrame.close();
              }
              
              mainImage.centerItem();
                return;
                
//                if( mainImage.getParentItem() instanceof IBackgroundRepositoryFrame) {
//                    mainImage.getParentItem().removeItem(mainImage);
//                   stitcherApp.createNewFrame(mainImage, new Vector2f(0.0f,
//                           300.0f), IStitcherContants.BACKGROUND + "-" + mainImage.getUUID(),
//                           IStitcherContants.BACKGROUND);
//                   return;
//                }
            }
        }
    }
                    
//                    if( isHotspotFrame(foundItem)) {
//                        if( mainImage.getParentItem() instanceof IImageRepositoryFrame ) {
//                            logger.debug("image released on hotspot");
//                            doBackgroundFrameDrop(mainImage, (IHotSpotFrame) foundItem);
//                        }
//                        return;
//                    } else if( isRepository(foundItem)) {
//                        logger.debug("image released on its parent");
//                        return;
//                    }
//                }// for
//            }// if
        
            
//            if( mainImage.getParentItem() instanceof IBackgroundRepositoryFrame) {
//                //is a background
//                logger.debug("background released on main surface");
//           
//                
//                if (mainImage instanceof IImage) {
//                    IRepositoryFrame backgroundFrameRepository = (IRepositoryFrame) mainImage.getParentItem();
//                    mainImage.getParentItem().removeItem(mainImage);
//                   
//                   
//                    // create copy
//                    
//                    
//                    
////                    
//                    IImage copyImage = copyImage((IImage) mainImage, backgroundFrameRepository);
//                    Vector2f position = StitcherUtils.generateRandomPosition((JMEFrame) backgroundFrameRepository, copyImage);
//                    copyImage.setRelativeLocation(position);
//
//                    
//                    
//                    stitcherApp.createNewFrame(mainImage, new Vector2f(0.0f,
//                            300.0f), IStitcherContants.BACKGROUND + "-" + mainImage.getUUID(),
//                            IStitcherContants.BACKGROUND);
//
//
//                    backgroundFrameRepository.close();
//                    backgroundFrameRepository.addItem(copyImage);
//
//                    return;
//                }
              
//            }
    
    private IImage copyImage(IImage imageItem) {
        IImage copy = StitcherUtils.createPhotoImage(imageItem
                .getImageUrl());

//        float scale = (Float) getScale(((JMERectangularItem) copy)
//                .getSize());
        copy.setRelativeScale(imageItem.getRelativeScale());
        BehaviourMaker.addBehaviour(copy, HotSpotImageBehavior.class);
        return copy;

    }
    private void doBackgroundFrameDrop(IItem releasedItem, IItem frameDroppedOn) {
        if( releasedItem.getParentItem() instanceof IImageRepositoryFrame ) {
            logger.debug("doing a background frame drop");
            IHotSpotFrame hotspotFrame = (IHotSpotFrame)frameDroppedOn;
            IRepositoryFrame repository = (IRepositoryFrame) releasedItem.getParentItem();

            if( hotspotFrame.isLocked() == false ) {
                
                if( hotspotFrame.getName().contains(IStitcherContants.HOTSPOT_FRAME_NAME_IMAGE)) {
                    //we are dropping on a  hotspot content frame
//                    repository.removeItem(releasedItem);
                 

                    IImage copyImage = copyImage((IImage) releasedItem);
                    hotspotFrame.addItem(copyImage);
                    copyImage.setRelativeScale(1.0f);
                    ((JMERectangularItem) copyImage).setSize(IStitcherContants.HOTSPOT_FRAME_DIMENSION, IStitcherContants.HOTSPOT_FRAME_DIMENSION);
                    

                    hotspotFrame.setLocked(true);
                    hotspotFrame.toggleLock();
                    hotspotFrame.getPalet().lockPalet(hotspotFrame.isLocked());

                    hotspotFrame.bringPaletToTop();
                    
                } else {
                    // on a background
                    IImage copyImage = copyImage((IImage) releasedItem);

                    
//                    repository.removeItem(copyImage);
                    Vector2f itemWorldPos = releasedItem.getWorldLocation();
                    hotspotFrame.addItem(copyImage);
//                    copyImage.centerItem();
                    copyImage.setWorldLocation(itemWorldPos);
                    hotspotFrame.getZOrderManager().bringToTop(copyImage, null);
                    hotspotFrame.bringHotSpotsToTop();
                    hotspotFrame.bringPaletToTop();
    
                    
                
                }
                releasedItem.centerItem();
                
//                repository.addItem(copyImage);
                repository.close();
            } else {
                //take it back
                releasedItem.centerItem();
            }
        }// if
    }
    
    public boolean isHotspotFrame(IItem item) {
        if( item != null && ( item instanceof IHotSpotFrame ) ) {
            return true;
        }
        return false;
    }
    
    public boolean isHotspotFrameText(IItem item) {
        if( item != null && ( item instanceof IHotSpotText ) ) {
            return true;
        }
        return false;
    }
    
    
    public boolean isRepository(IItem item) {
        
        if( item instanceof IRepositoryFrame ) {
            return true;
        }
        
        if(item.getParentItem() != null){
            isRepository(item.getParentItem());
        }
        
        return false;
    }
    
    @Override
    public void removeItemActingOn() {
        if(mainImage != null) {
            mainImage.getMultiTouchDispatcher().remove(this);
        }
        this.mainImage = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IImage) {
            this.mainImage = (IImage) item;
            this.mainImage.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }

}
