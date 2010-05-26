package multiplicity.csysngjme.factory;

import java.util.UUID;

import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMECursorTrails;
import multiplicity.csysngjme.items.JMEDiskCursorOverlay;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMELabel;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;


public class JMEContentItemFactory implements IContentFactory {

	public JMEContentItemFactory() {
	}
	
	@Override
	public IFrame createFrame(String name, UUID uuid, int width, int height) {
		JMEFrame frame = new JMEFrame(validateName(name), validateUUID(uuid), width, height);
		frame.initializeGeometry();
		return frame;
	}

	private String validateName(String name) {
		if(name == null) return "<empty>";
		return name;
	}

	private UUID validateUUID(UUID uuid) {
		if(uuid == null) return UUID.randomUUID();
		return uuid;
	}

	@Override
	public IBorder createRoundedBorder(String name, UUID uuid, float width, float height, float borderSize, int cornerDivisions) {
		JMERoundedRectangleBorder rrb = new JMERoundedRectangleBorder(validateName(name), validateUUID(uuid), width, height, borderSize, cornerDivisions);
		rrb.initializeGeometry();
		return rrb;
	}

	@Override
	public IImage createImage(String name, UUID uuid) {
		JMEImage image = new JMEImage(validateName(name), validateUUID(uuid));
		image.initializeGeometry();
		return image;
	}

	@Override
	public ILabel createLabel(String name, UUID uuid) {
		JMELabel label = new JMELabel(validateName(name), validateUUID(uuid));
		label.initializeGeometry();
		return label;
	}

	@Override
	public ICursorOverlay createCursorOverlay(String name, UUID randomUUID) {
		JMEDiskCursorOverlay overlay = new JMEDiskCursorOverlay(name, randomUUID, 30);
		overlay.initializeGeometry();
		return overlay;
	}

	@Override
	public ICursorTrailsOverlay createCursorTrailsOverlay(String name, UUID randomUUID) {
		JMECursorTrails overlay = new JMECursorTrails(name, randomUUID, 30);
		overlay.initializeGeometry();
		return overlay;
	}

	@Override
	public IColourRectangle createColourRectangle(String name, UUID uuid, int width, int height) {
		JMEColourRectangle rect = new JMEColourRectangle(name, uuid, width, height);
		rect.initializeGeometry();
		return rect;
	}
}
