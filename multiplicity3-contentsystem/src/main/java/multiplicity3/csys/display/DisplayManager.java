package multiplicity3.csys.display;

import com.jme3.math.Vector2f;

public class DisplayManager {
	
	int width;
	int height;
	
	public void setDisplayDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void tableToScreen(Vector2f in, Vector2f out) {
		out.x = (width * in.x);
		out.y = (height * in.y);
	}

	public int getDisplayWidth() {
		return width;
	}

	public int getDisplayHeight() {
		return height;
	}

	public float getScreenLeft() {
		return -getDisplayWidth()/2;
	}
	
	public float getScreenRight() {
		return getDisplayWidth()/2;
	}
	
	public float getScreenTop() {
		return getDisplayHeight()/2;
	}
	
	public float getScreenBottom() {
		return -getDisplayHeight()/2;
	}
}
