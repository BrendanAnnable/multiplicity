package multiplicity.csysng.items.hotspot;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;

import com.jme.math.Vector2f;

public interface IHotSpotItem extends IItem {

    public void setOpen(boolean isOpen);

    public boolean isOpen();

    public ILineItem createHotLink();

    public void update(Vector2f vector2f);

}
