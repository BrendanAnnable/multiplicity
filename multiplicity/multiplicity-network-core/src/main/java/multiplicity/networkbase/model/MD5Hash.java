package multiplicity.networkbase.model;

import java.io.Serializable;

/**
 * An immutable representation of an MD5 hash.
 * @author ashatch
 *
 */
public class MD5Hash implements Serializable {
	private static final long serialVersionUID = -2857030640571164229L;
	
	private String hashString;
	
	/**
	 * Creates an immutable representation of an MD5 hash
	 * based on the 32-character string supplied. Will
	 * throw an <code>IllegalArgumentException</code>
	 * if the supplied string is not 32 characters long.
	 * Provides expected behaviour for <code>equals()</code>
	 * and <code>hashCode()</code>
	 * @param s
	 */
	public MD5Hash(String s) {
		if(s.length() != 32) throw new IllegalArgumentException("Hash must have 32 characters");
		hashString = s;
	}
	
	/**
	 * Returns the underlying string representation.
	 */
	public String toString() {
		return hashString;
	}
	
	/**
	 * Returns the <code>hashCode</code> of the underlying
	 * string representation.
	 */
	public int hashCode() {
		return hashString.hashCode();
	}
	
	/**
	 * Returns true iff the supplied object is an
	 * instanceof MD5Hash and has the same
	 * <code>hashCode()</code>.
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof MD5Hash)) return false;
		return obj.hashCode() == hashCode();
	}
}
