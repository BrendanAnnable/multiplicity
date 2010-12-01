package multiplicity.appgallery.stitcher.listeners;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

public class RepositoryBehavior extends MultiTouchEventAdapter implements
        IBehaviour {

    private IRepositoryFrame reposFrame;

    @Override
    public void removeItemActingOn() {
        if(reposFrame != null) {
            reposFrame.getMultiTouchDispatcher().remove(this);
        }
        this.reposFrame = null;
    }

    @Override
    public void setItemActingOn(IItem item) {
        if(item instanceof IRepositoryFrame) {
            this.reposFrame = (IRepositoryFrame) item;
            reposFrame.getMultiTouchDispatcher().addListener(this);
        }else{
            //TODO: log severe
        }
    }

    @Override
    public void cursorPressed(MultiTouchCursorEvent event) {
        super.cursorPressed(event);
        
        if( reposFrame.tap() == 1 ) {
            if( reposFrame.isOpen() ) {
                reposFrame.close();
                reposFrame.resetTaps();
            } else {
                reposFrame.open();
                reposFrame.resetTaps();
            }
        }
        
//        StitcherUtils.updateAllHotLinkConnections();
        
    }
    
}
