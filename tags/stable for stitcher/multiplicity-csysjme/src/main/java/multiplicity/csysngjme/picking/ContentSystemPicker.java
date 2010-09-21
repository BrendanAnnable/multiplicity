package multiplicity.csysngjme.picking;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.picksystem.IPickSystem;

public class ContentSystemPicker implements IPickSystem {

	private Node rootNode;

	public ContentSystemPicker(Node rootOrthoNode) {
		this.rootNode = rootOrthoNode;
	}
	
	@Override
	public List<IItem> findItemsOnTableAtPosition(Vector2f pickPoint) {
		List<IItem> items = new ArrayList<IItem>();
		List<PickedSpatial> list = AccuratePickingUtility.pickAllOrthogonal(rootNode, pickPoint);
		for(PickedSpatial ps : list) {
			Spatial pickableSpatial = ps.getSpatial();
			Spatial parent = pickableSpatial.getParent();
			if(parent instanceof IItem) {
				items.add((IItem)parent);
			}
		}
		
		return items;
	}

}
