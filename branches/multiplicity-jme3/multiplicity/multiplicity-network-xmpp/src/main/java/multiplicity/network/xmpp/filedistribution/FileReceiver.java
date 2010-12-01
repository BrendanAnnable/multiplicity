package multiplicity.network.xmpp.filedistribution;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity.networkbase.filedistribution.IFileStatusCallback;
import multiplicity.networkbase.model.MD5Hash;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;


public class FileReceiver implements FileTransferListener {
	private static final Logger log = Logger.getLogger(FileReceiver.class.getName());
	private LocalFileCache cache;
	private Map<MD5Hash, UUIDToFileTransferMapping> map = new HashMap<MD5Hash, UUIDToFileTransferMapping>();
	private FileDistributionManager manager;

	public FileReceiver(FileDistributionManager manager, LocalFileCache cache) {
		this.manager = manager;
		this.cache = cache;
	}
	
	public void receiveFile(MD5Hash uid, String device, String owner, IFileStatusCallback cback) {
		map.put(uid,new UUIDToFileTransferMapping(device, owner, cback));
	}

	public void fileTransferRequest(final FileTransferRequest request) {
		final IncomingFileTransfer transfer = request.accept();
		
		final File f;
		try {
			f = File.createTempFile("mn_filexfer", "tmp");
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not create a temporary storage place for file transfer.", e);
			return;
		}
		log.info("File " + request.getFileName() + " being saved as " + f.getAbsolutePath());
		try {
			final String uidstr = request.getDescription();
			transfer.recieveFile(f);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while(!transfer.isDone()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							log.log(Level.WARNING, "Thread error.", e);
						}
					}
					log.info("File " + request.getFileName() + " received successfully.");
					final MD5Hash expectedHash = new MD5Hash(uidstr);
					try {
						UUIDToFileTransferMapping ufm = map.get(expectedHash);

						if(ufm.cback != null) {							
							LocalFileCacheEntry entry = cache.add(f, ufm.device, ufm.owner);
							if(entry.getHash().equals(expectedHash)) {
								manager.fileTransferComplete(entry.getHash(), entry.getFile().toURI().toURL(), ufm.cback);
							}
						}
						f.delete();
					} catch (IOException e) {
						log.log(Level.SEVERE, "Problem writing to cache.", e);
					}
				}
			});
			t.start();
		} catch (XMPPException ex) {
			log.log(Level.SEVERE, null, ex);
		}
	}
	
	private class UUIDToFileTransferMapping {
		public String device;
		public String owner;
		public IFileStatusCallback cback;
		
		public UUIDToFileTransferMapping(String device, String owner, IFileStatusCallback cback) {
			this.device = device;
			this.owner = owner;
			this.cback = cback;
		}
	}
}
