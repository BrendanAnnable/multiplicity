package multiplicity3.csys.items.keyboard;

import multiplicity3.csys.items.item.IItemDelegate;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;

public interface IKeyboardDelegate extends IItemDelegate {

	void setSize(float width, float height);

	void reDrawKeyboard(boolean shiftDown, boolean altDown, boolean ctlDown);

	void setKeyboardDefinition(KeyboardDefinition kd);

	void setKeyboardRenderer(IKeyboardGraphicsRenderer keyboardRenderer);

}
