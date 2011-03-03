package multiplicity.csysng.behaviours;

import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.stage.IStage;

public interface IBehaviour {
	/**
	 * The item whose multi-touch event dispatcher will generate
	 * the events that this behaviour will respond to.
	 * @param eventSourceItem
	 */
	public void setEventSource(final IItem eventSourceItem);
	
	/**
	 * The item that the behaviour should operate on. The behaviourmaker
	 * will set this to be the same as the event source, but this method
	 * gives the option to route the actions elsewhere.
	 * @param item
	 */
	public void setItemActingOn(final IItem item);
	
	/**
	 * Behaviours usually need to know about the stage.
	 * @param stage
	 */
	public void setStage(IStage stage);
	
}
