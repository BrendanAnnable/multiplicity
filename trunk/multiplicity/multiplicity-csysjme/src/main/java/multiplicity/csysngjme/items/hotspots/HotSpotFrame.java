package multiplicity.csysngjme.items.hotspots;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
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
	public List<ILineItem> hotLinks = new CopyOnWriteArrayList<ILineItem>();
	protected List<Line> lines = new CopyOnWriteArrayList<Line>();
	protected boolean isLocked = false;
	private JMEColourRectangle frameOverlay;
	private float oldRotation = 0f;

    private boolean isVisable; ;

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
		
		frameOverlay.addItemListener(new IItemListener() {
			
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
			}
			
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				
			}
			
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
			}
			
			@Override
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
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
		}
	}

	public void addHotLink(ILineItem hotLink) {
	    this.hotLinks.add(hotLink);
	}
	
	public void removeHotLink(IHotLink hotLink) {
	    if( !hotLinks.isEmpty() && hotLinks.contains(hotLink)) {
	        hotLinks.remove(hotLink);
	    }
	        
	}
	
	@Override
    public void setHotLinks(List<ILineItem> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public List<ILineItem> getHotLinks() {
        return hotLinks;
    }

	@Override
	public void bringPaletToTop() {
		// TODO Auto-generated method stub
		IPalet palet = (IPalet) this.getChild("palet");
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
		
		this.setLocked(!isLocked);
	}

	@Override
	public void sendOverlaytoBottom() {
		this.getZOrderManager().sendToBottom(frameOverlay, null);
	}

	public JMEColourRectangle getFrameOverlay() {
		return frameOverlay;
	}
}
