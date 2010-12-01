package multiplicity.network.xmpp.filedistribution.messages;

import multiplicity.network.xmpp.filedistribution.FileDistributionManager;
import multiplicity.networkbase.model.MD5Hash;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.IQ;

public class QueryContentIQ extends IQ {
	
	private MD5Hash md5id;

	public QueryContentIQ(MD5Hash md5id) {
		super();
		this.setType(Type.GET);
		this.setId(md5id);
	}
	
	@Override
	public String getChildElementXML() {
		Element content = DocumentFactory.getInstance().createElement("whereis", FileDistributionManager.namespace);
		content.addElement("id").addText(md5id.toString());
		return content.asXML();
	}

	public void setId(MD5Hash md5id) {
		this.md5id = md5id;
	}

	public MD5Hash getId() {
		return md5id;
	}
}
