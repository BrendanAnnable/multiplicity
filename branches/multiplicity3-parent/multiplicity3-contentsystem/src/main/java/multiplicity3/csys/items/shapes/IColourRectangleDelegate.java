package multiplicity3.csys.items.shapes;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.items.item.IItemDelegate;

public interface IColourRectangleDelegate extends IItemDelegate {

	void setSize(float width, float height);
	void setGradientBackground(Gradient g);
	void setSolidBackgroundColour(ColorRGBA colorRGBA);

}
