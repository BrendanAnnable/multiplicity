package multiplicity.csysngjme.factory;

import java.util.UUID;

import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysngjme.items.JMEPalet;

public class PaletItemFactory implements IPaletFactory {

	@Override
	public IPalet createPaletItem(String name, UUID uuid, float radius) {
		JMEPalet pi = new JMEPalet(name, uuid, radius);
        pi.initializeGeometry();
        return pi;
	}

	@Override
	public IPalet createPaletItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		JMEPalet pi = new JMEPalet(name, uuid, radius, colorRGBA);
        pi.initializeGeometry();
        return pi;
	}
}
