package multiplicity.csysng.items;

import java.awt.Color;

import com.jme3.math.Vector2f;

import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.border.IRoundedBorder;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.item.IItem;

public interface IFrame extends IItem {
	public void setSize(float width, float height);
	public Vector2f getSize();
	public void setBorder(IRoundedBorder b);
	public IRoundedBorder getBorder();
	public boolean hasBorder();	
	public void setSolidBackgroundColour(Color c);
	public void setGradientBackground(Gradient g);
	public IItemListener maintainBorderSizeDuringScale();
	public void removeItem(IItem item);
}
