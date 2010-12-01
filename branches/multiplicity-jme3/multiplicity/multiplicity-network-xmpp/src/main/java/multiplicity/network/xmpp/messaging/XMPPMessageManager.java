package multiplicity.network.xmpp.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import multiplicity.network.xmpp.model.XMPPEntityMap;
import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.messaging.IMessageListener;
import multiplicity.networkbase.messaging.IMessageManager;
import multiplicity.networkbase.messaging.exceptions.MessagingException;
import multiplicity.networkbase.model.DeviceIdentity;

/**
 * Uses Smack's <code>setProperty()</code> feature to send
 * arbitrary object messages as XMPP Message packets.
 * {@link IMessageListener}s are notified when messages are received
 * on the relevant associated channel (established via
 * <code>registerMessageListener()</code>).
 * @author ashatch
 *
 */
public class XMPPMessageManager implements IMessageManager, PacketListener {
	private static final Logger log = Logger.getLogger(XMPPMessageManager.class.getName());
	private ConcurrentLinkedQueue<Callable<Void>> updateList;
	private Map<String,XMPPChannelListeners> channelListeners;
	private XMPPConnection connection;

	public XMPPMessageManager(XMPPConnection connection) {
		this.setConnection(connection);
		updateList = new ConcurrentLinkedQueue<Callable<Void>>();
		this.channelListeners = new HashMap<String,XMPPChannelListeners>();
		connection.addPacketListener(this, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return true;
			}			
		});
	}

	@Override
	public void sendMessage(Object msg, String destinationChannel,
			DeviceIdentity... devices) throws NotConnectedException,
			MessagingException {
		for(DeviceIdentity di : devices) {
			String toString = di.getStringRepresentation();
			log.info("Sending message to " + toString);
			Message m = new Message(toString);
			m.setProperty(XMPPMessageKeys.PROPERTY_CHANNEL, destinationChannel);
			m.setProperty(XMPPMessageKeys.PROPERTY_PAYLOAD, msg);
			connection.sendPacket(m);
		}
	}

	@Override
	public void registerMessageListener(String destinationChannel, IMessageListener listener) {
		getChannelListeners(destinationChannel, true).addListener(listener);
	}

	// *** business logic ***

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	// *** PacketListener method ***

	@Override
	public void processPacket(Packet packet) {
		if(!(packet instanceof Message)) return;

		Message m = (Message) packet;

		final Object channel = m.getProperty(XMPPMessageKeys.PROPERTY_CHANNEL);
		if(channel == null) return;
		if(!(channel instanceof String)) return;

		final Object payload = m.getProperty(XMPPMessageKeys.PROPERTY_PAYLOAD);
		if(payload == null) return;
		
		final DeviceIdentity from = XMPPEntityMap.getDeviceIdentity(m.getFrom());

		if(from == null) return;
		
		updateList.add(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				XMPPChannelListeners listeners = channelListeners.get(channel);
				if(listeners != null) {
					listeners.notifyMessageReceived(from, payload);
				}
				return null;
			}
		});
	}

	private XMPPChannelListeners getChannelListeners(String destinationChannel, boolean createIfDoesNotExist) {
		XMPPChannelListeners listeners = channelListeners.get(destinationChannel);
		if(listeners == null && createIfDoesNotExist) {
			listeners = new XMPPChannelListeners(destinationChannel);
			channelListeners.put(destinationChannel, listeners);
		}
		return listeners;
	}

	public void update() {
		for(Callable<Void> c : updateList) {
			try {
				c.call();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			updateList.remove(c);
		}
	}

}
