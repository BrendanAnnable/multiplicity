package multiplicity.networkbase.messaging;

import multiplicity.networkbase.model.DeviceIdentity;

/**
 * Listener associated with {@link IMessageManager}.
 * @author ashatch
 *
 */
public interface IMessageListener {
	/**
	 * Called by the {@link IMessageManager} when a message is received
	 * on the named channel.
	 * 
	 * @param from
	 * @param channel
	 * @param msg
	 */
	public void messageReceived(DeviceIdentity from, String channel, Object msg);
}
