package multiplicity.csysngjme.items.hotspots;

import java.util.List;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEColourCircle;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class HotSpotItem extends JMEColourCircle implements IHotSpotItem {

	private static final long serialVersionUID = 3685342474539036654L;
	private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

	private boolean isOpen = true;
	private IHotSpotFrame hotSpotFrameContent;
	private IHotSpotFrame sourceFrameContent;

	private IHotLink hotLink;
    private ColorRGBA colorRGBA = new ColorRGBA(1f, 0f, 0f, 1f);
	private String type;

	private int taps = 0;
	public HotSpotItem(String name, UUID uuid, float radius) {
		super(name, uuid, radius);
	}

	public HotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid, radius, colorRGBA);
		this.colorRGBA = colorRGBA;
	}

	@Override
	public String getType() {
        return type;
    }

	@Override
    public void setType(String type) {
        this.type = type;
    }
    
	@Override
	public void initializeGeometry() {
		super.initializeGeometry();
	}

	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen) {

			// setGradientBackground(new Gradient(
			// Color.RED,
			// new Color(0f, 0f, 0f,1f), GradientDirection.VERTICAL));
			//
			// this.setSolidBackgroundColour(Color.RED);
			this.hotLink.setVisible(true);
//			this.hotLink.redrawLine(getLineVertices());
			hotSpotFrameContent.setVisible(true);
		} else {
			this.hotLink.setVisible(false);

			// setGradientBackground(new Gradient(
			// Color.WHITE,
			// new Color(0f, 0f, 0f,1f), GradientDirection.VERTICAL));
			// this.setSolidBackgroundColour(Color.BLACK);
			hotSpotFrameContent.setVisible(false);
		}
	}
	
	@Override
	public void toggle() {
	    if( isOpen ) {
	        setOpen(false);
	    } else {
	        setOpen(true);
	    }
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public void removeHotLink(IHotLink hotLink) {
	    hotSpotFrameContent.removeHotLink(hotLink);
        sourceFrameContent.removeHotLink(hotLink);
	}
	@Override
	public IHotLink createHotLink() {
	    sourceFrameContent = (IHotSpotFrame) this.getParentItem();

		Vector3f[] vertices = getLineVertices();

		UUID uuid = UUID.randomUUID();
		hotLink = new HotLink("line-" + uuid, uuid, vertices, colorRGBA, 4f, this);
		hotLink.initializeGeometry();
		hotSpotFrameContent.addHotLink(hotLink);
		sourceFrameContent.addHotLink(hotLink);


//		hotSpotFrameContent.addItemListener(new ItemListenerAdapter() {
//
//			public void itemMoved(IItem item) {
//
//				IHotSpotFrame frame = ((IHotSpotFrame) item);
////				List<IHotSpotItem> hotSpots2 = frame.getHotSpots();
//				List<IHotLink> iLineItems = frame.getHotLinks();
////
//				IHotSpotItem hsi = null;
//				for (IHotLink iLine : iLineItems) {
//					hsi = iLine.getHotSpotItem();
//					Vector3f[] vertices = ((HotSpotItem) hsi).getLineVertices();
//					iLine.redrawLine(vertices);
//				}
//
//				List<IHotSpotItem> hotSpots = frame.getHotSpots();
//				for (IHotSpotItem iHotSpotItem : hotSpots) {
//					redrawHotlink(iHotSpotItem);
//				}
//
//			};
//		});

		return hotLink;
	}
	
	
	@Override
    public void redrawHotlink(IItem item) {
	    Vector3f[] vertices = getLineVertices();
        ((HotSpotItem) item).getHotLink().redrawLine(vertices);
	}

	   @Override
	    public void redrawHotlink() {
	       redrawHotlink(this);
	    }
	   
	private Vector3f[] getLineVertices() {
		IHotSpotFrame parentF = (IHotSpotFrame) this.getParentItem();
		Vector2f parentFCoord = parentF.getRelativeLocation();
		// get the worldlocation of hotspot
		Vector2f xyHS1 = new Vector2f(this.getRelativeLocation().x + parentFCoord.x, this.getRelativeLocation().y + parentFCoord.y);
		// get the worldlocation of hotspot
		Vector2f xyHS2 = hotSpotFrameContent.getRelativeLocation();

		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
		vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
		return vertices;
	}

	public void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent) {
		this.hotSpotFrameContent = hotSpotFrameContent;
	}

	public IHotSpotFrame getHotSpotFrameContent() {
		return hotSpotFrameContent;
	}

	public void setHotLink(IHotLink hotLink) {
		this.hotLink = hotLink;
	}

	public IHotLink getHotLink() {
		return hotLink;
	}

	
	@Override
    public void updateHotSpot() {
        List<IHotLink> iLineItems = hotSpotFrameContent.getHotLinks();
        
        if( iLineItems != null ) {
            IHotSpotItem hsi = null;
            for (IHotLink iLine : iLineItems) {
                hsi = iLine.getHotSpotItem();
                Vector3f[] vertices = ((HotSpotItem) hsi).getLineVertices();
                iLine.redrawLine(vertices);
            }
        }

        List<IHotSpotItem> hotSpots = hotSpotFrameContent.getHotSpots();
        if( hotSpots != null ) {
            for (IHotSpotItem iHotSpotItem : hotSpots) {
                redrawHotlink(iHotSpotItem);
            }
        }
    }
	
	@Override
	public void update(Vector2f frameLocation) {
//		Vector2f HSLocation = this.getRelativeLocation();
//		Vector2f xyHS1 = new Vector2f(frameLocation.x + HSLocation.x, frameLocation.y + HSLocation.y);
//		hotLink.redrawTargetLocation(xyHS1);
		Vector3f[] vertices = this.getLineVertices();
		this.getHotLink().redrawLine(vertices);
	}

    @Override
    public int tap() {
        return ++taps;
    }

    @Override
    public void resetTaps() {
        taps = 0;
    }

}
