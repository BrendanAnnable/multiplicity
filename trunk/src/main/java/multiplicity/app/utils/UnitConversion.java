package multiplicity.app.utils;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;

public class UnitConversion {
	public static void tableToScreen(float x, float y, Vector3f store) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		store.x = dw * x;
		store.y = dh - (dh * y);
	}
	
	public static void tableToScreen(float x, float y, Vector2f store) {		
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		store.x = dw * x;
		store.y = dh - (dh * y);
	}

	public static void tableToScreen(Vector2f in, Vector2f out) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		out.x = dw * in.x;
		out.y = dh - (dh * in.y);		
	}

	public static void tableToScreen(Vector2f position, Vector3f out) {
		int dh = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		int dw = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		out.x = dw * position.x;
		out.y = dh - (dh * position.y);
	}
}
