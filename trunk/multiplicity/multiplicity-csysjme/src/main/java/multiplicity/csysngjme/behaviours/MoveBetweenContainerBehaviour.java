package multiplicity.csysngjme.behaviours;

import java.util.List;

import org.apache.log4j.Logger;

import com.jme.system.DisplaySystem;

import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.items.IItem;
import multiplicity.csysngjme.items.JMEItem;
import multiplicity.csysngjme.picking.AccuratePickingUtility;
import multiplicity.csysngjme.picking.PickedSpatial;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class MoveBetweenContainerBehaviour implements IBehaviour, IMultiTouchEventListener {
	
	private final static Logger logger = Logger.getLogger(MoveBetweenContainerBehaviour.class.getName());	
	private IItem item;

	@Override
	public void setItemActingOn(IItem item) {
		this.item = item;
		item.getMultiTouchDispatcher().addListener(this);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		logger.info("cursor released caught event: "+item.getParentItem().getClass());
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void removeItemActingOn() {
        // TODO Auto-generated method stub
    }

}
