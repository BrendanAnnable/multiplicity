package multiplicity.networkbase.messaging;

import multiplicity.networkbase.model.DeviceIdentity;

public interface IMessageListener {
	public void messageReceived(DeviceIdentity from, String channel, Object msg);
}
