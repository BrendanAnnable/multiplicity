package multiplicity.csysng.items.hotspot;

import java.util.List;

import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;

public interface IHotSpotFrame extends IFrame {

    public void addHotLink(ILineItem hotLink);

    public void removeHotLink(IHotLink hotLink);

    public void setHotLinks(List<ILineItem> hotLinks);

    public List<ILineItem> getHotLinks();

    public List<IHotSpotItem> getHotSpots();

    public void setHotSpots(List<IHotSpotItem> hotSpots);

    public void addHotSpot(IItem item);

    public void bringHotSpotsToTop();
 
    public void bringPaletToTop();

    public void setVisible(boolean b);
    
    public boolean isVisable();

	public void setLocked(boolean isLocked);

	public boolean isLocked();

	public void toggleLock();

	public void sendOverlaytoBottom();
}
