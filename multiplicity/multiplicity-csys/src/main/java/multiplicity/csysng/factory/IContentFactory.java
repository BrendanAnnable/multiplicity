package multiplicity.csysng.factory;

import java.util.UUID;

import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.ILinkingLine;
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
	public IImage createImage(String name, UUID uuid);
	public ILabel createLabel(String name, UUID uuid);
	public ICursorOverlay createCursorOverlay(String name, UUID uuid);
	public ICursorTrailsOverlay createCursorTrailsOverlay(String name, UUID uuid);
	public IColourRectangle createColourRectangle(String name, UUID uuid, int width, int height);
	public IEditableText createEditableText(String name, UUID uuid);
	public IKeyboard createKeyboard(String name, UUID uuid);
	public IThreeDeeContent createThreeDeeContent(String name, UUID uuid);
	public IBorder createRoundedRectangleBorder(String name, UUID uuid, float with, int cornerSegments);
	public ILinkingLine createLinkingLine(String name, UUID uuid);
}
