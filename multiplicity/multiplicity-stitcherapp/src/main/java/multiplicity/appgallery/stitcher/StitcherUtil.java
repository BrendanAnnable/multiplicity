package multiplicity.appgallery.stitcher;

import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class StitcherUtil {

    /**
     * Bad should use iterator pattern
     */
    public static void removeHotSpot(StitcherApp stitcher, IHotSpotItem hotSpotItem) {
        
        IHotSpotFrame hotSpotFrameContent = hotSpotItem.getHotSpotFrameContent();
        
        //first remove the current hotspot
        IHotSpotFrame parentItem = (IHotSpotFrame) hotSpotItem.getParentItem();
     
        //remove the hotlink
        stitcher.removeLineItem(hotSpotItem.getHotLink());
        //remove the content frame
        stitcher.remove(hotSpotFrameContent);
        
        //go for the children
        for (IHotSpotItem hs : hotSpotFrameContent.getHotSpots()) {
            StitcherUtil.removeHotSpot(stitcher, hs);
            
        }

        parentItem.removeHotSpot(hotSpotItem);

        
    }
}
