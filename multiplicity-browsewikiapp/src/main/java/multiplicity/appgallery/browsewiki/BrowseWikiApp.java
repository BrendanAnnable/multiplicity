package multiplicity.appgallery.browsewiki;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
import multiplicity.appgallery.stitcher.AttachmentFetchThread;
import multiplicity.appgallery.stitcher.IStitcherContants;
import multiplicity.appgallery.stitcher.StitcherUtils;
import multiplicity.appgallery.stitcher.listeners.HotSpotFrameBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotImageBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotItemBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotTextBehavior;
import multiplicity.appgallery.stitcher.listeners.PaletBehavior;
import multiplicity.appgallery.stitcher.listeners.RepositoryBehavior;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardDefinition;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IImageRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysngjme.factory.PaletItemFactory;
import multiplicity.csysngjme.factory.Repository.RepositoryContentItemFactory;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.items.hotspots.listeners.OverlayBehavior;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;
import no.uio.intermedia.snomobile.XWikiRestFulService;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

public class BrowseWikiApp extends AbstractMultiplicityApp implements IStitcherContants {

	
    private final static Logger logger = Logger.getLogger(BrowseWikiApp.class.getName());
	private final List<String> pageNames = new ArrayList<String>();
	private final List<IHotSpotFrame> hotSpotFrames = new ArrayList<IHotSpotFrame>();
	private IPage stencilsPage;
	private IPage backgroundsPage;
	private IPage scansPage;
	// private Vector<IComment> comments;
	private XWikiRestFulService wikiService;
	private Vector<IAttachment> attachments;
	// private Vector<ITag> tags;
	
	

	private IHotSpotContentFactory hotSpotContentFactory;

	private IPaletFactory paletFactory;

	private IRepositoryContentItemFactory repositoryFactory;

	public BrowseWikiApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		setHotSpotContentFactory(new HotSpotContentItemFactory());
		setPaletFactory(new PaletItemFactory());
		setRepositoryFactory(new RepositoryContentItemFactory());
		pageNames.add(STENCIL_REPO_NAME);
		pageNames.add(BACKGROUND_REPO_NAME);
		pageNames.add(SCAN_REPO_NAME);
		HashMap<String, IPage> populateFromWiki = populateFromWiki();
		loadContent(populateFromWiki);

	}

	private HashMap<String, IPage> populateFromWiki() {
		Properties prop = new Properties();
		HashMap<String, IPage> wikiPages = new HashMap<String, IPage>();

		try {
			prop.load(BrowseWikiApp.class.getResourceAsStream("xwiki.properties"));
			StitcherUtils.wikiUser = prop.getProperty("DEFAULT_USER");
			StitcherUtils.wikiPass = prop.getProperty("DEFAULT_PASS");
			StitcherUtils.maxFileSize = Integer.valueOf(prop.getProperty("MAX_ATTCHMENT_SIZE"));
			stencilsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("REPOSITORY_WIKI_SPACE"), prop.getProperty("REPOSITORY_WIKI_SPACE_STENCILS"), false);
			wikiPages.put(pageNames.get(0), stencilsPage);
			backgroundsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_BACKGROUNDS"), false);
			wikiPages.put(pageNames.get(1), backgroundsPage);
			scansPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_SCANS"), false);
			wikiPages.put(pageNames.get(2), scansPage);
		} catch (IOException e) {
			logger.debug("setup:  IOException: " + e);
		}

		return wikiPages;

	}

	private IPage getWikiPage(Properties prop, String wikiName, String spaceName, String pageName, boolean withAttachments) {
		IPage wikiPage = null;
		wikiService = new XWikiRestFulService(prop);
		wikiPage = wikiService.getWikiPage(wikiName, spaceName, pageName, withAttachments);
		return wikiPage;
	}

	private void loadContent(final HashMap<String, IPage> wikiPages) {
        

        Runnable runnable = new Runnable() {
            
            @Override
            public void run() {
                
                List<IItem> items = null;
                IPage iPage = null;
                AttachmentFetchThread getAttachmentItems;
                for (int i = 0; i < wikiPages.size(); i++) {
                    iPage = wikiPages.get(pageNames.get(i));
                    attachments = iPage.getAttachments();
                    items = new ArrayList<IItem>();

                    getAttachmentItems = new AttachmentFetchThread( iPage, attachments, items, pageNames.get(i));
                    try {
                        getAttachmentItems.start();
                        getAttachmentItems.join();
                        items = getAttachmentItems.getItems();
                    } catch (InterruptedException e) {
                        logger.debug("GetAttachmentItems:  InterruptedException: " + e);
                    }

                    if (items != null) {
                        repostoryFactory(getAttachmentItems.getItemsToReturn(), new Vector2f(i * 5f, i * 5f), pageNames.get(i));
                    }
                }
                
            }
        };
        
        runnable.run();
		

		

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());
		add(cursors);
	}

   
	
	public void repostoryFactory(Vector<Object> imagesToAdd, Vector2f atPosition, String frameName) {

	    for (int i = 0; i < imagesToAdd.size(); i++) {
            @SuppressWarnings("unchecked")
            Vector<Object> itemEntry = (Vector<Object>) imagesToAdd.elementAt(i);

            IImage image = (IImage) itemEntry.elementAt(1);
            
            float scale = (Float) itemEntry.elementAt(0);
            image.setRelativeScale(scale);
            Vector2f position = this.generateRandomPosition(image);
            image.setRelativeLocation(position);
            this.add(image);
        }

	}
	
    public static Vector2f generateRandomPosition(IImage vecItem) {
        
        Vector2f frameSize = new Vector2f();
        
        Vector2f imageSize = ((JMERectangularItem) vecItem).getSize();
        
        logger.debug("generate random position.......");

        DisplaySystem displaySystem = DisplaySystem.getDisplaySystem();
        
        int i = 4;
        float lowerBoundX = -displaySystem.getWidth() / i + imageSize.x / i;
        float upperBoundX = displaySystem.getWidth() / i - imageSize.x / i;

        float lowerBoundY = -displaySystem.getHeight() / i + imageSize.y / i;
        float upperBoundY = displaySystem.getHeight() / i - imageSize.y / i;

        float posX = (float) (lowerBoundX + (Math.random() * (upperBoundX - lowerBoundX)));
        float posY = (float) (lowerBoundY + (Math.random() * (upperBoundY - lowerBoundY)));

        Vector2f vector2f = new Vector2f(posX, posY);
        logger.debug("generate random position....... " + vector2f );

        return vector2f;
    }

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(BrowseWikiApp.class);
	}

	public void setHotSpotContentFactory(IHotSpotContentFactory hotSpotContentFactory) {
		this.hotSpotContentFactory = hotSpotContentFactory;
	}

	public IHotSpotContentFactory getHotSpotContentFactory() {
		return hotSpotContentFactory;
	}

	public IPaletFactory getPaletFactory() {
		return paletFactory;
	}

	public void setPaletFactory(IPaletFactory paletFactory) {
		this.paletFactory = paletFactory;
	}

	public void setRepositoryFactory(IRepositoryContentItemFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}

	public IRepositoryContentItemFactory getRepositoryFactory() {
		return repositoryFactory;
	}

    public List<IHotSpotFrame> getHotSpotFrames() {
        return hotSpotFrames;
    }

}
