package multiplicity.appgallery.stitcher.listeners;

import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class HotSpotTextListener extends MultiTouchEventAdapter {

    private IHotSpotText hotSpotText;

    public HotSpotTextListener(IHotSpotText hotSpotText) {
        this.hotSpotText = hotSpotText;
        this.hotSpotText.getMultiTouchDispatcher().addListener(this);
    }

    @Override
    public void cursorChanged(MultiTouchCursorEvent event) {
        super.cursorChanged(event);
        HotSpotUtils.updateHotSpots(hotSpotText);
    }
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        
        
        if (hotSpotText.tap() > 4) {

            if (hotSpotText.isKeyboardVisible() == false) {
                StitcherUtils.showKeyboard(hotSpotText);
            } else {
                StitcherUtils.hideKeyboard(hotSpotText);
            }
            
            hotSpotText.resetTaps();

        }
    }

}
