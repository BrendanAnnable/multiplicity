package multiplicity.network.xmpp.model;

import multiplicity.networkbase.model.DeviceIdentity;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

public class XMPPEntityMap {

	private static final String MULTIPLICITY_DEVICES_GROUP = "multiplicitydevices";
	private static RosterGroup rosterGroup;

	public static boolean isMultiplicityDevice(XMPPConnection conn, String entity) {
		RosterGroup rg = getMultiplicityGroup(conn);
		return rg.contains(entity);
	}

	public static RosterGroup getMultiplicityGroup(XMPPConnection conn) {
		if(rosterGroup == null) {
			rosterGroup = conn.getRoster().getGroup(MULTIPLICITY_DEVICES_GROUP);
		}
		return rosterGroup;
	}
	
	public static DeviceIdentity getDeviceIdentity(Presence presence) {
//		return new DeviceIdentity(StringUtils.parseBareAddress(presence.getFrom()));
		return new DeviceIdentity(presence.getFrom());
	}

	public static DeviceIdentity getDeviceIdentity(RosterEntry re) {		
		System.out.println("Getting device entity for " + re.getName() + " which is " + re.getUser());
//		return new DeviceIdentity(StringUtils.parseBareAddress(re.getUser()));
		return new DeviceIdentity(re.getUser());
	}

	public static DeviceIdentity getDeviceIdentity(String from) {
//		return new DeviceIdentity(StringUtils.parseBareAddress(from));
		return new DeviceIdentity(from);
	}
}
