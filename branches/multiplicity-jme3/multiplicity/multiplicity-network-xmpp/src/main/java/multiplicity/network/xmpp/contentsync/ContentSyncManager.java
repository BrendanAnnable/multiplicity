package multiplicity.network.xmpp.contentsync;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.INode;
import multiplicity.csysng.items.INode.IChildrenChangedListener;
import multiplicity.networkbase.contentsync.IContentSynchronisationManager;

public class ContentSyncManager implements IContentSynchronisationManager, IChildrenChangedListener {

	@SuppressWarnings("unused")
	private XMPPConnection connection;

	public ContentSyncManager(XMPPConnection connection) {
		this.connection = connection;
	}

	@Override
	public void setRoot(INode rootNode) {
		rootNode.registerChildrenChangedListener(this);
	}

	@Override
	public void childrenChanged(INode node, List<IItem> list) {

		
	}

}
