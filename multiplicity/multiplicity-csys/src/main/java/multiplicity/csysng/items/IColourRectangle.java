package multiplicity.csysng.items;

import java.awt.Color;

import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.gfx.Gradient;

public interface IColourRectangle extends IRectangularItem {
	public void setSolidBackgroundColour(Color c);
	public void setGradientBackground(Gradient g);
	public void setSolidBackgroundColour(ColorRGBA colorRGBA);
	public void initializeGeometry();
}
