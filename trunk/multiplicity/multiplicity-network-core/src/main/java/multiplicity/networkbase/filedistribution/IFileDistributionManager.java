package multiplicity.networkbase.filedistribution;

import java.io.File;

import multiplicity.networkbase.model.MD5Hash;

/**
 * 
 * @author ashatch
 *
 */
public interface IFileDistributionManager {
	/**
	 * Copies the resource to the content distribution system server.
	 * Returns immediately. {@link IFileStatusCallback} is used to notify if and when
	 * the file is registered and the status of such registration. Returns
	 * a unique identity (instance of {@link MD5Hash}) to refer to the content.
	 * A timeout
	 * can be set by which a response must be made, otherwise <code>contentRegistrationError</code>
	 * will be called in the supplied callback.
	 * 
	 * @param f
	 * @return A unique id for this content item.
	 */
	public MD5Hash registerContent(File f, IFileStatusCallback callback, long timeoutMillis);
	
	/**
	 * Attempts to get the content associated with the provided identity ({@link MD5Hash}).
	 * Status changes of the retrieval are notified through {@link IFileStatusCallback}.
	 * A timeout
	 * can be set by which the content must be retrieved, otherwise <code>contentNotReceived</code>
	 * will be called in the supplied callback.
	 * @param url
	 * @param intoDirectory
	 * @return
	 */
	public void retrieveContent(MD5Hash contentID, IFileStatusCallback cback, long timeoutMillis);
	
	
	/**
	 * The multiplicity-network system is supportive of applications where callbacks
	 * need to be executed in a particular thread. The update() method is used to make sure
	 * callback methods are called in the thread that calls update().
	 */
	public void update();
}
