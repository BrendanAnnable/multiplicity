package multiplicity.csysngjme.items.hotspots;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;

public class HotSpotItemImage extends JMEImage implements IHotSpotItem {

    private static final long serialVersionUID = 3685342474539036654L;
    private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

    private String type;
    private URL imageResource;
    
    public int taps = 0;
    
    public HotSpotItemImage(String name, UUID uuid, URL imageResource) {
        super(name, uuid);
        this.imageResource = imageResource;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
       setImage(imageResource, 1f);
       setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
        
    }

    @Override
    public void setOpen(boolean isOpen) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IHotLink createHotLink() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Vector2f vector2f) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IHotSpotFrame getHotSpotFrameContent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void redrawHotlink(IItem item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void redrawHotlink() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateHotSpot() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void tap() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetTaps() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeHotLink(IHotLink hotLink) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IHotLink getHotLink() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getTapCount() {
        // TODO Auto-generated method stub
        return 0;
    }

}
