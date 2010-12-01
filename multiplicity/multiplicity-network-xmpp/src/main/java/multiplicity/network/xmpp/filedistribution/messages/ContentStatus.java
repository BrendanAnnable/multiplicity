package multiplicity.network.xmpp.filedistribution.messages;

import org.jivesoftware.smack.packet.IQ;

public class ContentStatus extends IQ {
	
	private String id;
	private String filename;
	private String device;
	private String status;
	private String owner;

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}

	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return device;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

}
