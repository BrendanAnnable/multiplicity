package multiplicity.network.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPConnectionFactory {
	
	public static XMPPConnection createConnection(String host, String user, String password) throws XMPPException {
		return createConnection(host, 5222, user, password, false, true, false);
	}
	
	public static XMPPConnection createConnection(String host, int port, String user, String password) throws XMPPException {
		return createConnection(host, port, user, password, false, true, false);
	}
	
	public static XMPPConnection createConnection(String host, int port, String user, String password,
			boolean isDebugEnabled, boolean isCompressionEnabled, boolean isSASLAuthenticationEnabled) throws XMPPException {
		
		ConnectionConfiguration cc = new ConnectionConfiguration(host, port);
		cc.setDebuggerEnabled(isDebugEnabled);
		cc.setCompressionEnabled(isCompressionEnabled);
		cc.setSASLAuthenticationEnabled(isSASLAuthenticationEnabled);

		XMPPConnection connection = new XMPPConnection(cc);
		connection.connect();
		connection.login(user, password, "default");		
		
		return connection;
	}
}
