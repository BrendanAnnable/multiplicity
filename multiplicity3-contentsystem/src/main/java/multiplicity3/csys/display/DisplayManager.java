package multiplicity3.csys.display;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class DisplayManager {
	
	int width;
	int height;
	
	public void setDisplayDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void tableToScreen(float x, float y, Vector3f store) {
		store.x = width * x;
		store.y = (height * y);
	}
	
	public void tableToScreen(float x, float y, Vector2f store) {		
		store.x = width * x;
		store.y = (height * y);
	}

	public void tableToScreen(Vector2f in, Vector2f out) {
		out.x = width * in.x;
		out.y = (height * in.y);		
	}

	public void tableToScreen(Vector2f position, Vector3f out) {
		out.x = width * position.x;
		out.y = (height * position.y);
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
