package multiplicity.network.xmpp.filedistribution;

import java.io.File;

import multiplicity.networkbase.filedistribution.IFileStatusCallback;
import multiplicity.networkbase.model.MD5Hash;

class CallbackPendingStatusUpdate {
	
	public static final int REGISTRATION = 0;
	public static final int RETRIEVAL = 1;	
	
	private MD5Hash id;
	private IFileStatusCallback callback;
	private long registrationTime;
	private long timeoutMillis;
	private int type;
	private File file;

	public CallbackPendingStatusUpdate(MD5Hash id, IFileStatusCallback callback, long registrationTime, long timeOutMillis, int type) {
		this.setId(id);
		this.setCallback(callback);
		this.setRegistrationTime(registrationTime);
		this.setTimeoutMillis(timeOutMillis);
		this.setType(type);
	}

	public void setId(MD5Hash id) {
		this.id = id;
	}

	public MD5Hash getId() {
		return id;
	}

	public void setCallback(IFileStatusCallback callback) {
		this.callback = callback;
	}

	public IFileStatusCallback getCallback() {
		return callback;
	}


	public void setTimeoutMillis(long timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	public long getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setRegistrationTime(long registrationTime) {
		this.registrationTime = registrationTime;
	}

	public long getRegistrationTime() {
		return registrationTime;
	}

	public void setFile(File f) {
		this.file = f;		
	}
	
	public File getFile() {
		return this.file;
	}
}
