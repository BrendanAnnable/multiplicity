package multiplicity.csysjme3.items.container;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import multiplicity.csysjme3.items.item.JMEItemDelegate;
import multiplicity.csysng.items.container.IContainerDelegate;

public class JMEContainerDelegate extends JMEItemDelegate implements IContainerDelegate {
	
	private Node containerNode;

	public JMEContainerDelegate(JMEContainer item) {
//		this.item = item;
		setName(item.getName() + ":" + item.getUUID() + "_" + getClass().getName());
	}

	@Override
	public Spatial getManipulableSpatial() {
		return containerNode;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		containerNode = new Node("container");		
	}

}