package multiplicity.csysng.items.keyboard.behaviour;

import multiplicity.csysng.items.keyboard.model.KeyboardKey;

public interface IMultiTouchKeyboardListener {
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);
}
