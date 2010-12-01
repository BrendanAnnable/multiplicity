package multiplicity.networkbase.messaging;

import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.messaging.exceptions.MessagingException;
import multiplicity.networkbase.model.DeviceIdentity;

/**
 * Provides inter-device messaging via named channels.
 * @author ashatch
 *
 */
public interface IMessageManager {
	
	/**
	 * Send a serializable object message to a destination channel
	 * on one or more devices.
	 * 
	 * @param msg
	 * @param destinationChannel
	 * @param devices
	 * @throws NotConnectedException
	 * @throws MessagingException
	 */
	public void sendMessage(Object msg, String destinationChannel,
			DeviceIdentity... devices) throws NotConnectedException, MessagingException;
	
	/**
	 * Listen to messages sent to the device on the named channel.
	 * 
	 * @param destinationChannel
	 * @param listener
	 */
	public void registerMessageListener(String destinationChannel, IMessageListener listener);
}
