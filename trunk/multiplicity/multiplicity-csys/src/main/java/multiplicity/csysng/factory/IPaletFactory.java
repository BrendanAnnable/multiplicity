package multiplicity.csysng.factory;

import java.util.UUID;

import multiplicity.csysng.items.IPalet;

public interface IPaletFactory {

	public IPalet createPaletItem(String name, UUID uuid, int width, int height);
}
