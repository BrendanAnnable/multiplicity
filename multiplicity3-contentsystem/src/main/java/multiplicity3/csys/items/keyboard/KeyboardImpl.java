package multiplicity3.csys.items.keyboard;

import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.ItemImpl;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;

public class KeyboardImpl extends ItemImpl implements IKeyboard {
	
	private Vector2f size = new Vector2f();
	private IKeyboardDelegate keyboardDelegate;
	private KeyboardDefinition definition;
//	private IKeyboardGraphicsRenderer renderer;

	public KeyboardImpl(String name, UUID uuid) {
		super(name, uuid);
	}
	
	public void setDelegate(IKeyboardDelegate delegate) {
		super.setDelegate(delegate);
		this.keyboardDelegate = delegate;
	}

	@Override
	public void setSize(float width, float height) {
		this.size.set(width, height);
		keyboardDelegate.setSize(width, height);
	}

	@Override
	public void setSize(Vector2f size) {
		this.size = size;
		keyboardDelegate.setSize(size.x, size.y);
	}

	@Override
	public Vector2f getSize() {
		return size;
	}

	@Override
	public float getWidth() {
		return size.x;
	}

	@Override
	public float getHeight() {
		return size.y;
	}

	@Override
	public void setKeyboardDefinition(KeyboardDefinition kd) {
		this.definition = kd;	
		keyboardDelegate.setKeyboardDefinition(kd);
	}

	@Override
	public KeyboardDefinition getKeyboardDefinition() {
		return this.definition;
	}

	@Override
	public void setKeyboardRenderer(
			IKeyboardGraphicsRenderer keyboardRenderer) {
//		this.renderer = keyboardRenderer;
		keyboardDelegate.setKeyboardRenderer(keyboardRenderer);
	}

	@Override
	public void reDrawKeyboard(boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		keyboardDelegate.reDrawKeyboard(shiftDown, altDown, ctlDown);
	}

	@Override
	public void reDraw() {
		reDrawKeyboard(false, false, false);
	}

}
