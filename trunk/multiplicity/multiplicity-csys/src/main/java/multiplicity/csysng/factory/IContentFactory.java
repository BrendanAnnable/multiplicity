package multiplicity.csysng.factory;

import java.util.UUID;

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

/**
 * Factory (see Factory pattern) for creating items in the content system.
 * 
 * @author dcs0ah1
 *
 */
public interface IContentFactory {
	public IFrame createFrame(String name, UUID uuid, float width, float height);
	public IBorder createRoundedBorder(String name, UUID uuid, float width, float height, float borderSize, int cornerDivisions);
	public IImage createImage(String name, UUID randomUUID);
	public ILabel createLabel(String name, UUID randomUUID);
	public ICursorOverlay createCursorOverlay(String string, UUID randomUUID);
	public ICursorTrailsOverlay createCursorTrailsOverlay(String string, UUID randomUUID);
	public IColourRectangle createColourRectangle(String string, UUID randomUUID, int width, int height);
	public IEditableText createEditableText(String name, UUID randomUUID);
	public IKeyboard createKeyboard(String string, UUID randomUUID);
	public IThreeDeeContent createThreeDeeContent(String string, UUID randomUUID);
}
