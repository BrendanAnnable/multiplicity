package multiplicity.csysngjme.items;

import java.awt.Color;
import java.util.UUID;

import multiplicity.csysng.items.IPalet;

import com.jme.renderer.ColorRGBA;

public class JMEPalet extends JMEColourCircle implements IPalet {

	private static final long serialVersionUID = 8754761957677002270L;
	private int taps = 0;
    private ColorRGBA colorRGBA;
	
	public JMEPalet(String name, UUID uuid, float radius) {
		super(name, uuid, radius);
	}

	public JMEPalet(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid, radius, colorRGBA);
		this.colorRGBA = colorRGBA;
		
	}

	@Override
	public void initializeGeometry() {
		super.initializeGeometry();		
	}

	@Override
	public void lockPalet(boolean locked) {
		if(locked) {
			super.changeBackgroundColor(new ColorRGBA(.3f, 3f, 3f, .5f));
		}
		else {
			super.changeBackgroundColor(this.colorRGBA);
		}
	}

    @Override
    public int tap() {
        return ++taps;
    }

    @Override
    public void resetTaps() {
        taps = 0;
    }
}
