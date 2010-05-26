package multiplicity.app.multiappsystem;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;


public interface IApplicationInfo {
	
	public enum ReactivatePolicy {
		RESTART,
		RESUME
	}
	
	public Class<? extends AbstractStandaloneApp> getApplicationClass();
	public String getApplicationName();
	public ReactivatePolicy getReactivatePolicy();
	public String getApplicationVersion();
}
