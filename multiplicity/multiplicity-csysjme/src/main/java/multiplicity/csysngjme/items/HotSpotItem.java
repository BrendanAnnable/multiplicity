package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.items.hotspot.IHotSpotItem;

public class HotSpotItem extends JMEColourRectangle implements IHotSpotItem {

    boolean isOpen;
    protected String link;
    IHotSpotItem relationHotSpot;
    
    
    public HotSpotItem(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
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
    public IHotSpotItem getRelationHotSpot() {
        return relationHotSpot;
    }

    @Override
    public void createLink(IHotSpotItem relationHotSpot) {
        this.relationHotSpot = relationHotSpot;
    }

}
