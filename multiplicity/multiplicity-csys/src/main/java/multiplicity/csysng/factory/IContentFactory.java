package multiplicity.csysng.factory;

import java.net.URL;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.ILinkingLine;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IImageRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryFrame;
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
	IPalet createPaletItem(String name, UUID uuid, float radius);
	IPalet createPaletItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA);
	IBackgroundRepositoryFrame createBackgroundRepositoryFrame(String name,
			UUID uuid, int width, int height, Vector2f openLocation,
			Vector2f closeLocation);
	IBackgroundRepositoryFrame createBackgroundRepositoryFrame(String name,
			UUID uuid, int width, int height);
	IImageRepositoryFrame createImageRepositoryFrame(String name, UUID uuid,
			int width, int height, Vector2f openLocation, Vector2f closeLocation);
	IImageRepositoryFrame createImageRepositoryFrame(String name, UUID uuid,
			int width, int height);
	IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width,
			int height, Vector2f openLocation, Vector2f closeLocation);
	IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width,
			int height);
	IHotSpotFrame createEditableHotSpotTextFrame(String name, UUID uuid,
			int width, int height, URL keyboardImage);
	IHotSpotRepo createHotSpotRepo(String name, UUID uuid, int width, int height);
	IHotSpotText createEditableHotSpotText(String name, UUID uuid);
	IHotSpotItem createHotSpotItemImage(String name, UUID uuid,
			URL imageResource);
	IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius,
			ColorRGBA colorRGBA);
	IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius);
	IHotSpotFrame createHotSpotFrame(String name, UUID uuid, int width,
			int height);
	IBorder createRoundedRectangleBorder(String name, UUID uuid, float width,
			int cornerSegments, ColorRGBA colour);
}
