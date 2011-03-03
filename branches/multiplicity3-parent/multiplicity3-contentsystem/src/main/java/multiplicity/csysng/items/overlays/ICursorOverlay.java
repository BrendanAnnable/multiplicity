package multiplicity.csysng.items.overlays;

import multiplicity.csysng.items.item.IItem;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchEventProducer;

public interface ICursorOverlay extends IItem, IMultiTouchEventListener {
	void respondToItem(IItem item);
	void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer);
}
