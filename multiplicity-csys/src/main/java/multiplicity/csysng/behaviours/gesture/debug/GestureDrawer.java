package multiplicity.csysng.behaviours.gesture.debug;


import java.nio.FloatBuffer;

import multiplicity.csysng.behaviours.gesture.Gesture;


import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Line.Mode;
import com.jme.util.geom.BufferUtils;

public class GestureDrawer extends Node {

	private static final long serialVersionUID = -8960376013092429184L;
	private Line a;
	private Line b;

	public GestureDrawer() {
		super("gesture drawer");
		
		a = new Line("g1");
		a.setLocalTranslation(100, 100, 0);		
		a.setLocalTranslation(200, 100, 0);
		b = new Line("g2");
		
		initLine(a);
		initLine(b);

	}
	
	private void initLine(Line l) {
		l.setLineWidth(3f);
		l.setSolidColor(ColorRGBA.white);
		l.setLocalScale(200f);
		l.setMode(Mode.Connected);
		attachChild(l);		
	}

	public void updateGesturePair(Gesture g1, Gesture g2) {
		blat(g1, a);
		blat(g2, b);
	}
	
	public void blat(Gesture g, Line l) {
		FloatBuffer v = BufferUtils.createFloatBuffer(g.numPoints() * 3);
		for(int i = 0; i < g.numPoints(); i++) {
			Vector2f x = g.getPoint(i);
			v.put(x.x).put(x.y).put(0);
		}
		l.reconstruct(v, null, null, null);
	}
	
}
