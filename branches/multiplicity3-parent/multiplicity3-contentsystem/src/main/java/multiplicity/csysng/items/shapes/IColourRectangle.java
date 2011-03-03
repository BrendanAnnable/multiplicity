package multiplicity.csysng.items.shapes;

import com.jme3.math.ColorRGBA;

import multiplicity.csysng.gfx.Gradient;

public interface IColourRectangle extends IRectangularItem {
	public void setGradientBackground(Gradient g);
	public void setSolidBackgroundColour(ColorRGBA colorRGBA);
}
