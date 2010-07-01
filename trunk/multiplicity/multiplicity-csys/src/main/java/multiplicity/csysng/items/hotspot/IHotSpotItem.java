package multiplicity.csysng.items.hotspot;

import java.awt.Color;

import multiplicity.csysng.items.IItem;

import com.jme.math.Vector2f;

public interface IHotSpotItem extends IItem {

    public void setOpen(boolean isOpen);

    public boolean isOpen();

    public IHotLink createHotLink();

    public void update(Vector2f vector2f);

}
