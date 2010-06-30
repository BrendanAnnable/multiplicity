package multiplicity.csysng.factory;

import java.util.UUID;

import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.items.IPalet;

public interface IPaletFactory {

	public IPalet createPaletItem(String name, UUID uuid, float radius);

	public IPalet createPaletItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA);

}
