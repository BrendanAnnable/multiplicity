package multiplicity.csysng.items.hotspot;

import multiplicity.csysng.items.IItem;

import com.jme.math.Vector2f;

public interface IHotSpotItem extends IItem {

    public void setOpen(boolean isOpen);

    public boolean isOpen();

    public IHotLink createHotLink();

    public void update(Vector2f vector2f);

    public  IHotSpotFrame getHotSpotFrameContent();

    public  void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent);

    public  void initializeGeometry();

    public void redrawHotlink(IItem item);
    
    public void setType(String type);
    
    public String getType();

    public void redrawHotlink();

    public void updateHotSpot();

    public int tap();

    public void resetTaps();

    public void toggle();

}
