package multiplicity.appgallery.stitcher.listeners;

import org.apache.log4j.Logger;

import multiplicity.appgallery.stitcher.IStitcherContants;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.csysngjme.items.hotspots.listeners.HotSpotUtils;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class HotSpotFrameBehavior extends MultiTouchEventAdapter implements
		IBehaviour {

	private final static Logger logger = Logger.getLogger(HotSpotFrameBehavior.class.getName());

	
    private IHotSpotFrame hotSpotFrame;

    
    @Override
    public void cursorChanged(MultiTouchCursorEvent event) {
    	super.cursorChanged(event);
    	
    	logger.debug("hotspot frame cursorchanged");
    	
    	String type = hotSpotFrame.getType();
    	
    	if( type.equals(IStitcherContants.IMAGE) || type.equals(IStitcherContants.BACKGROUND) ) {
    		   hotSpotFrame.updateOverLay();
    	        
    	}
    	
		   HotSpotUtils.updateHotSpots(hotSpotFrame);

    }
    
    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
    	super.cursorPressed(event);
    	
    	logger.debug("hotspot frame cursorpressed");
    	
    	String type = hotSpotFrame.getType();
    	
    	if( type.equals(IStitcherContants.IMAGE) || type.equals(IStitcherContants.BACKGROUND) ) {
    	      hotSpotFrame.updateOverLay();
    	} 
    	
	      HotSpotUtils.updateHotSpots(hotSpotFrame);

    	
    }
    
	@Override
    public void removeItemActingOn() {
        if(hotSpotFrame != null) {
            hotSpotFrame.getMultiTouchDispatcher().remove(this);
        }
        this.hotSpotFrame = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IHotSpotFrame) {
            this.hotSpotFrame = (IHotSpotFrame) item;
            this.hotSpotFrame.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }
   

}
