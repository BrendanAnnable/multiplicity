package multiplicity.network.xmpp.filedistribution.messages;

import multiplicity.network.xmpp.filedistribution.FileDistributionManager;
import multiplicity.networkbase.model.MD5Hash;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.IQ;

public class PeerTransferRequest extends IQ {
	private MD5Hash md5id;
	
	public PeerTransferRequest() {
		super();
	}
	
	public MD5Hash getId() {
		return md5id;
	}
	
	public void setId(MD5Hash id) {
		this.md5id = id;
	}

	@Override
	public String getChildElementXML() {
		Element content = DocumentFactory.getInstance().createElement("peerxfer", FileDistributionManager.namespace);
		content.addElement("id").addText(md5id.toString());
		return content.asXML();
	}
}
