package multiplicity.csysngjme.items.hotspots.listeners;

import java.util.List;

import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;


public class HotSpotUtils {
    
    public static void updateHotSpots(IHotSpotFrame frame) {
        List<IHotSpotItem> hotSpots = frame.getHotSpots();
        for (IHotSpotItem iHotSpotItem : hotSpots) {
            iHotSpotItem.updateHotSpot();
        }
        
        List<IHotLink> hotLinks = frame.getHotLinks();
            for (IHotLink iHotLink : hotLinks) {
                iHotLink.getHotSpotItem().updateHotSpot();
                
            }
    }
    
    public static void updateHotLinkSegments(IHotSpotFrame startFrame) {
        List<IHotSpotItem> hotSpots = startFrame.getHotSpots();
        if( hotSpots != null && hotSpots.isEmpty()) {
            for (IHotSpotItem iHotSpotItem : hotSpots) {
                iHotSpotItem.updateHotSpot();
                //update its children
                if( iHotSpotItem.getHotSpotFrameContent() != null ) {
                    updateHotLinkSegments(iHotSpotItem.getHotSpotFrameContent());
                }
            }
        }
       
        
        
        
    }
    public static void updateAllHotLinkConnections(List<IHotSpotFrame> hotSpotFrames) {
        if( !hotSpotFrames.isEmpty() ) {
            for (IHotSpotFrame iHotSpotFrame : hotSpotFrames) {
                updateHotSpots(iHotSpotFrame);
            }
        }
    }
}