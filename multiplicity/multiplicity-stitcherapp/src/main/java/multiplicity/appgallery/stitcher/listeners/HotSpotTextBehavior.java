package multiplicity.appgallery.stitcher.listeners;

import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class HotSpotTextBehavior extends MultiTouchEventAdapter implements IBehaviour {

    private IHotSpotText hotSpotText;
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        
        
        if (hotSpotText.tap() > 3) {

            if (hotSpotText.isKeyboardVisible() == false) {
                StitcherUtils.showKeyboard(hotSpotText);
            } else {
                StitcherUtils.hideKeyboard(hotSpotText);
            }
            
            hotSpotText.resetTaps();

        }
    }

    @Override
    public void removeItemActingOn() {
        if(hotSpotText != null) {
            hotSpotText.getKeyboardImage().getMultiTouchDispatcher().remove(this);
        }
        this.hotSpotText = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IHotSpotText) {
            this.hotSpotText = (IHotSpotText) item;
            hotSpotText.getKeyboardImage().getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }
}
