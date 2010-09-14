package multiplicity.csysngjme.items.hotspots;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMEFrame;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial;

public class HotSpotFrame extends JMEFrame implements IHotSpotFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	private final static Logger logger = Logger.getLogger(HotSpotFrame.class.getName());

	public List<IHotSpotItem> hotSpots = new CopyOnWriteArrayList<IHotSpotItem>(); 
	public List<IHotLink> hotLinks = new CopyOnWriteArrayList<IHotLink>();
	protected List<Line> lines = new CopyOnWriteArrayList<Line>();
	protected boolean isLocked = false;
	protected IColourRectangle frameOverlay;

	protected boolean isVisible;

    protected IPalet palet;
    protected boolean canScale = true;

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	@Override
	public void addFrameOverlay() {
        int width = (int) this.getSize().x;
        int height = (int) this.getSize().y;
        setFrameOverlay(new JMEColourRectangle("frameOverlay",UUID.randomUUID(), width , height));
        getFrameOverlay().initializeGeometry();
        getFrameOverlay().setSolidBackgroundColour(new ColorRGBA(1f, 0f, 0f, 0.2f));
        getFrameOverlay().getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
        this.addItem(getFrameOverlay());
        this.getZOrderManager().bringToTop(getFrameOverlay(), null);
    }

    @Override
   public void behaviourAdded(IBehaviour behaviour) {
        super.behaviourAdded(behaviour);
        if( getFrameOverlay() != null)
            getFrameOverlay().getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
    }
	
	public void updateOverLay() {
	    this.bringHotSpotsToTop();
	    this.bringPaletToTop();
	}

	public List<IHotSpotItem> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(List<IHotSpotItem> hotSpots) {
		this.hotSpots = hotSpots;
	}

	@Override
	public void addHotSpot(IHotSpotItem hotspotItem) {
		hotSpots.add(hotspotItem);
		addItem(hotspotItem);
	}

	@Override
	public void removeHotSpot(IHotSpotItem hotspotItem) {
	    logger.debug("removing hotspot from hotspotframe");
	    hotSpots.remove(hotspotItem);
	    ((Spatial)hotspotItem).removeFromParent();
//	    removeItem(hotspotItem);
	    
	}
	public void bringHotSpotsToTop() {
        if (!hotSpots.isEmpty()) {
            for (IHotSpotItem iHotSpotItem : hotSpots) {
                this.getZOrderManager().bringToTop(iHotSpotItem, null);
            }
        }
	}

	@Override
	public void addHotLink(IHotLink hotLink) {
	    this.hotLinks.add(hotLink);
	    logger.debug("hotlink added to hotspot frame");
	}
	
	@Override
	public void removeHotLink(IHotLink hotLink) {
	    if( !hotLinks.isEmpty() && hotLinks.contains(hotLink)) {
	        hotLinks.remove(hotLink);
	        logger.debug("hotlink removed from hotspot frame");
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
		    logger.debug("palet to the top; is NOT locked");
		    sendOverlayToBottom();
//		    sendHotLinksToTop();
//		    bringHotSpotsToTop();
		} else  {
		    logger.debug("palet to the top; is locked");
		    sendOverlayToTop();
		    
//		    bringHotSpotsToTop();
//		    sendHotLinksToTop();
		}
		
//		  for (IHotLink hl : hotLinks) {
//	            hl.getZOrderManager().setItemZOrder(palet.getZOrderManager().getItemZOrder()-1);
//	       }
		this.getZOrderManager().bringToTop(palet, null);  
		
	}

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        
        if( isVisible ) {
            this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_ORTHO);
            this.getMaskGeometry().setRenderQueueMode(Renderer.QUEUE_ORTHO);
        } else {
            this.getMaskGeometry().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
            this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

        }
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
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
			this.sendOverlayToBottom();
			border.setColor(new ColorRGBA(1f, 1f, 1f, 0.6f));
		}
		else {
			//our frame is in unlock mode, let's lock it
			this.sendOverlayToTop();
			border.setColor(new ColorRGBA(0f, 0f, 0f, 0f));
		}
	}

	@Override
	public void sendOverlayToBottom() {
		this.getZOrderManager().sendToBottom(getFrameOverlay(), null);
	}
	
	@Override
	public void sendOverlayToTop() {
	    this.getZOrderManager().bringToTop(getFrameOverlay(), null);
	}

	@Override
	public IColourRectangle getFrameOverlay() {
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
    

    public void setFrameOverlay(JMEColourRectangle frameOverlay) {
        this.frameOverlay = frameOverlay;
    }

    @Override
    public void setScalable(boolean canScale) {
        this.canScale  = canScale;
    }
    
    @Override
    public boolean canScale() {
        return this.canScale;
    }
    
}
