package multiplicity.csysngjme.items.hotspots;

import java.util.ArrayList;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEFrame;

public class HotSpotFrame extends JMEFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	
	protected ArrayList<IHotSpotItem> hotSpots = new ArrayList<IHotSpotItem>(); 

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	public ArrayList<IHotSpotItem> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(ArrayList<IHotSpotItem> hotSpots) {
		this.hotSpots = hotSpots;
	}

	public int addHotSpot(IItem item) {
		hotSpots.add((IHotSpotItem) item);

		return hotSpots.size();
	}

	public void bringHotSpotsToTop() {
		for (IHotSpotItem iHotSpotItem : hotSpots) {
			this.getZOrderManager().bringToTop(iHotSpotItem, null);   
		}
	}
}
