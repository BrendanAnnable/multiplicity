package multiplicity.csysngjme.items.hotspots;

import java.util.ArrayList;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEColourRectangle;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public class HotSpotItem extends JMEColourRectangle implements IHotSpotItem {

	private static final long serialVersionUID = 3685342474539036654L;
	private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

	boolean isOpen;
    protected String link;
    IHotSpotItem relationHotSpot;
    private IHotSpotFrame hotSpotFrameContent;
    private IHotLink hotLink;
    
    
    public HotSpotItem(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
    }

    public HotSpotItem(IHotSpotFrame hotSpotFrameContent, String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
        this.setHotSpotFrameContent(hotSpotFrameContent);
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
    @Override
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }


    @Override
    public boolean isOpen() {
        return isOpen;
    }


    @Override
    public IHotLink createHotLink() {

		//get the location of the 2 hotspots relative to the parent frame
		Vector2f xyHS1 = this.getRelativeLocation();
		Vector2f xyHS2 = hotSpotFrameContent.getRelativeLocation();
		
		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
		vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
		
		hotLink = new HotLink(vertices);
		hotSpotFrameContent.addHotLink(hotLink);
			

		this.addItemListener(new ItemListenerAdapter() {
            
            public void itemMoved(IItem item) {
                
                Vector2f xyHS1 = item.getRelativeLocation();
                ((HotSpotItem)item).getHotLink().redrawSourceLocation(xyHS1);
               
            };
                   
        } );
		
		hotSpotFrameContent.addItemListener(new ItemListenerAdapter() {
            
            public void itemMoved(IItem item) {
                
                IHotSpotFrame frame = ((IHotSpotFrame)item);
                
                ArrayList<IHotLink> hotLinks = frame.getHotLinks();
                for (IHotLink iHotLink : hotLinks) {
                    iHotLink.redrawTargetLocation(item.getRelativeLocation());
                }
            };
        } );
    	
    	return hotLink;
    }

    public void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent) {
        this.hotSpotFrameContent = hotSpotFrameContent;
        this.createHotLink();
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
	public void update(Vector2f frameLocation) {
		// TODO Auto-generated method stub
		
		Vector2f HSLocation = this.getRelativeLocation();

      Vector2f xyHS1 = new Vector2f(frameLocation.x + HSLocation.x, frameLocation.y+HSLocation.y);
      hotLink.redrawSourceLocation(xyHS1);
	}

}
