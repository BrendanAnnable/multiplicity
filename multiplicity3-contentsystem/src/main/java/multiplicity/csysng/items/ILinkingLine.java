package multiplicity.csysng.items;

import java.awt.Color;

import multiplicity.csysng.items.item.IItem;


/**
 * This line will link two items together such that when those items
 * are updated, the line will be updated too.
 * @author dcs0ah1
 *
 */

public interface ILinkingLine extends IItem {
	public void setSourceItem(IItem item);
	public IItem getSourceItem();
	public void setDestinationItem(IItem item);
	public IItem getDestinationItem();
	public void setLineColour(Color c);
	public void setLineWidth(float width);
	public float getLength();
	public void updateEndPoints();
}
