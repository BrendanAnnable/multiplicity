package multiplicity.appgallery.stitcher.listeners;

import multiplicity.appgallery.stitcher.StitcherApp;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class HotSpotTextListener extends MultiTouchEventAdapter {

    
    private StitcherApp stitcherApp;
    private IHotSpotText hotSpotText;

    public HotSpotTextListener(IHotSpotText hotSpotText, StitcherApp stitcherApp) {
        this.hotSpotText = hotSpotText;
        this.stitcherApp = stitcherApp;
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
                showKeyboard(hotSpotText);
            } else {
                hideKeyboard(hotSpotText);
            }
            
            hotSpotText.resetTaps();

        }
    }

           private void showKeyboard(IHotSpotText hotSpotText) {
              
               IFrame keyboard = hotSpotText.getKeyboard();
               stitcherApp.add(keyboard);
               BehaviourMaker.addBehaviour(keyboard, RotateTranslateScaleBehaviour.class);
               stitcherApp.getZOrderManager().bringToTop(keyboard, null);
               hotSpotText.setKeyboardVisible(true);
               
           }
           
           private void hideKeyboard(IHotSpotText hotSpotText) {
               IFrame keyboard = hotSpotText.getKeyboard();
               stitcherApp.remove(keyboard);
               hotSpotText.setKeyboardVisible(false);
           }
}
