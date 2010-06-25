package multiplicity.csysng.items.hotspot;

import java.awt.Color;

import com.jme.scene.Line;

import multiplicity.csysng.items.IItem;

public interface IHotSpotItem extends IItem {

    public void setOpen(boolean isOpen);

    public boolean isOpen();

    public void setSolidBackgroundColour(Color color);

    public IHotLink createHotLink();

    public void update();

}
