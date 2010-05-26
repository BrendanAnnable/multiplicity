package multiplicity.csysngjme.utils;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import multiplicity.csysng.gfx.ColourUtils;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IItem;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Quad;

public class JMEUtils {
	private static Logger logger;

	public static void applyGradientToQuad(Quad q, Gradient g) {
		FloatBuffer colorBuf = q.getColorBuffer();
		colorBuf.rewind();
		ColorRGBA from = ColourUtils.colourConvert(g.getFrom());
		ColorRGBA to = ColourUtils.colourConvert(g.getTo());
		if (g.getDirection() == GradientDirection.VERTICAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // top
																		// left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // bottom left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // bottom right
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // top
																		// right
		} else if (g.getDirection() == GradientDirection.HORIZONTAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // top
																		// left
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // bottom
																		// left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // bottom right
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // top right
		} else if (g.getDirection() == GradientDirection.DIAGONAL) {
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // top
																		// left
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // bottom left
			colorBuf.put(from.r).put(from.g).put(from.b).put(from.a); // bottom
																		// right
			colorBuf.put(to.r).put(to.g).put(to.b).put(to.a); // top right
		}

		colorBuf.flip();
	}

	public static void dumpItemToConsole(IItem item, Class clazz) {

		logger = Logger.getLogger(clazz.getName());
		logger.info("Item zorder: " + item.getZOrderManager().getItemZOrder());
		if (item.getManipulableSpatial() != null) {
			logger.info("item spatial zorder " + item.getManipulableSpatial().getZOrder());
		}

		if (item.hasChildren())
			dumpItemToConsole(item, clazz);

	}

}
