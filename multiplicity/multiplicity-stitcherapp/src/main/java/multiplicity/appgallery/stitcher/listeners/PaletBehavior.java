package multiplicity.appgallery.stitcher.listeners;

import java.util.List;

import multiplicity.appgallery.stitcher.StitcherApp;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.jmeutils.UnitConversion;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;

public class PaletBehavior extends MultiTouchEventAdapter implements IBehaviour {

    private final static Logger logger = Logger.getLogger(PaletBehavior.class.getName());
    private IPalet palet;

    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        paletPressed(palet);
        IHotSpotFrame hotspotFrame = (IHotSpotFrame) palet.getParentItem();
        List<IHotSpotItem> hotSpots = hotspotFrame.getHotSpots();
        for (IHotSpotItem iHotSpotItem : hotSpots) {
            
            if( iHotSpotItem.getHotSpotFrameContent() != null )
                StitcherUtils.bringToTop(iHotSpotItem.getHotSpotFrameContent());
                
        }
    }
    
    @Override
    public void cursorReleased(MultiTouchCursorEvent event) {
        super.cursorReleased(event);
        paletReleased(palet, event);
    }
    
    protected void paletReleased(IPalet palet, MultiTouchCursorEvent event) {
        String message = "Palet released: ";
        logger.debug(message);
        boolean offParent = true;
        
        Vector2f locStore = new Vector2f();
        UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

        List<IItem> items = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);

        IHotSpotFrame paletParent = (IHotSpotFrame) palet.getParentItem();

        for (IItem foundItem : items) {
            if (foundItem.getParentItem() != null && foundItem.getParentItem().equals(palet.getParentItem())) {
                offParent = false;
                logger.debug("Palet released on its parent");
            }
        }

        if (offParent) {
            palet.centerItem();
            logger.debug("Palet in the mist, come back to center");
        }

        paletParent.bringPaletToTop();
    }
    protected void paletPressed(IPalet palet) {
        logger.info("palet pressed");
        int taps = palet.tap();
        logger.info("palet clicked " + taps);
        if (taps > 1) {
            
            palet.resetTaps();
            
            IHotSpotFrame parentFrame = (IHotSpotFrame) palet.getParentItem();
            if( parentFrame.isLocked() ) {
                logger.info("unlocking palet");
                parentFrame.setLocked(false);
                palet.lockPalet(false);
            } else {
                logger.info("locking palet");
                parentFrame.setLocked(true);
                palet.lockPalet(true);
            }
            parentFrame.bringPaletToTop();
        }
    }

    @Override
    public void removeItemActingOn() {
        if(palet != null) {
            palet.getMultiTouchDispatcher().remove(this);
        }
        this.palet = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IPalet) {
            this.palet = (IPalet) item;
            this.palet.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }
}
