package multiplicity.csysng.gfx;

import java.awt.Color;

import com.jme3.math.ColorRGBA;

public class ColourUtils {
	public static ColorRGBA colourConvert(Color c) {
		return new ColorRGBA(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
	}
}
