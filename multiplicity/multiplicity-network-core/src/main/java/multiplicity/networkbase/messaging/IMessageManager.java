package multiplicity.networkbase.messaging;

import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.messaging.exceptions.MessagingException;
import multiplicity.networkbase.model.DeviceIdentity;

public interface IMessageManager {
	
	public void sendMessage(Object msg, String destinationChannel,
			DeviceIdentity... devices) throws NotConnectedException, MessagingException;
	
	public void registerMessageListener(String destinationChannel, IMessageListener listener);
}
