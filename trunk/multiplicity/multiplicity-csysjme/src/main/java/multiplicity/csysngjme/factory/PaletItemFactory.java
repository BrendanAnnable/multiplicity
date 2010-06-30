package multiplicity.csysngjme.factory;

import java.util.UUID;

import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysngjme.items.JMEPalet;

public class PaletItemFactory implements IPaletFactory {

	@Override
	public IPalet createPaletItem(String name, UUID uuid, int width, int height) {
		JMEPalet pi = new JMEPalet(name, uuid, width, height);
        pi.initializeGeometry();
        return pi;
	}

}
