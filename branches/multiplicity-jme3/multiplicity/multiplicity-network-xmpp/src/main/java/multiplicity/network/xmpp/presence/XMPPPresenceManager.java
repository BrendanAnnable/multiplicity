package multiplicity.network.xmpp.presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import multiplicity.network.xmpp.model.XMPPEntityMap;
import multiplicity.networkbase.exceptions.NotConnectedException;
import multiplicity.networkbase.model.DeviceIdentity;
import multiplicity.networkbase.presence.IPresenceListener;
import multiplicity.networkbase.presence.IPresenceManager;

public class XMPPPresenceManager implements IPresenceManager, RosterListener {
	private static final Logger log = Logger.getLogger(XMPPPresenceManager.class.getName());

	private ConcurrentLinkedQueue<Callable<Void>> updateList;
	private Set<DeviceIdentity> currentDevicesPresent;
	private List<IPresenceListener> listeners;
	private XMPPConnection connection;


	public XMPPPresenceManager(XMPPConnection connection) {
		log.log(Level.INFO, "Creating XMPP Presence Manager");
		setConnection(connection);

		currentDevicesPresent = new HashSet<DeviceIdentity>();		
		listeners = new ArrayList<IPresenceListener>();
		updateList = new ConcurrentLinkedQueue<Callable<Void>>();

		Roster r = connection.getRoster();
		r.addRosterListener(this);
		notifyCurrentPresence(connection);
	}

	private void notifyCurrentPresence(XMPPConnection connection) {
		Roster r = connection.getRoster();
		RosterGroup rg = XMPPEntityMap.getMultiplicityGroup(connection);
		if(rg == null) {
			log.log(Level.WARNING, "Could not find the multiplicity roster group.");
			return;
		}
		log.log(Level.FINE, "Loading roster entries.");
		for(RosterEntry re : rg.getEntries()) {
			Presence presence = r.getPresence(re.getUser());
			if(presence.isAvailable()) {
				DeviceIdentity did = XMPPEntityMap.getDeviceIdentity(re);			
				notifyDeviceAvailable(did);	
			}
		}
	}

	@Override
	public Collection<DeviceIdentity> getCurrentDevicesPresent()
	throws NotConnectedException {
		return Collections.unmodifiableCollection(currentDevicesPresent);
	}

	@Override
	public void registerPresenceListener(IPresenceListener listener) {
		if(listeners.contains(listener)) return;
		listeners.add(listener);
		//notifyNewListener(listener);
	}

	//*** roster listener methods ***

	@Override
	public void entriesAdded(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

	@Override
	public void entriesUpdated(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

	@Override
	public void entriesDeleted(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

	@Override
	public void presenceChanged(Presence presence) {
		if(!XMPPEntityMap.isMultiplicityDevice(getConnection(), presence.getFrom()))  return;		
		if(presence.isAvailable()) {
			notifyDeviceAvailable(XMPPEntityMap.getDeviceIdentity(presence));
		}else {
			notifyDeviceUnavailable(XMPPEntityMap.getDeviceIdentity(presence));
		}		
	}

	// *** business logic ***

	private void notifyDeviceUnavailable(final DeviceIdentity did) {
		updateList.add(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				synchronized(currentDevicesPresent) {
					if(currentDevicesPresent.remove(did)) {
						for(IPresenceListener l : listeners) {			
							l.deviceUnavailable(did);	
						}			
					}
				}
				return null;
			}			
		});
	}

	private void notifyDeviceAvailable(final DeviceIdentity did) {
		log.log(Level.FINER, "Device " + did + " is now available.");
		updateList.add(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				synchronized(currentDevicesPresent) {
					if(currentDevicesPresent.add(did)) {
						log.log(Level.FINER, "Added " + did + " to currentDevicesPresent list. Notifying listeners.");
						for(IPresenceListener l : listeners) {	
							l.deviceAvailable(did);				
						}
					}else{
						log.log(Level.FINER, "Already had " + did + " in the currentDevicesPresent list.");
					}
				}
				return null;
			}
		});
	}

//	private void notifyNewListener(final IPresenceListener listener) {
//		log.log(Level.FINER, "New listener added, scheduling update.");
//		updateList.add(new Callable<Void>() {
//			@Override
//			public Void call() throws Exception {
//				synchronized(currentDevicesPresent) {
//					log.log(Level.FINER, "Notifying " + listener + " of currentDevicesPresent " + currentDevicesPresent);
//					for(DeviceIdentity di : currentDevicesPresent) {
//						listener.deviceAvailable(di);
//					}
//				}	
//				return null;
//			}
//		});
//	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void update() {
		if(updateList.size() < 1) return;
		log.log(Level.FINER, "Calling update on " + updateList.size() + " callable items.");
		for(Callable<Void> c : updateList) {
			try {
				log.log(Level.FINER, "calling.");
				c.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.log(Level.FINER, "removing.");
			updateList.remove(c);
		}
		log.log(Level.FINER, "update complete.");
	}
}
