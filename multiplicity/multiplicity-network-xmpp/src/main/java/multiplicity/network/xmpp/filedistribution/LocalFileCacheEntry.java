package multiplicity.network.xmpp.filedistribution;

import java.io.File;
import java.io.Serializable;

import multiplicity.networkbase.model.MD5Hash;

public class LocalFileCacheEntry implements Serializable {
	private static final long serialVersionUID = 3191230924406069447L;
	
	private MD5Hash hash;
	private File file;

	public LocalFileCacheEntry(MD5Hash hash, File file) {
		this.hash = hash;
		this.file = file;
	}
	
	public MD5Hash getHash() {
		return hash;
	}

	public File getFile() {
		return file;
	}
}
