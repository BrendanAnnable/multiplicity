package multiplicity.app.multiappsystem;

import multiplicity.app.AbstractMultiplicityApp;


public interface IApplicationInfo {
	
	public enum ReactivatePolicy {
		RESTART,
		RESUME
	}
	
	public Class<? extends AbstractMultiplicityApp> getApplicationClass();
	public String getApplicationName();
	public ReactivatePolicy getReactivatePolicy();
	public String getApplicationVersion();
}
