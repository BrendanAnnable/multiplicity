package multiplicity.csysngjme.items.hotspots;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;

public class HotSpotFrame extends JMEFrame implements IHotSpotFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	private final static Logger logger = Logger.getLogger(HotSpotFrame.class.getName());

	public List<IHotSpotItem> hotSpots = new CopyOnWriteArrayList<IHotSpotItem>(); 
	public List<IHotLink> hotLinks = new CopyOnWriteArrayList<IHotLink>();
	protected List<Line> lines = new CopyOnWriteArrayList<Line>();
	protected boolean isLocked = false;
	private JMEColourRectangle frameOverlay;
//	private float oldRotation = 0f;

    private boolean isVisable;

    private IPalet palet; ;

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	public void addTouchOverlay() {
		int width = (int) this.getSize().x;
		int height = (int) this.getSize().y;
		frameOverlay = new JMEColourRectangle("frameOverlay",UUID.randomUUID(), width , height);
		frameOverlay.initializeGeometry();
		frameOverlay.setSolidBackgroundColour(new ColorRGBA(0f, 0f, 0f, 0.2f));
		this.addItem(frameOverlay);
		this.getZOrderManager().sendToBottom(frameOverlay, null);
		BehaviourMaker.addBehaviour(frameOverlay, RotateTranslateScaleBehaviour.class);
		
		frameOverlay.addItemListener(new ItemListenerAdapter() {
			
			@Override
			public void itemZOrderChanged(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemScaled(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemRotated(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemMoved(IItem item) {
			    super.itemMoved(item); 
			    logger.debug("overlay: moved");
			    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                
                
			    parentFrame.sendHotLinksToTop();
                parentFrame.bringHotSpotsToTop();
                parentFrame.bringPaletToTop();
			}
			
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
			    super.itemCursorReleased(item, event);
			    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
             
                
			    parentFrame.sendHotLinksToTop();
                parentFrame.bringHotSpotsToTop();
                parentFrame.bringPaletToTop();
			}
			
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
			    super.itemCursorPressed(item, event);
			    logger.debug("overlay: pressed");
	            IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
			    
             
	            parentFrame.sendHotLinksToTop();
                parentFrame.bringHotSpotsToTop();
                parentFrame.bringPaletToTop();
			}
			
			@Override
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
			    super.itemCursorClicked(item, event);
			    logger.debug("overlay: clicked");
			    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                
			    parentFrame.sendHotLinksToTop();
                parentFrame.bringHotSpotsToTop();
                parentFrame.bringPaletToTop();
				
			}
			
			@Override
			public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
                super.itemCursorChanged(item, event);
				IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
				Vector2f frameOriginLocation = parentFrame.getRelativeLocation();
				Vector2f itemDisplacement = item.getRelativeLocation();
				parentFrame.setRelativeLocation(new Vector2f(frameOriginLocation.x + itemDisplacement.x, frameOriginLocation.y + itemDisplacement.y));
				item.setRelativeLocation(new Vector2f(0f, 0f));
				
				float itemScale = item.getRelativeScale();
				if(itemScale < 2.5f && itemScale>0.2f)  {
					parentFrame.setRelativeScale(itemScale);
				}
				
				float relativeRotation = item.getRelativeRotation();
				parentFrame.setRelativeRotation(relativeRotation);
				
				parentFrame.sendHotLinksToTop();
                parentFrame.bringHotSpotsToTop();
				parentFrame.bringPaletToTop();

				
			}
		});
	}

	public List<IHotSpotItem> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(List<IHotSpotItem> hotSpots) {
		this.hotSpots = hotSpots;
	}

	public void addHotSpot(IItem item) {
		hotSpots.add((IHotSpotItem) item);
	}

	public void bringHotSpotsToTop() {
		for (IHotSpotItem iHotSpotItem : hotSpots) {
			this.getZOrderManager().bringToTop(iHotSpotItem, null);  
			iHotSpotItem.redrawHotlink(iHotSpotItem);
		}
	}

	public void addHotLink(IHotLink hotLink) {
	    this.hotLinks.add(hotLink);
	    hotLink.addItemListener(new ItemListenerAdapter(){
            
            @Override
            public void itemCursorPressed(IItem item,
                    MultiTouchCursorEvent event) {
                super.itemCursorPressed(item, event);
                logger.debug("hotlink - pressed");
//                sendHotLinksToBottom();
                bringPaletToTop();
            }
            
            @Override
            public void itemCursorChanged(IItem item,
                    MultiTouchCursorEvent event) {
                // TODO Auto-generated method stub
                super.itemCursorChanged(item, event);
                logger.debug("hotlink - changed");
//               sendHotLinksToBottom();
                bringPaletToTop();
            }
            
            @Override
            public void itemCursorClicked(IItem item,
                    MultiTouchCursorEvent event) {
                // TODO Auto-generated method stub
                super.itemCursorClicked(item, event);
                logger.debug("hotlink - clicked");
//                sendHotLinksToBottom();
                bringPaletToTop();
            }
            
            
        });
	}
	
	public void removeHotLink(IHotLink hotLink) {
	    if( !hotLinks.isEmpty() && hotLinks.contains(hotLink)) {
	        hotLinks.remove(hotLink);
	    }
	        
	}
	
	@Override
    public void setHotLinks(List<IHotLink> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public List<IHotLink> getHotLinks() {
        return hotLinks;
    }

	@Override
	public void bringPaletToTop() {
		// TODO Auto-generated method stub
		if(isLocked() == false ) {
		    logger.debug("palet to the top; is not locked");
		    sendOverlayToBottom();
		    
		} else  {
		    logger.debug("palet to the top; is locked");
		    sendOverlayToTop();
		}
		
//		  for (IHotLink hl : hotLinks) {
//	            hl.getZOrderManager().setItemZOrder(palet.getZOrderManager().getItemZOrder()-1);
//	       }
		this.getZOrderManager().bringToTop(palet, null);  
		
	}

    @Override
    public void setVisible(boolean isVisable) {
        this.isVisable = isVisable;
        
        if( isVisable ) {
            this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_ORTHO);
            this.getMaskGeometry().setRenderQueueMode(Renderer.QUEUE_ORTHO);
        } else {
            this.getMaskGeometry().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
            this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

        }
    }

    @Override
    public boolean isVisable() {
        return this.isVisable;
    }

	@Override
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
		this.toggleLock();
	}

	@Override
	public void toggleLock() {
		if(isLocked) {
			//our frame is in lock mode, let's unlock it
			this.getZOrderManager().sendToBottom(frameOverlay, null);
			border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
		}
		else {
			//our frame is in unlock mode, let's lock it
			this.getZOrderManager().bringToTop(frameOverlay, null);
			border.setColor(new ColorRGBA(0f, 0f, 0f, 0f));
		}
	}

	@Override
	public void sendOverlayToBottom() {
		this.getZOrderManager().sendToBottom(frameOverlay, null);
	}
	
	@Override
	public void sendOverlayToTop() {
	    this.getZOrderManager().bringToTop(frameOverlay, null);
	}

	public JMEColourRectangle getFrameOverlay() {
		return frameOverlay;
	}
	
	@Override
    public IPalet getPalet() {
	    return palet;
	}

    @Override
    public void addPalet(IPalet palet) {
        this.palet = palet;
        this.addItem(palet);
    }
    
    @Override
    public void sendHotLinksToBottom() {
        
        for (IHotLink hl : hotLinks) {
            this.getZOrderManager().sendToBottom(hl, null);
        }
    }
    
    @Override
    public void sendHotLinksToTop() {
        for (IHotLink hl : hotLinks) {
            this.getZOrderManager().bringToTop(hl, null);
        }
    }
    
}
