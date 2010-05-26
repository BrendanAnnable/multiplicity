package multiplicity.csysng.items;

import java.awt.Color;

import multiplicity.csysng.gfx.Gradient;

public interface IColourRectangle extends IRectangularItem {
	public void setSolidBackgroundColour(Color c);
	public void setGradientBackground(Gradient g);
}
