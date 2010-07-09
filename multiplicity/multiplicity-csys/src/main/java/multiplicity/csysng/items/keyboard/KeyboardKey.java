package multiplicity.csysng.items.keyboard;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class KeyboardKey {
	private String string_rep = "";
	private int keyCode;
	private Shape keyShape;
	private boolean modifierFlag = false;
	
	public KeyboardKey(String string, int keyCode, Shape keyShape) {
		this.string_rep = string;
		this.setKeyCode(keyCode);
		this.setKeyShape(keyShape);
	}

	public String getKeyStringRepresentation() {
		return string_rep;
	}
	
	public String toString() {
		return string_rep + "(" + getKeyCode() + ")";
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyShape(Shape keyShape) {
		this.keyShape = keyShape;
	}

	public Shape getKeyShape() {
		return keyShape;
	}
	
	public Rectangle2D getBounds() {
		return keyShape.getBounds2D();
	}
	
	public int getMinX() {
		return (int) keyShape.getBounds2D().getMinX();	
	}
	
	public int getMinY() {
		return (int) keyShape.getBounds2D().getMinY();	
	}
	
	public int getMaxX() {
		return (int) keyShape.getBounds2D().getMaxX();
	}
	
	public int getMaxY() {
		return (int) keyShape.getBounds2D().getMaxY();
	}

	public void setModifier(boolean modifierFlag) {
		this.modifierFlag = modifierFlag;
	}

	public boolean isModifier() {
		return modifierFlag;
	}
}
