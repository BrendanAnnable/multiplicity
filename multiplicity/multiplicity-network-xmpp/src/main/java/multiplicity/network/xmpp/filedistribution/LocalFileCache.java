package multiplicity.network.xmpp.filedistribution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity.network.xmpp.filedistribution.crypto.CryptoUtils;
import multiplicity.networkbase.model.MD5Hash;

public class LocalFileCache {
	private static final Logger log = Logger.getLogger(LocalFileCache.class.getName());
	private static final String propertiesFileName = "info.properties";
	private File dir;

	public LocalFileCache(File directory) {
		this.dir = directory;
		if(!this.dir.exists()) {
			dir.mkdir();
		}
	}
	
	public boolean contains(MD5Hash id) throws IOException {
		File f = getExistingFileForID(id);
		if(f == null) return false;
		if(!f.exists()) return false;
		return true;
	}
	
	public File getExistingFileForID(MD5Hash id) throws IOException {
		log.info("Getting file reference for id " + id);
		Properties props = getPropertiesForID(id);
		if(props == null) return null;
		String name = props.getProperty("name");
		return new File(getDirectoryForID(id), name);
	}
	
	public Properties getPropertiesForID(MD5Hash id) throws IOException {
		if(id == null) return null;
		File dir = getDirectoryForID(id);
		if(!dir.exists()) return null;
		
		File propsFile = new File(dir, propertiesFileName);
		if(!propsFile.exists()) return null;
		
		Properties p = new Properties();		
		log.info("Loading properties from " + propsFile.getAbsolutePath());
		FileInputStream fis = new FileInputStream(propsFile);
		p.load(fis);
		fis.close();
		return p;
	}
	
	public File getDirectoryForID(MD5Hash id) {
		String idstr = id.toString();
		File firstDir = new File(dir, idstr.substring(0, 1));
		File secondDir = new File(firstDir, idstr.substring(1, 2));		
		File idDir = new File(secondDir, idstr);
		return idDir;
	}
	
	public LocalFileCacheEntry add(File f, String device, String owner) throws IOException {		
		try {
			log.info("Adding " + f.getAbsolutePath() + " to content cache at " + dir.getAbsolutePath());
			MD5Hash id = CryptoUtils.md5(f);
			File existingFile = getExistingFileForID(id);
			if(existingFile != null) {
				log.fine("Already have this file in our cache. Done.");
				return new LocalFileCacheEntry(id, existingFile);
			}
			
			log.fine("File not in the cache, copying in.");
			
			File idDir = getDirectoryForID(id);
			if(!idDir.exists()) idDir.mkdirs();
			
			File copyTo = new File(idDir, f.getName());		
			copyFile(f, copyTo);
			writeProperties(id, f.getName(), device, owner, idDir);
			return new LocalFileCacheEntry(id, copyTo);		

		} catch (NoSuchAlgorithmException e) {
			log.log(Level.SEVERE, "Don't have access to the MD5 digest.", e);
		}
		return null;
	}
	
	private void writeProperties(MD5Hash id, String name, String device, String owner, File idDir) throws IOException {
		Properties p = new Properties();
		p.setProperty("id", id.toString());
		p.setProperty("name", name);
		p.setProperty("device", device);
		p.setProperty("owner", owner);
		FileOutputStream fos = new FileOutputStream(new File(idDir, propertiesFileName));
		p.store(fos, "");
		fos.close();
	}

	private void copyFile(File f, File copyTo) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		FileOutputStream fos = new FileOutputStream(copyTo);
		byte[] buf = new byte[2048];
		int read;
		while((read = fis.read(buf)) != -1) {
			fos.write(buf, 0, read);
		}
		fos.close();
		fis.close();
	}
	
	public static void main(String[] args) {
		LocalFileCache cc = new LocalFileCache(new File("testcache"));
		try {
			File f = new File("test.png");
			LocalFileCacheEntry entry = cc.add(f, "abc", "fred");
			MD5Hash id = entry.getHash();
			System.out.println(cc.getPropertiesForID(id));
			System.out.println("Does file exist? " + cc.contains(id));
			System.out.println(cc.getExistingFileForID(id).getAbsolutePath());
			System.out.println(cc.getPropertiesForID(new MD5Hash("01234567890123456789012345678901")));
			System.out.println("Does file exist? " + cc.contains(new MD5Hash("01234567890123456789012345678901")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
