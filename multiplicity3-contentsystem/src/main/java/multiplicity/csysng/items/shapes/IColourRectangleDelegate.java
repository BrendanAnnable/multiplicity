package multiplicity.csysng.items.shapes;

import com.jme3.math.ColorRGBA;

import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.item.IItemDelegate;

public interface IColourRectangleDelegate extends IItemDelegate {

	void setSize(float width, float height);
	void setGradientBackground(Gradient g);
	void setSolidBackgroundColour(ColorRGBA colorRGBA);

}
