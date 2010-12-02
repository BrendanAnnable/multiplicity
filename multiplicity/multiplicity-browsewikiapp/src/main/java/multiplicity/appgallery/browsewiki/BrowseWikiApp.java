package multiplicity.appgallery.browsewiki;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IRectangularItem;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.input.IMultiTouchEventProducer;
import no.uio.intermedia.snomobile.XWikiRestFulService;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.system.DisplaySystem;

public class BrowseWikiApp extends AbstractMultiplicityApp implements IStitcherContants {

	
    private final static Logger logger = Logger.getLogger(BrowseWikiApp.class.getName());
	private final List<String> pageNames = new ArrayList<String>();
	private final List<IHotSpotFrame> hotSpotFrames = new ArrayList<IHotSpotFrame>();
	private IPage scansPage;
	// private Vector<IComment> comments;
	private XWikiRestFulService wikiService;
	private Vector<IAttachment> attachments;
	// private Vector<ITag> tags;
	

	public BrowseWikiApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
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
//			stencilsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("REPOSITORY_WIKI_SPACE"), prop.getProperty("REPOSITORY_WIKI_SPACE_STENCILS"), false);
//			wikiPages.put(pageNames.get(0), stencilsPage);
//			backgroundsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_BACKGROUNDS"), false);
//			wikiPages.put(pageNames.get(1), backgroundsPage);
			scansPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_SCANS"), false);
			wikiPages.put(pageNames.get(0), scansPage);
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
		addItem(cursors);
	}

   
	
	public void repostoryFactory(Vector<Object> imagesToAdd, Vector2f atPosition, String frameName) {

	    for (int i = 0; i < imagesToAdd.size(); i++) {
            @SuppressWarnings("unchecked")
            Vector<Object> itemEntry = (Vector<Object>) imagesToAdd.elementAt(i);

            IImage image = (IImage) itemEntry.elementAt(1);
            
            float scale = (Float) itemEntry.elementAt(0);
            image.setRelativeScale(scale);
            Vector2f position = generateRandomPosition(image);
            image.setRelativeLocation(position);
            BehaviourMaker.addBehaviour(image, InertiaBehaviour.class);
            this.addItem(image);
        }

	}
	
    public static Vector2f generateRandomPosition(IImage vecItem) {
        
        Vector2f imageSize = ((IRectangularItem) vecItem).getSize();
        
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

    public List<IHotSpotFrame> getHotSpotFrames() {
        return hotSpotFrames;
    }

}
