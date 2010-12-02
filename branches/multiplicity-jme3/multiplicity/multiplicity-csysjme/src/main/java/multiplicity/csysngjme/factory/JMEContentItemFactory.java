package multiplicity.csysngjme.factory;

import java.net.URL;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;

import multiplicity.csysng.factory.IContentFactory;
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
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMECursorTrails;
import multiplicity.csysngjme.items.JMEDiskCursorOverlay;
import multiplicity.csysngjme.items.JMEEditableText;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMEKeyboard;
import multiplicity.csysngjme.items.JMELabel;
import multiplicity.csysngjme.items.JMELinkingLine;
import multiplicity.csysngjme.items.JMEPalet;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.csysngjme.items.hotspots.HotSpotItem;
import multiplicity.csysngjme.items.hotspots.HotSpotItemImage;
import multiplicity.csysngjme.items.hotspots.HotSpotRepo;
import multiplicity.csysngjme.items.hotspots.HotSpotText;
import multiplicity.csysngjme.items.hotspots.HotSpotTextFrame;
import multiplicity.csysngjme.items.repository.BackgroundRepositoryFrame;
import multiplicity.csysngjme.items.repository.ImageRepository;
import multiplicity.csysngjme.items.repository.RepositoryFrame;
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
	
	@Override
	public IBorder createRoundedRectangleBorder(String name, UUID uuid, float width, int cornerSegments, ColorRGBA colour) {
		return new JMERoundedRectangleBorder(name, UUID.randomUUID(), width, cornerSegments, colour);
	}

	@Override
	public ILinkingLine createLinkingLine(String name, UUID uuid) {
		JMELinkingLine line = new JMELinkingLine(name, uuid);
		line.initializeGeometry();
		return line;
	}
	
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
	
    @Override
    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width, int height) {
        IRepositoryFrame frame = new RepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IRepositoryFrame createRepositoryFrame(String name, UUID uuid, int width, int height, Vector2f openLocation, Vector2f closeLocation) {
       IRepositoryFrame frame = new RepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height, openLocation, closeLocation);
       frame.initializeGeometry();
       return frame;
    }
    
    @Override
    public IBackgroundRepositoryFrame createBackgroundRepositoryFrame(
            String name, UUID uuid, int width, int height,  Vector2f openLocation, Vector2f closeLocation) {
        IBackgroundRepositoryFrame frame = new BackgroundRepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height, openLocation, closeLocation);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IBackgroundRepositoryFrame createBackgroundRepositoryFrame(
            String name, UUID uuid, int width, int height) {
        IBackgroundRepositoryFrame frame = new BackgroundRepositoryFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }
   
    @Override
    public IImageRepositoryFrame createImageRepositoryFrame(
            String name, UUID uuid, int width, int height,  Vector2f openLocation, Vector2f closeLocation) {
        IImageRepositoryFrame frame = new ImageRepository(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height, openLocation, closeLocation);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IImageRepositoryFrame createImageRepositoryFrame(
            String name, UUID uuid, int width, int height) {
        IImageRepositoryFrame frame = new ImageRepository(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }
    
    @Override
    public IHotSpotFrame createHotSpotFrame(String name, UUID uuid, int width, int height) {
        HotSpotFrame frame = new HotSpotFrame(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        frame.addFrameOverlay();
        return frame;
    }

	@Override
	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius) {
		IHotSpotItem pi = new HotSpotItem(name, uuid, radius);
        pi.initializeGeometry();
        return pi;
	}

	@Override
	public IHotSpotItem createHotSpotItem(String name, UUID uuid, float radius, ColorRGBA colorRGBA) {
		IHotSpotItem pi = new HotSpotItem(name, uuid, radius, colorRGBA);
        pi.initializeGeometry();
        return pi;
	}

    @Override
    public IHotSpotItem createHotSpotItemImage(String name, UUID uuid,
             URL imageResource) {
        IHotSpotItem pi = new HotSpotItemImage(name, uuid, imageResource);
        pi.initializeGeometry();
        return pi;
    }
    

    @Override
    public IHotSpotText createEditableHotSpotText(String name, UUID uuid) {
        IHotSpotText text = new HotSpotText(name, uuid);
        text.initializeGeometry();
        return text;
    }

    @Override
    public IHotSpotRepo createHotSpotRepo(String name, UUID uuid, int width,
            int height) {
        IHotSpotRepo frame =  new HotSpotRepo(ContentItemFactoryUtil.validateName(name), ContentItemFactoryUtil.validateUUID(uuid), width, height);
        frame.initializeGeometry();
        return frame;
    }

    @Override
    public IHotSpotFrame createEditableHotSpotTextFrame(String name, UUID uuid, int width, int height,URL keyboardImage) {
        HotSpotTextFrame text = new HotSpotTextFrame(name, uuid, width, height,keyboardImage);
        text.initializeGeometry();
//        text.addFrameOverlay();
        return text;
    }

}
