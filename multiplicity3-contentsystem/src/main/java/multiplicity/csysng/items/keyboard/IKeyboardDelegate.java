package multiplicity.csysng.items.keyboard;

import multiplicity.csysng.items.item.IItemDelegate;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;

public interface IKeyboardDelegate extends IItemDelegate {

	void setSize(float width, float height);

	void reDrawKeyboard(boolean shiftDown, boolean altDown, boolean ctlDown);

	void setKeyboardDefinition(KeyboardDefinition kd);

	void setKeyboardRenderer(IKeyboardGraphicsRenderer keyboardRenderer);

}
