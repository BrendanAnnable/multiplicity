package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.items.IPalet;

public class JMEPalet extends JMEColourRectangle implements IPalet {

	private static final long serialVersionUID = 8754761957677002270L;

	public JMEPalet(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}
	
	@Override
	public void initializeGeometry() {
		super.initializeGeometry();		
	}

}
