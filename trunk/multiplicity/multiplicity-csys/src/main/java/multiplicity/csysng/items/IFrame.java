package multiplicity.csysng.items;

import java.awt.Color;

import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.events.IItemListener;

public interface IFrame extends IItem {
	public void setSize(float width, float height);
	public void setBorder(IBorder b);
	public IBorder getBorder();
	public boolean hasBorder();	
	public void setSolidBackgroundColour(Color c);
	public void setGradientBackground(Gradient g);
	public IItemListener maintainBorderSizeDuringScale();
}
