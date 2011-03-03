package multiplicity3.config.network.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class XMPPConfigPrefsItem implements PreferencesItem {
	
	private static final Preferences prefs = ConfigurationApplication.getPreferences(XMPPConfigPrefsItem.class);
	
	private static final String XMPP_SERVER_HOST = "XMPP_SERVER_HOST";
	private static final String XMPP_SERVER_PORT = "XMPP_SERVER_PORT";
	private static final String XMPP_USER = "XMPP_USER";
	private static final String XMPP_PASSWORD = "XMPP_PASSWORD";
        private static final String XMPP_CHOOSE_USER_AT_LAUNCH = "XMPP_CHOOSE_USER_AT_LAUNCH";
	private static final String XMPP_CONTENT_SERVER_ID = "XMPP_CONTENT_SERVER_ID";
        private static final String XMPP_CONTENT_DIR = "XMPP_CONTENT_DIR";

	@Override
	public JPanel getConfigurationPanel() {
		return new XMPPPrefsPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Network";
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
        
        public String getContentServerID() {
            return prefs.get(XMPP_CONTENT_SERVER_ID, "");
        }
        
        public void setContentServerID(String id) {
            prefs.put(XMPP_CONTENT_SERVER_ID, id);
        }
        
        public File getContentDir() {
            return new File(prefs.get(XMPP_CONTENT_DIR, "."));
        }
        
        public void setContentDir(File f) {
            try {
                prefs.put(XMPP_CONTENT_DIR, f.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(XMPPConfigPrefsItem.class.getName()).log(Level.SEVERE, "Could not set content directory preferences.", ex);
            }
        }
}
