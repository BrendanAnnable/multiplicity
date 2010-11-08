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
	 * 
	 * @param f
	 * @return A unique id for this content item.
	 */
	public MD5Hash registerContent(File f, IFileStatusCallback callback);
	
	/**
	 * Attempts to get the content associated with the provided identity ({@link MD5Hash}).
	 * Status changes of the retrieval are notified through {@link IFileStatusCallback}.
	 * @param url
	 * @param intoDirectory
	 * @return
	 */
	void retrieveContent(MD5Hash contentID, IFileStatusCallback cback);
}
