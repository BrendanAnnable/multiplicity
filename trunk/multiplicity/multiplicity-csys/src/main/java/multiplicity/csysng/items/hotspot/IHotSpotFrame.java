package multiplicity.csysng.items.hotspot;

import java.util.List;

import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;

public interface IHotSpotFrame extends IFrame {

    public void addHotLink(IHotLink hotLink);

    public void removeHotLink(IHotLink hotLink);

    public void setHotLinks(List<IHotLink> hotLinks);

    public List<IHotLink> getHotLinks();

    public List<IHotSpotItem> getHotSpots();

    public void setHotSpots(List<IHotSpotItem> hotSpots);

    public void addHotSpot(IHotSpotItem item);

    public void bringHotSpotsToTop();
 
    public void bringPaletToTop();

    public void setVisible(boolean b);
    
    public boolean isVisible();

	public void setLocked(boolean isLocked);

	public boolean isLocked();

	public void toggleLock();

    public IPalet getPalet();

    public void sendOverlayToTop();
    public void sendOverlayToBottom();

    public void addPalet(IPalet palet);

    public  void sendHotLinksToTop();

    public  void sendHotLinksToBottom();

    public IColourRectangle getFrameOverlay();

    public void updateOverLay();

    public void addFrameOverlay();

    public void removeHotSpot(IHotSpotItem hotspotItem);

}
