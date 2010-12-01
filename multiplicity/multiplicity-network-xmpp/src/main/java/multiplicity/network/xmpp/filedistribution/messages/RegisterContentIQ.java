package multiplicity.network.xmpp.filedistribution.messages;

import multiplicity.network.xmpp.filedistribution.FileDistributionManager;
import multiplicity.networkbase.model.MD5Hash;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.IQ;

public class RegisterContentIQ extends IQ {
	
	private MD5Hash md5id;
	private String fileName;
	private String localID;

	public RegisterContentIQ(MD5Hash md5id, String fileName, String localID) {
		super();
		this.setType(Type.SET);
		this.md5id = md5id;
		this.fileName = fileName;
		this.localID = localID;
	}

	@Override
	public String getChildElementXML() {
		Element content = DocumentFactory.getInstance().createElement("registercontent", FileDistributionManager.namespace);
		content.addElement("id").addText(md5id.toString());
		content.addElement("filename").addText(fileName);
		content.addElement("device").addText(localID);
		return content.asXML();
	}
}
