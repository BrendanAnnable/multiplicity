package multiplicity.networkbase.contentdistribution;

import java.io.File;
import java.net.URL;

import multiplicity.networkbase.model.MD5Hash;

/**
 * When registering or retrieving content through an {@link IContentDistributionManager},
 * changes in status are reported through this interface.
 * @author dcs0ah1
 *
 */
public interface IContentStatusCallback {
	
	/**
	 * The content item requested to be registered was
	 * successfully registered.
	 * @param uuid
	 */
	public void contentSuccessfullyRegistered(MD5Hash uuid);
	
	/**
	 * The content item requested to be registered was already
	 * registered.
	 * @param uuid
	 */
	public void contentAlreadyRegistered(MD5Hash uuid);
	
	/**
	 * Called when some error was encountered at the server. If an
	 * exception was reported, this will be provided.
	 * @param f
	 * @param ex
	 */
	public void contentRegistrationError(File f, Exception ex);
	
	/**
	 * Content was succesfully retrieved, and is available at the
	 * provided URL. If the content was retrieved locally (e.g. by
	 * some file cache), then the <code>wasLocal</code> flag will be <code>true</code>.
	 * @param contentID
	 * @param url
	 * @param wasLocal
	 */
	public void contentReceived(MD5Hash contentID, URL url, boolean wasLocal);
		
	/**
	 * Called if there was a problem retrieving the requested
	 * content.
	 * @param contentID
	 */
	public void contentNotReceived(MD5Hash contentID);
	
	
}
