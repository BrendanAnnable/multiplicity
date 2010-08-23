package multiplicity.csysng.items.hotspot;

import multiplicity.csysng.items.ILineItem;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;


public interface IHotLink extends ILineItem {

    public abstract void redrawTargetLocation(Vector2f relativeLocation);

    public abstract void redrawLine(Vector3f[] vertices);

    public abstract IHotSpotItem getHotSpotItem();

    public abstract void initializeGeometry();


}