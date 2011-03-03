package multiplicity3.jme3csys.items.stage;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStageDelegate;
import multiplicity3.jme3csys.items.item.JMEItemDelegate;

public class JMEStageDelegate extends JMEItemDelegate implements IStageDelegate {


	public JMEStageDelegate(IItem item) {
		//
	}
	
	

	@Override
	public Spatial getManipulableSpatial() {
		return this;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		// do nothing		
	}
}
