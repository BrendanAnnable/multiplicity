package multiplicity.csysngjme.factory;

import java.util.UUID;

import multiplicity.csysng.factory.IContentFactory;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.threedee.IThreeDeeContent;
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMECursorTrails;
import multiplicity.csysngjme.items.JMEDiskCursorOverlay;
import multiplicity.csysngjme.items.JMEEditableText;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMEKeyboard;
import multiplicity.csysngjme.items.JMELabel;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.threedee.JMEThreeDeeContent;


public class JMEContentItemFactory extends ContentItemFactoryUtil implements IContentFactory {

	public JMEContentItemFactory() {
	}
	
	@Override
	public IFrame createFrame(String name, UUID uuid, float width, float height) {
		JMEFrame frame = new JMEFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
		frame.initializeGeometry();
		return frame;
	}

	@Override
	public IBorder createRoundedBorder(String name, UUID uuid, float width, float height, float borderSize, int cornerDivisions) {
		JMERoundedRectangleBorder rrb = new JMERoundedRectangleBorder(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height, borderSize, cornerDivisions);
		rrb.initializeGeometry();
		return rrb;
	}

	@Override
	public IImage createImage(String name, UUID uuid) {
		JMEImage image = new JMEImage(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid));
		image.initializeGeometry();
		return image;
	}

	@Override
	public ILabel createLabel(String name, UUID uuid) {
		JMELabel label = new JMELabel(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid));
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

	@Override
	public IEditableText createEditableText(String name, UUID uuid) {
		JMEEditableText text = new JMEEditableText(name, uuid);
		text.initializeGeometry();
		return text;
	}
	
	@Override
	public IKeyboard createKeyboard(String name, UUID uuid) {
		JMEKeyboard kb = new JMEKeyboard(name, uuid);
		kb.initializeGeometry();
		return kb;
	}

	@Override
	public IThreeDeeContent createThreeDeeContent(String name, UUID uuid) {
		return new JMEThreeDeeContent(name, uuid);
	}

	@Override
	public IBorder createRoundedRectangleBorder(String name, UUID uuid, float width, int cornerSegments) {
		return new JMERoundedRectangleBorder(name, uuid, width, cornerSegments);
	}
	
}
