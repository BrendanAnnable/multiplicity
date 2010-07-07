package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.items.IPalet;

import com.jme.renderer.ColorRGBA;

public class JMEPalet extends JMEColourCircle implements IPalet {

	private static final long serialVersionUID = 8754761957677002270L;
	private int taps = 0;
	
	public JMEPalet(String name, UUID uuid, float radius) {
		super(name, uuid, radius);
	}

	public JMEPalet(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid, radius, colorRGBA);
		
	}

	@Override
	public void initializeGeometry() {
		super.initializeGeometry();		
	}

	@Override
	public void updatePalet(boolean locked) {
		if(locked) {
			super.changeBackgroundColor(new ColorRGBA(0f, 0f, 0f, 1f));
		}
		else {
			super.changeBackgroundColor(new ColorRGBA(0f, 1f, 0f, 1f));
		}
	}

    @Override
    public int tap() {
        return taps++;
    }

    @Override
    public void resetTaps() {
        taps = 0;
    }
}
