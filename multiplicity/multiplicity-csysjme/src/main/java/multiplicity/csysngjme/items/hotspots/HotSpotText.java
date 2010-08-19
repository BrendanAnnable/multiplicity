package multiplicity.csysngjme.items.hotspots;

import java.awt.Color;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEEditableText;

public class HotSpotText extends JMEEditableText implements IHotSpotText {

    public List<ILineItem> hotLinks = new CopyOnWriteArrayList<ILineItem>();
    public List<IHotSpotItem> hotSpots = new CopyOnWriteArrayList<IHotSpotItem>(); 

    
    public HotSpotText(String name, UUID uuid) {
        super(name, uuid);
    }
    
    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
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
            iHotSpotItem.redrawHotlink(iHotSpotItem);
        }
    }

    @Override
    public void bringPaletToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isVisable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setLocked(boolean isLocked) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void toggleLock() {
        // TODO Auto-generated method stub

    }

    @Override
    public IPalet getPalet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendOverlayToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendOverlayToBottom() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPalet(IPalet palet) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendHotLinksToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendHotLinksToBottom() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBorder(IBorder b) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBorder getBorder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasBorder() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSolidBackgroundColour(Color c) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGradientBackground(Gradient g) {
        // TODO Auto-generated method stub

    }

    @Override
    public IItemListener maintainBorderSizeDuringScale() {
        // TODO Auto-generated method stub
        return null;
    }

}
