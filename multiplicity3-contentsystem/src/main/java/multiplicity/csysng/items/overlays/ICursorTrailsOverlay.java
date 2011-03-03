package multiplicity.csysng.items.overlays;

import java.awt.Color;

import multiplicity.csysng.items.item.IItem;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;

public interface ICursorTrailsOverlay extends IItem, IMultiTouchEventListener {
	public void respondToItem(IItem item);
	public void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer);
	public void setSolidColour(Color c);
	public void setFadingColour(Color c);
}
