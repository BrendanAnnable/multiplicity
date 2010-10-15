package multiplicity.config.network;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity.config.ConfigurationApplication;
import multiplicity.config.PreferencesItem;

public class NetworkConfigPrefsItem implements PreferencesItem {
	
	private static final Preferences prefs = ConfigurationApplication.getPreferences(NetworkConfigPrefsItem.class);
	
	private static final String HTTP_PROXY_HOST = "HTTP_PROXY_HOST";
	private static final String HTTP_PROXY_PORT = "HTTP_PROXY_PORT";
	private static final String HTTP_PROXY_ENABLED = "HTTP_PROXY_ENABLED";
	
	private static final String XMPP_SERVER_HOST = "XMPP_SERVER_HOST";
	private static final String XMPP_SERVER_PORT = "XMPP_SERVER_PORT";
	private static final String XMPP_ENABLED = "XMPP_ENABLED";
	private static final String XMPP_USER = "XMPP_USER";
	private static final String XMPP_PASSWORD = "XMPP_PASSWORD";
        private static final String XMPP_CHOOSE_USER_AT_LAUNCH = "XMPP_CHOOSE_USER_AT_LAUNCH";
	

	@Override
	public JPanel getConfigurationPanel() {
		return new NetworkPrefsPanel2(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Network";
	}

	public String getProxyHost() {
		return prefs.get(HTTP_PROXY_HOST, "");
	}
	
	public void setProxyHost(String host) {
		prefs.put(HTTP_PROXY_HOST, host);
	}
	
	public int getProxyPort() {
		return prefs.getInt(HTTP_PROXY_PORT, 8080);
	}

	public void setProxyPort(int port) {
		prefs.putInt(HTTP_PROXY_PORT, port);
	}
	
	public void setProxyEnabled(boolean b) {
		prefs.putBoolean(HTTP_PROXY_ENABLED, b);
	}
	
	public boolean getProxyEnabled() {
		return prefs.getBoolean(HTTP_PROXY_ENABLED, false);
	}
        
	public void setXMPPHost(String host) {
		prefs.put(XMPP_SERVER_HOST, host);
	}
	
	public String getXMPPHost() {
		return prefs.get(XMPP_SERVER_HOST, "");
	}
      
	public void setXMPPPort(int port) {
		prefs.putInt(XMPP_SERVER_PORT, port);
	}
	
	public int getXMPPPort() {
		return prefs.getInt(XMPP_SERVER_PORT, 5222);
	}
	
	public void setXMPPEnabled(boolean isEnabled) {
		prefs.putBoolean(XMPP_ENABLED, isEnabled);
	}
	
	public boolean getXMPPEnabled() {
		return prefs.getBoolean(XMPP_ENABLED, false);
	}
	
	public void setXMPPUser(String user) {
		prefs.put(XMPP_USER, user);
	}
	
	public String getXMPPUser() {
		return prefs.get(XMPP_USER, "");
	}
	
	public void setXMPPPassword(String password) {
		prefs.put(XMPP_PASSWORD, password);
	}
	
	public String getXMPPPassword() {
		return prefs.get(XMPP_PASSWORD, "");
	}
        
        public boolean getChooseUserAtLaunch() {
            return prefs.getBoolean(XMPP_CHOOSE_USER_AT_LAUNCH, false);
        }
        
        public void setChooseUserAtLaunch(boolean choose) {
            prefs.putBoolean(XMPP_CHOOSE_USER_AT_LAUNCH, choose);
        }
}
