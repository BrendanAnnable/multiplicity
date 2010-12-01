package multiplicity.networkbase.model;

import java.io.Serializable;

/**
 * Immutable representation of a device's identity. Encapsulates
 * some string representation. Supports expected behaviour for
 * <code>equals()</code> and <code>hashCode</code> (see javadoc).
 * @author dcs0ah1
 */
public class DeviceIdentity implements Serializable {
	private static final long serialVersionUID = 9092769596478119095L;
	private String id;
	
	public DeviceIdentity(String uniqueString) {
		this.id = uniqueString;
	}
	
	/**
	 * Gets the underlying string representation.
	 * @return
	 */
	public String getStringRepresentation() {
		return id;
	}
	
	/**
	 * Equivalent to calling <code>hashCode()</code> on the
	 * underlying string representation.
	 */
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * Returns true when compared to another instance of
	 * DeviceIdentity where the underlying string representations
	 * are the same.
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof DeviceIdentity)) return false;
		
		return hashCode() == obj.hashCode();
	}
	
	/**
	 * Returns the string representation.
	 */
	public String toString() {
		return getStringRepresentation();
	}
}
