package multiplicity.appgallery.stitcher.listeners;

import java.awt.event.KeyEvent;

import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardListener;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class HotSpotTextBehavior extends MultiTouchEventAdapter implements IBehaviour {

    private IHotSpotText hotSpotText;
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        
        
        if (hotSpotText.tap() > 1) {

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
            hotSpotText.getKeyBoardBehavior().addListener(new NorwegianKeyboardListener(null, null) {
                
                @Override
                public void keyPressed(KeyboardKey k, boolean shiftDown,
                        boolean altDown, boolean ctlDown) {
                    if(k.getKeyCode() == KeyEvent.VK_CANCEL) {
                        StitcherUtils.hideKeyboard(hotSpotText);
                    }
                }
            });
        }else{
            //TODO: log severe
        }
    }
}
