package multiplicity.csysngjme.zordering;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.zorder.IZOrderManager;
import multiplicity.input.events.MultiTouchCursorEvent;

public class SimpleZOrderManager implements IZOrderManager {
	
	protected int zOrder = 0;
	protected IItem item;
	
	public SimpleZOrderManager(IItem item) {
		this.item = item;
	}

	@Override
	public void bringToTop(IItem itemPressed, MultiTouchCursorEvent event) {
	}
	
	@Override
	public void sendToBottom(IItem item, MultiTouchCursorEvent event) {
	}	

	@Override
	public void childZSpaceRequirementChanged(IItem itemBeingManaged, IZOrderManager defaultZOrderManager) {
	}

	@Override
	public int getItemZOrder() {
		return zOrder;
	}

	@Override
	public int getZSpaceRequirement() {
		return 1;
	}

	@Override
	public void registerForZOrdering(IItem item) {}

	@Override
	public void setCapacity(int c) {}

	@Override
	public void setItemZOrder(int zValue) {
		this.zOrder = zValue;
		if(item.getTreeRootSpatial() != null) item.getTreeRootSpatial().setZOrder(zValue);
		if(item.getManipulableSpatial() != null) item.getManipulableSpatial().setZOrder(zValue);
	}
	
	@Override
	public void updateZOrdering() {
		// no need to do anything
	}

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
		
	}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemMoved(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemRotated(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemScaled(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemZOrderChanged(IItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void neverBringToTop(IItem item) {
		
	}
//
//    @Override
//    public void bumpBottom(IItem item) {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public void bumpTop(IItem item) {
//        // TODO Auto-generated method stub
//        
//    }

	@Override
	public void unregisterForZOrdering(IItem i) {}
}
