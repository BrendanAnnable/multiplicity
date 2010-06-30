package multiplicity.csysngjme.items.hotspots;

import java.util.ArrayList;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEFrame;

import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial;

public class HotSpotFrame extends JMEFrame implements IHotSpotFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	
	public ArrayList<IHotSpotItem> hotSpots = new ArrayList<IHotSpotItem>(); 
	public ArrayList<IHotLink> hotLinks = new ArrayList<IHotLink>();
	protected ArrayList<Line> lines = new ArrayList<Line>();

    private boolean isVisable; ;

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	public ArrayList<IHotSpotItem> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(ArrayList<IHotSpotItem> hotSpots) {
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

	public void addHotLink(IHotLink hotLink) {
	    this.hotLinks.add(hotLink);
	}
	
	public void removeHotLink(IHotLink hotLink) {
	    if( !hotLinks.isEmpty() && hotLinks.contains(hotLink)) {
	        hotLinks.remove(hotLink);
	    }
	        
	}
	
    public void setHotLinks(ArrayList<IHotLink> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public ArrayList<IHotLink> getHotLinks() {
        return hotLinks;
    }

	@Override
	public void bringPaletToTop() {
		// TODO Auto-generated method stub
		IPalet palet = (IPalet) this.getChild("palet");
		this.getZOrderManager().bringToTop(palet, null);  
	}

    @Override
    public void setVisable(boolean isVisable) {
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
}
