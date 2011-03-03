package multiplicity3.csys.items.item;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

public interface IItemDelegate {
	void setRelativeLocation(Vector2f newLoc);
	void setRelativeRotation(float angle);
	void setRelativeScale(float scale);
	void addItem(IItem item);
	void removeItem(IItem item);
	void setVisible(boolean isVisible);
	Spatial getTreeRootSpatial();
	Spatial getManipulableSpatial();
	void setMaskGeometry(Geometry maskGeometry);
	Geometry getMaskGeometry();
	void setZOrder(int zOrder);
}
