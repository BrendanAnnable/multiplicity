package multiplicity.networkbase.contentdistribution;

import java.io.File;

import multiplicity.networkbase.model.MD5Hash;

/**
 * 
 * @author ashatch
 *
 */
public interface IContentDistributionManager {
	/**
	 * Copies the resource to the content distribution system server.
	 * Returns immediately. {@link IContentStatusCallback} is used to notify if and when
	 * the file is registered and the status of such registration. Returns
	 * a unique identity (instance of {@link MD5Hash}) to refer to the content. 
	 * 
	 * @param f
	 * @return A unique id for this content item.
	 */
	public MD5Hash registerContent(File f, IContentStatusCallback callback);
	
	/**
	 * Attempts to get the content associated with the provided identity ({@link MD5Hash}).
	 * Status changes of the retrieval are notified through {@link IContentStatusCallback}.
	 * @param url
	 * @param intoDirectory
	 * @return
	 */
	void retrieveContent(MD5Hash contentID, IContentStatusCallback cback);
}
