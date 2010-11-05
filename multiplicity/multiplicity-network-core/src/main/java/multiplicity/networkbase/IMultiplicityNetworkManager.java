package multiplicity.networkbase;

import multiplicity.networkbase.contentdistribution.IContentDistributionManager;
import multiplicity.networkbase.exceptions.AlreadyConnectedException;
import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.messaging.IMessageManager;
import multiplicity.networkbase.model.DeviceIdentity;
import multiplicity.networkbase.presence.IPresenceManager;

/**
 * Container for different aspects of networking, including presence
 * detection, messaging and file content distribution.
 * 
 * @author ashatch
 */
public interface IMultiplicityNetworkManager {
	
	/**
	 * Attempt to make a connection. If the network is
	 * already connected, an exception will be thrown.
	 * @throws AlreadyConnectedException
	 */
	public void connect() throws AlreadyConnectedException;
	
	/**
	 * Is this network manager connected.
	 * @return
	 */
	public boolean isConnected();
	
	/**
	 * Attempts to disconnect the network. If the
	 * network is not connected, an exception will be
	 * thrown.
	 * @throws NotConnectedException
	 */
	public void disconnect() throws NotConnectedException;
	
	
	/**
	 * Get the presence manager.
	 * @return
	 */
	public IPresenceManager getPresenceManager();
	
	/**
	 * Get the message manager.
	 * @return
	 */
	public IMessageManager getMessageManager();
	
	/**
	 * Get the content distribution manager.
	 * @return
	 */
	public IContentDistributionManager getContentDistributionManager();
	

	/**
	 * Get the device identity for this device's connection to the network.
	 * @return
	 */
	public DeviceIdentity getDeviceIdentity();
	
	/**
	 * Instances of <code>IMultiplicityNetworkManager</code> are designed
	 * to support single-threaded applications such that, for example, event-based
	 * systems are supported such that the thread that calls <code>update()</code> will
	 * be the thread that dispatches events out of the network system. This is useful
	 * for OpenGL based applications. For situations where single-threadedness is
	 * not important, create a thread that calls <code>update()</code> frequently. 
	 */
	public void update();
}
