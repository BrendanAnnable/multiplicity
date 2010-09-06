package multiplicity.csysngjme.items.hotspots.listeners;

import java.util.ArrayList;
import java.util.List;

import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;


public class HotSpotUtils {
    
    public static List apps = new ArrayList();
    
    public static void updateHotSpots(IHotSpotFrame frame) {
        List<IHotLink> hotLinks = frame.getHotLinks();
            for (IHotLink iHotLink : hotLinks) {
                iHotLink.getHotSpotItem().updateHotSpot();
                
            }
    }
}