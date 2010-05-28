package multiplicity.app.utils;

import java.io.File;

public class LocalStorageUtility {
	
	public LocalStorageUtility() {
	}

	public static File getLocalWorkingDirectory(String appName, String appVersion) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory = new File(".");
		String sysName = System.getProperty("os.name").toLowerCase();
		String dirName = appName+appVersion;
		
		if(sysName.contains("windows")) {
			final String applicationData = System.getenv("APPDATA");
            if (applicationData != null)
                workingDirectory = new File(applicationData, "." + dirName + File.pathSeparatorChar);
            else
                workingDirectory = new File(userHome, '.' + dirName + File.pathSeparatorChar);
		}else if(sysName.contains("mac")) {
			workingDirectory = new File(userHome, "Library/Application Support/" + dirName );
		}else if(sysName.contains("solaris")) {
			workingDirectory = new File(userHome, '.' + dirName + File.pathSeparatorChar);
		}else if(sysName.contains("linux")) {
			// ?
		}else{
			workingDirectory = new File(".");
		}
		
		return workingDirectory;
	}
}
