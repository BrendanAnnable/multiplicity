package multiplicity.network.xmpp.messaging;

import java.util.ArrayList;
import java.util.List;

import multiplicity.networkbase.messaging.IMessageListener;
import multiplicity.networkbase.model.DeviceIdentity;

class XMPPChannelListeners {
	private String channel;
	private List<IMessageListener> listeners;

	public XMPPChannelListeners(String channel) {
		listeners = new ArrayList<IMessageListener>();
		this.setChannel(channel);
	}
	
	public void addListener(IMessageListener listener) {
		if(listeners.contains(listener)) return;		
		listeners.add(listener);
	}
	
	public void notifyMessageReceived(DeviceIdentity fromid, Object obj) {
		for(IMessageListener listener : listeners) {
			listener.messageReceived(fromid, channel, obj);
		}
	}
	
	public List<IMessageListener> getListeners() {
		return listeners;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}
}
