package multiplicity.csysngjme.items;

import java.util.ArrayList;
import java.util.UUID;

import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IItem;

public class HotSpotFrame extends JMEFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	
	protected ArrayList<IColourRectangle> hotSpots = new ArrayList<IColourRectangle>(); 

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	public ArrayList<IColourRectangle> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(ArrayList<IColourRectangle> hotSpots) {
		this.hotSpots = hotSpots;
	}

	public void addHotSpot(IItem item) {
		hotSpots.add((IColourRectangle) item);
	}
}
