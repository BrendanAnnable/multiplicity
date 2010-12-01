package multiplicity.network.xmpp.filedistribution;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import multiplicity.network.xmpp.filedistribution.messages.ContentStatus;
import multiplicity.network.xmpp.filedistribution.messages.PeerTransferRequest;
import multiplicity.network.xmpp.filedistribution.messages.QueryContentIQ;
import multiplicity.network.xmpp.filedistribution.messages.RegisterContentIQ;
import multiplicity.networkbase.filedistribution.IFileStatusCallback;
import multiplicity.networkbase.filedistribution.IFileDistributionManager;
import multiplicity.networkbase.model.DeviceIdentity;
import multiplicity.networkbase.model.MD5Hash;

public class FileDistributionManager implements IFileDistributionManager, PacketListener {
	public static final String namespace = "multiplicity:content";
	public static final String CHANNEL = "contentdistribution";
	
	private static final Logger log = Logger.getLogger(FileDistributionManager.class.getName());

	private ConcurrentHashMap<String,CallbackPendingStatusUpdate> callbacksPendingStatusUpdate = new ConcurrentHashMap<String,CallbackPendingStatusUpdate>();
	private ConcurrentLinkedQueue<Callable<Void>> updateList = new ConcurrentLinkedQueue<Callable<Void>>();
	private String localID;
	private DeviceIdentity contentServerID;
	private LocalFileCache fileCache;
	private FileReceiver receiver;
	private FileTransferManager fileTransferManager;
	private XMPPConnection connection;

	public FileDistributionManager(String localID, XMPPConnection connection, DeviceIdentity contentServerID, File contentStoreDirectory) {
		this.localID = localID;
		this.contentServerID = contentServerID;
		this.connection = connection;
		fileCache = new LocalFileCache(contentStoreDirectory);
		receiver = new FileReceiver(this, fileCache);

		connection.addPacketListener(this, new PacketFilter() {
			@Override
			public boolean accept(Packet p) {
				return true;
			}			
		});
		
		fileTransferManager = new FileTransferManager(connection);
		FileTransferNegotiator.setServiceEnabled(connection, true);
		ProviderManager.getInstance().addIQProvider("contentstatus", namespace, ContentStatus.class);
		ProviderManager.getInstance().addIQProvider("peerxfer", namespace, PeerTransferRequest.class);
	}

	@Override
	public MD5Hash registerContent(File f, IFileStatusCallback callback, long timeoutMillis) {		
		try {
			log.info("Request to register content for " + f.getAbsolutePath());
			LocalFileCacheEntry entry = fileCache.add(f, localID, "default");
			MD5Hash id = entry.getHash();
			File storedFile = entry.getFile();			
			
			// setup a packet to request content registration at server
			RegisterContentIQ iq = new RegisterContentIQ(id, storedFile.getName(), localID);
			iq.setTo(contentServerID.getStringRepresentation());
			
			// we use the update() loop to process calls to callbacks, so add to queue.
			CallbackPendingStatusUpdate pr = new CallbackPendingStatusUpdate(id, callback, System.currentTimeMillis(), timeoutMillis, CallbackPendingStatusUpdate.REGISTRATION);
			pr.setFile(f);
			callbacksPendingStatusUpdate.put(id.toString(), pr);
			
			// now we're all setup to deal with a reply, lets send packet to server.
			connection.sendPacket(iq);
			return id;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not register content.", e);
		}
		return null;
	}

	@Override
	public void retrieveContent(final MD5Hash contentID, final IFileStatusCallback cback, long timeoutMillis) {
		log.info("Request to get content with hash " + contentID);
		try {
			if(!fileCache.contains(contentID)) {
				log.info("Cache MISS on " + contentID + ", so we need to get it.");
				// we don't have the file. Request info on where to get it.
				QueryContentIQ query = new QueryContentIQ(contentID);
				query.setTo(contentServerID.getStringRepresentation());
				CallbackPendingStatusUpdate pr = new CallbackPendingStatusUpdate(contentID, cback, System.currentTimeMillis(), timeoutMillis, CallbackPendingStatusUpdate.RETRIEVAL);
				callbacksPendingStatusUpdate.put(contentID.toString(), pr);
				connection.sendPacket(query);			
			}else{
				log.info("Cache HIT on " + contentID + ", lets pull it out of the cache.");
				// file exists. Report it back on next update.
				final File f = fileCache.getExistingFileForID(contentID);
				log.info(contentID + " is file " + f.getAbsolutePath());
				updateList.add(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						log.info("Sending content received message to callback for " + contentID + " (" + f.getAbsolutePath() + ")");
						cback.contentReceived(contentID, f.toURI().toURL(), true);
						return null;
					}
				});
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not retrieve content.", e);
			cback.contentNotReceived(contentID);
		}		
	}

	@Override
	public void processPacket(final Packet p) {
		log.info(p.getClass().getName() + ": " + p);

		if(p instanceof ContentStatus) {
			final ContentStatus cs = (ContentStatus)p;
			log.info("Status: " + cs.getStatus());

			if("registered".equals(cs.getStatus())) {
				handleRegistration(cs, false);
			}else if("alreadyregistered".equals(cs.getStatus())) {
				handleRegistration(cs, true);
			}else if("queryresult".equals(cs.getStatus())) {
				handleQueryResult(cs);
			}
		}else if(p instanceof PeerTransferRequest) {			
			PeerTransferRequest req = (PeerTransferRequest) p;
			log.info("Peer transfer request received from " + p.getFrom());
			
	        // Create the outgoing file transfer
	        final OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(req.getFrom());

	        // Send the file
	        try {
	        	log.info("Getting file reference from cache.");
		        File f = fileCache.getExistingFileForID(req.getId());
		        if(f == null) {
		        	log.log(Level.SEVERE, "Request for a file we don't have. Ignoring.");
		        	return;
		        }
		        log.info("File is " + f.getAbsolutePath());
	        	log.info("Attempting send.");
				transfer.sendFile(f, req.getId().toString());
		        Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						while(!transfer.isDone()) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}				
					}        	
		        });
		        t.start();
		        try {
					t.join();
					log.info("Completed file transfer for " + req.getId());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (XMPPException e2) {
				log.log(Level.SEVERE, "Could not send file for id " + req.getId(), e2);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Could not send file for id " + req.getId(), e);
			}
		}
	}

	private void handleQueryResult(ContentStatus cs) {
		log.info("Content status supplied for " + cs.getId());
		// told where to find the file.
		// get file transfer manager to be ready
		CallbackPendingStatusUpdate pr = callbacksPendingStatusUpdate.get(cs.getId());
		if(pr != null && pr.getCallback() != null) {
			log.info("We are retrieving " + cs.getId());
			MD5Hash uid = new MD5Hash(cs.getId());
			log.info("Setup file receiver to accept for " + uid);
			receiver.receiveFile(uid, cs.getDevice(), cs.getOwner(), pr.getCallback());			
			callbacksPendingStatusUpdate.remove(uid);
			
			// send message to destination to ask for file transfer
			log.info("Peer transfer request going to " + cs.getDevice() + " for " + uid);
			PeerTransferRequest msg = new PeerTransferRequest();
			msg.setId(uid);
			msg.setTo(cs.getDevice());
			log.info("Message sent.");
			connection.sendPacket(msg);
		}
	}

	private void handleRegistration(final ContentStatus cs, final boolean alreadyRegistered) {
		updateList.add(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				CallbackPendingStatusUpdate pr = callbacksPendingStatusUpdate.get(cs.getId());
				if(pr != null && pr.getCallback() != null) {												
					if(alreadyRegistered) {
						log.info("Notifying callback that item was already registered!");
						pr.getCallback().contentAlreadyRegistered(new MD5Hash(cs.getId()));
					}else{
						log.info("Notifying callback that item was registered!");
						pr.getCallback().contentSuccessfullyRegistered(new MD5Hash(cs.getId()));	
					}
					callbacksPendingStatusUpdate.remove(cs.getId());
				}else{
					log.log(Level.WARNING, "No callback for content registration: " + cs.getId());
				}
				return null;
			}
		});
	}

	public void fileTransferComplete(final MD5Hash uid, final URL file, final IFileStatusCallback cback) {
		updateList.add(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				cback.contentReceived(uid, file, false);
				return null;
			}
		});
	}
		
	@Override
	public void update() {
		for(Callable<Void> c : updateList) {
			try {
				c.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			updateList.remove(c);
		}
		// check to see if anything has timed out
		processTimedOut();
	}

	private void processTimedOut() {
		long now = System.currentTimeMillis();
		
		// build up a list of items that HAVE timed out
		List<String> idsForDeletion = new ArrayList<String>();
		for(String id : callbacksPendingStatusUpdate.keySet()) {
			CallbackPendingStatusUpdate cb = callbacksPendingStatusUpdate.get(id);
			if((now - cb.getRegistrationTime()) > cb.getTimeoutMillis()) {
				idsForDeletion.add(id);
			}
		}
		
		// now deal with that list safely
		for(String id : idsForDeletion) {
			CallbackPendingStatusUpdate cb = callbacksPendingStatusUpdate.remove(id);
			if(cb.getType() == CallbackPendingStatusUpdate.REGISTRATION) {				
				cb.getCallback().contentRegistrationError(cb.getFile(), new Exception("Timeout. Is server running? Is network down?"));
			}else if(cb.getType() == CallbackPendingStatusUpdate.RETRIEVAL) {
				cb.getCallback().contentNotReceived(cb.getId());
			}
		}
	}

}
