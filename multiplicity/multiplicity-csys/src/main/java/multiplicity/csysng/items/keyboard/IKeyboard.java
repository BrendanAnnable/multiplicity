package multiplicity.csysng.items.keyboard;

import multiplicity.csysng.items.IRectangularItem;

public interface IKeyboard extends IRectangularItem {
	public void setKeyboardDefinition(KeyboardDefinition kd);
	public KeyboardDefinition getKeyboardDefinition();
	public void setKeyboardRenderer(IKeyboardRenderer simpleAlphaKeyboardRenderer);
	public void reDrawKeyboard();
}
