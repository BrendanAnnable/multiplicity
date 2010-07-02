package multiplicity.csysng.items.hotspot;

import java.util.ArrayList;

import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;

public interface IHotSpotFrame extends IFrame {

    public void addHotLink(IHotLink hotLink);

    public void removeHotLink(IHotLink hotLink);

    public void setHotLinks(ArrayList<IHotLink> hotLinks);

    public ArrayList<IHotLink> getHotLinks();

    public ArrayList<IHotSpotItem> getHotSpots();

    public void setHotSpots(ArrayList<IHotSpotItem> hotSpots);

    public void addHotSpot(IItem item);

    public void bringHotSpotsToTop();
 
    public void bringPaletToTop();

    public void setVisible(boolean b);
    
    public boolean isVisable();

	public void setLocked(boolean isLocked);

	public boolean isLocked();

	public void toggleLock();
}
