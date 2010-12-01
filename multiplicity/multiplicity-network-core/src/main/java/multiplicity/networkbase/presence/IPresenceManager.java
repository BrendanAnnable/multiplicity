package multiplicity.networkbase.presence;

import java.util.Collection;

import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.model.DeviceIdentity;

/**
 * Support for presence detection.
 * @author ashatch
 *
 */
public interface IPresenceManager {
	
	/**
	 * Get a (possibly unordered) collection of devices currently
	 * active on the network.
	 * @return
	 * @throws NotConnectedException
	 */
	public Collection<DeviceIdentity> getCurrentDevicesPresent() throws NotConnectedException;
	
	/**
	 * Register an interest in presence status changes.
	 * @param listener
	 */
	public void registerPresenceListener(IPresenceListener listener);
}
