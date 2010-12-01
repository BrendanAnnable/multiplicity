package multiplicity.network.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import multiplicity.config.network.xmpp.XMPPConfigPrefsItem;
import multiplicity.network.xmpp.contentsync.ContentSyncManager;
import multiplicity.network.xmpp.exceptions.XMPPConnectionException;
import multiplicity.network.xmpp.filedistribution.FileDistributionManager;
import multiplicity.network.xmpp.messaging.XMPPMessageManager;
import multiplicity.network.xmpp.model.XMPPEntityMap;
import multiplicity.network.xmpp.presence.XMPPPresenceManager;
import multiplicity.network.xmpp.ui.UsernamePasswordUI;
import multiplicity.network.xmpp.ui.UsernamePasswordUI.UsernamePassword;
import multiplicity.networkbase.IMultiplicityNetworkManager;
import multiplicity.networkbase.contentsync.IContentSynchronisationManager;
import multiplicity.networkbase.exceptions.AlreadyConnectedException;
import multiplicity.networkbase.filedistribution.IFileDistributionManager;
import multiplicity.networkbase.messaging.IMessageManager;
import multiplicity.networkbase.model.DeviceIdentity;
import multiplicity.networkbase.presence.IPresenceManager;

public class XMPPMultiplicityNetworkManager implements IMultiplicityNetworkManager {
	
	private XMPPConnection connection;
	private XMPPPresenceManager presenceManager;
	private XMPPMessageManager messageManager;
	private FileDistributionManager distributionManager;
	private ContentSyncManager syncManager;
	
	public XMPPMultiplicityNetworkManager(XMPPConnection connection) throws XMPPConnectionException, XMPPException {
		DeviceIdentity contentServerID = XMPPEntityMap.getDeviceIdentity(new XMPPConfigPrefsItem().getContentServerID());		
		setup(connection, contentServerID, new XMPPConfigPrefsItem().getContentDir());
	}
	
	public XMPPMultiplicityNetworkManager() throws XMPPConnectionException, XMPPException {
		XMPPConfigPrefsItem xmppPrefs = new XMPPConfigPrefsItem();
		
		DeviceIdentity contentServerID = XMPPEntityMap.getDeviceIdentity(xmppPrefs.getContentServerID());		
		
		String user = xmppPrefs.getXMPPUser();
		String pass = xmppPrefs.getXMPPPassword();
		
		if(xmppPrefs.getChooseUserAtLaunch()) {
			UsernamePassword up = UsernamePasswordUI.promptForUsernameAndPassword();
			user = up.getUsername();
			pass = up.getPassword();
		}
		
		XMPPConnection connection = XMPPConnectionFactory.createConnection(xmppPrefs.getXMPPHost(), xmppPrefs.getXMPPPort(), user, pass);
		
		setup(connection, contentServerID, new XMPPConfigPrefsItem().getContentDir());
	}
	
	private void setup(XMPPConnection connection, DeviceIdentity contentServerID, File contentStoreDirectory) throws XMPPConnectionException {
		if(!connection.isConnected()) throw new XMPPConnectionException("Connection supplied is not connected.");
		setConnection(connection);
		presenceManager = new XMPPPresenceManager(getConnection());
		messageManager = new XMPPMessageManager(getConnection());	
		distributionManager = new FileDistributionManager(getDeviceIdentity().getStringRepresentation(), connection, contentServerID, contentStoreDirectory);
		syncManager = new ContentSyncManager(getConnection());
		
	}

	@Override
	public void connect() throws AlreadyConnectedException {
		if(getConnection().isConnected()) throw new AlreadyConnectedException();
	}

	@Override
	public boolean isConnected() {
		return connection.isConnected();
	}

	@Override
	public void disconnect() {
		connection.disconnect();
	}

	@Override
	public IPresenceManager getPresenceManager() {
		return presenceManager;
	}

	@Override
	public IMessageManager getMessageManager() {
		return messageManager;
	}

	@Override
	public IFileDistributionManager getFileDistributionManager() {
		return distributionManager;
	}
	
	@Override
	public IContentSynchronisationManager getContentSynchronisationManager() {
		return syncManager;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	@Override
	public void update() {
		messageManager.update();
		presenceManager.update();
		distributionManager.update();
	}
	
	@Override
	public DeviceIdentity getDeviceIdentity() {
		return XMPPEntityMap.getDeviceIdentity(connection.getUser());
	}


}
