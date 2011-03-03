package multiplicity.csysjme3.items.stage;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

import multiplicity.csysjme3.items.item.JMEItemDelegate;
import multiplicity.csysng.items.item.IItem;
import multiplicity.csysng.stage.IStageDelegate;

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
