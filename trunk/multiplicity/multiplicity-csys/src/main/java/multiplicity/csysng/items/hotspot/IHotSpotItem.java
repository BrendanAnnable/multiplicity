package multiplicity.csysng.items.hotspot;

import java.awt.Color;

import multiplicity.csysng.items.IItem;

public interface IHotSpotItem extends IItem {

    public void setOpen(boolean isOpen);

    public boolean isOpen();

    public IHotSpotItem getRelationHotSpot();

    public void setSolidBackgroundColour(Color color);

	public void createCallBackHotSpotFrame();

    public void createLink();

}
