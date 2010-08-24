package multiplicity.csysng.items.hotspot;

import multiplicity.csysng.items.ILineItem;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;


public interface IHotLink extends ILineItem {

    public void redrawTargetLocation(Vector2f relativeLocation);

    public void redrawLine(Vector3f[] vertices);

    public IHotSpotItem getHotSpotItem();

    public void initializeGeometry();
    
    public void setVisible(boolean visible);

    public IHotSpotFrame getTargetFrame();

    public void setTargetFrame(IHotSpotFrame targetFrame);

    public IHotSpotFrame getSourceFrame();

    public void setSourceFrame(IHotSpotFrame sourceFrame);



}