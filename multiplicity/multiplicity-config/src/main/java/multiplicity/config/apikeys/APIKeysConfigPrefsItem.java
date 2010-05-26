package multiplicity.config.apikeys;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity.config.ConfigurationApplication;
import multiplicity.config.PreferencesItem;

public class APIKeysConfigPrefsItem implements PreferencesItem {
	
	private static final Preferences prefs = ConfigurationApplication.getPreferences(APIKeysConfigPrefsItem.class);
	
	private static final String FLICKR_API_KEY = "FLICKR_API_KEY";
	private static final String FLICKR_API_SECRET = "FLICKR_API_SECRET";

	@Override
	public JPanel getConfigurationPanel() {
		return new APIKeyPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "API Keys";
	}
	
	public String getFlickrAPIKey() {
		return prefs.get(FLICKR_API_KEY, "");
	}
	
	public void setFlickrAPIKey(String key) {
		prefs.put(FLICKR_API_KEY, key);
	}
	
	public String getFlickrAPISecret() {
		return prefs.get(FLICKR_API_SECRET, "");
	}
	
	public void setFlickrAPISecret(String secret) {
		prefs.put(FLICKR_API_SECRET, secret);
	}
}
