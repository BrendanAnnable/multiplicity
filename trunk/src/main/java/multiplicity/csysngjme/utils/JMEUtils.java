package multiplicity.csysngjme.utils;

import java.nio.FloatBuffer;

import multiplicity.csysng.gfx.ColourUtils;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;

public class JMEUtils {
	public static void applyGradientToQuad(Quad q, Gradient g) {
		FloatBuffer colorBuf = q.getColorBuffer();
		colorBuf.rewind();
		ColorRGBA from = ColourUtils.colourConvert(g.getFrom());
		ColorRGBA to = ColourUtils.colourConvert(g.getTo());
		if(g.getDirection() == GradientDirection.VERTICAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); 	// top left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); 			// bottom left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); 			// bottom right
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a);	// top right
		}else if(g.getDirection() == GradientDirection.HORIZONTAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); 	// top left
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); 	// bottom left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); 			// bottom right
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a);			// top right
		}else if(g.getDirection() == GradientDirection.DIAGONAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); 	// top left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); 			// bottom left
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); 			// bottom right
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a);			// top right
		}

		colorBuf.flip();
	}
}
