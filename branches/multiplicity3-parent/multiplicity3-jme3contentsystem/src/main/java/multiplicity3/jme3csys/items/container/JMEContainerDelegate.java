package multiplicity3.jme3csys.items.container;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import multiplicity3.csys.items.container.IContainerDelegate;
import multiplicity3.jme3csys.items.item.JMEItemDelegate;

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