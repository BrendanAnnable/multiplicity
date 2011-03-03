package multiplicity.csysng.items.keyboard;

import java.awt.Graphics2D;

import multiplicity.csysng.items.keyboard.behaviour.IMultiTouchKeyboardListener;

public interface IKeyboardGraphicsRenderer extends IMultiTouchKeyboardListener {
	public void drawKeyboard(Graphics2D g2d, boolean shiftDown, boolean altDown, boolean ctlDown);
}
