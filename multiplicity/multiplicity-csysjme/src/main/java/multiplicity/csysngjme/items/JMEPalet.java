package multiplicity.csysngjme.items;

import java.awt.Color;
import java.util.UUID;

import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.items.IPalet;

public class JMEPalet extends JMEColourCircle implements IPalet {

	private static final long serialVersionUID = 8754761957677002270L;
	
	public JMEPalet(String name, UUID uuid, float radius) {
		super(name, uuid, radius);
	}

	public JMEPalet(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		super(name, uuid, radius, colorRGBA);
		
	}

	private void addCloseButton() {
		int width = (int) this.getSize().x/2;
		int height = (int) this.getSize().y+40;
		JMEColourRectangle jMEColourRectangle = new JMEColourRectangle("lock",UUID.randomUUID(), width , height);
		jMEColourRectangle.initializeGeometry();
		jMEColourRectangle.setSolidBackgroundColour(new Color(255,0,0));
		this.addItem(jMEColourRectangle);
		this.getZOrderManager().sendToBottom(jMEColourRectangle, null);
	}

	@Override
	public void initializeGeometry() {
		super.initializeGeometry();		
	}

	@Override
	public void updatePalet(boolean locked) {
		if(locked) {
			super.changePalet(new ColorRGBA(0f, 0f, 0f, 1f));
		}
		else {
			super.changePalet(new ColorRGBA(0f, 1f, 0f, 1f));
		}
	}
}
