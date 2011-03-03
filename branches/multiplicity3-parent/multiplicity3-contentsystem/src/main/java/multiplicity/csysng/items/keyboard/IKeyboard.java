package multiplicity.csysng.items.keyboard;

import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;
import multiplicity.csysng.items.shapes.IRectangularItem;

public interface IKeyboard extends IRectangularItem {
	public void setKeyboardDefinition(KeyboardDefinition kd);
	public KeyboardDefinition getKeyboardDefinition();
	public void setKeyboardRenderer(IKeyboardGraphicsRenderer simpleAlphaKeyboardRenderer);
	public void reDrawKeyboard(boolean shiftDown, boolean altDown, boolean ctlDown);
	public void reDraw();
}
