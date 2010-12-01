package multiplicity.networkbase;

import multiplicity.config.network.NetworkConfigPrefsItem;

/**
 * Responsible for instantiating implementations of
 * the multiplicity network system.
 * @author ashatch
 *
 */
public class MultiplicityNetworkFactory {
	
	/**
	 * Inspects the Multiplicity preferences system to determine what
	 * implementation of <code>IMultiplicityNetworkManager</code> to load,
	 * and attempts to construct it. Any concrete implementation of
	 * <code>IMultiplicityNetworkManager</code> must provide a
	 * parameter-less constructor.
	 * 
	 * @return a concrete implementation of IMultiplicityNetworkManager,
	 * or null if the network system is disabled.
	 * 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static IMultiplicityNetworkManager getPreferredNetworkManager() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// get our preferences
		NetworkConfigPrefsItem netPrefs = new NetworkConfigPrefsItem();
				
		if(netPrefs.getNetworkSystemEnabled()) {
			// network system is enabled. Attempt to create it.
			IMultiplicityNetworkManager mgr = (IMultiplicityNetworkManager) Class.forName(netPrefs.getNetworkSystemClass()).newInstance();
			return mgr;
		}
		return null;
	}
}
