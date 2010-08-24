package multiplicity.appgallery.stitcher;

import java.awt.Color;
import java.awt.Font;
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
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysngjme.factory.PaletItemFactory;
import multiplicity.csysngjme.factory.Repository.RepositoryContentItemFactory;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.items.hotspots.HotLink;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.jmeutils.UnitConversion;
import no.uio.intermedia.snomobile.XWikiRestFulService;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class StitcherApp extends AbstractMultiplicityApp {

	private final static Logger logger = Logger.getLogger(StitcherApp.class.getName());
	public static final String IMAGE = "IMAGE";
    public static final String TEXT = "TEXT";
    public static final String BACKGROUND = "BACKGROUND";
	public final String MULTIPLICITY_SPACE = "multiplicity";
	public final String STENCIL_NAME = "stencils";
	public final String BACKGROUND_NAME = "backgrounds";
	public final String SCAN_NAME = "scans";
	private final List<String> pageNames = new ArrayList<String>();
	private final List<IHotLink> hotspotConnections = new ArrayList<IHotLink>();
	private IPage stencilsPage;
	private IPage backgroundsPage;
	private IPage scansPage;
	// private Vector<IComment> comments;
	private XWikiRestFulService wikiService;
	private Vector<IAttachment> attachments;
	// private Vector<ITag> tags;
	public String output_document_path = null;
	public String wikiUser = null;
	public String wikiPass = null;
	// public Grouper smaker = null;
	public int maxFileSize = 0;
	private int frameWidth = 600;
	private int frameHeight = 600;

	public final Float BORDER_THICKNESS = 40f;
	public final Float TOP_BOTTOM_REPO_HEIGHT = 300f;
	public final Float RIGHT_LEFT_REPO_HEIGHT = 400f;
	public final int HOTSPOT_DIMENSION = 80;
	public final int HOTSPOT_FRAME_DIMENSION = 200;
	public final int PALET_DIMENSION = 35;
	public final int SMALLER_PALET_DIMENSION = 30;
	public final int MAX_THUMBNAIL_SIDE_SIZE = 150;
	public final float INITIAL_DROP_SCALE = 0.5f;

	// when this is filled the first one is at the top of the z index
	List<IItem> zOrderedItems;

	private IHotSpotContentFactory hotSpotContentFactory;

	private IPaletFactory paletFactory;

	private IRepositoryContentItemFactory repositoryFactory;

	public StitcherApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		setHotSpotContentFactory(new HotSpotContentItemFactory());
		setPaletFactory(new PaletItemFactory());
		setRepositoryFactory(new RepositoryContentItemFactory());
		pageNames.add(STENCIL_NAME);
		pageNames.add(BACKGROUND_NAME);
		pageNames.add(SCAN_NAME);
		HashMap<String, IPage> populateFromWiki = populateFromWiki();
		loadContent(populateFromWiki);

	}

	private HashMap<String, IPage> populateFromWiki() {
		Properties prop = new Properties();
		HashMap<String, IPage> wikiPages = new HashMap<String, IPage>();

		try {
			prop.load(StitcherApp.class.getResourceAsStream("xwiki.properties"));
			this.wikiUser = prop.getProperty("DEFAULT_USER");
			this.wikiPass = prop.getProperty("DEFAULT_PASS");
			this.maxFileSize = Integer.valueOf(prop.getProperty("MAX_ATTCHMENT_SIZE"));
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
        
//        IImage bg = getContentFactory().createImage("backgroundimage",
//                UUID.randomUUID());
//        bg.setImage(StitcherApp.class
//                .getResource("bluebackground1680.png"));
//        bg.centerItem();
//        add(bg);
//        zOrderManager.sendToBottom(bg, null);
//        zOrderManager.neverBringToTop(bg);
	        
        zOrderedItems = new ArrayList<IItem>();
        
        Runnable runnable = new Runnable() {
            
            @Override
            public void run() {
                
                List<IItem> items = null;
                IPage iPage = null;
                GetAttachmentItems getAttachmentItems;
                for (int i = 0; i < wikiPages.size(); i++) {
                    iPage = wikiPages.get(pageNames.get(i));
                    attachments = iPage.getAttachments();
                    items = new ArrayList<IItem>();

                    getAttachmentItems = new GetAttachmentItems(StitcherApp.this, iPage, attachments, items, pageNames.get(i));
                    try {
                        getAttachmentItems.start();
                        getAttachmentItems.join();
                        items = getAttachmentItems.getItems();
                    } catch (InterruptedException e) {
                        logger.debug("GetAttachmentItems:  InterruptedException: " + e);
                    }

                    if (items != null) {
                        addItemsToFrame(getAttachmentItems.getItemsToReturn(), new Vector2f(i * 5f, i * 5f), pageNames.get(i));
                    }
                }
                
            }
        };
        
        runnable.run();
		

		createHotSpotRepo(IMAGE);
		createHotSpotRepo(TEXT);

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());
		add(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
		// trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		add(trails);
	}

    public IHotSpotFrame createNewHotSpotContentFrame(String type) {
		return createNewFrame(null, null, "hotspotf-", type);
	}

	public IHotSpotFrame createNewFrame(IItem backgroundImage,Vector2f atPosition, String frameName, String type) {
	    IHotSpotFrame newHotSpotFrame = null;

	       
	    if( type.equals(BACKGROUND) ) {
	        backgroundImage.setRelativeScale(INITIAL_DROP_SCALE);
	        JMEImage bi = (JMEImage)backgroundImage;

	        // scale image.
	        Vector3f localScale = bi.getLocalScale();

	        float newX = bi.getSize().x * localScale.x;
	        float newY = bi.getSize().y * localScale.y;
	        
	        newHotSpotFrame = this.getHotSpotContentFactory().createHotSpotFrame(frameName,  UUID.randomUUID(), Float.valueOf(newX).intValue(), Float.valueOf(newY).intValue());
	        newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1, 15, new ColorRGBA(0f, 0f, 0f, 0f)));
	        newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
	        newHotSpotFrame.maintainBorderSizeDuringScale();
	        newHotSpotFrame.setRelativeLocation(atPosition);

	        BehaviourMaker.addBehaviour(newHotSpotFrame, RotateTranslateScaleBehaviour.class);
	        this.add(newHotSpotFrame);
	        
            Vector2f itemWorldPos = backgroundImage.getWorldLocation();
            newHotSpotFrame.addItem(backgroundImage);
            backgroundImage.setWorldLocation(itemWorldPos);
            newHotSpotFrame.getZOrderManager().bringToTop(backgroundImage, null);
            BehaviourMaker.removeBehavior(backgroundImage, RotateTranslateScaleBehaviour.class);
            backgroundImage.centerItem();
            // little trick to make sure palet and hotspots are always on top
            backgroundImage.addItemListener(new ItemListenerAdapter() {
                @Override
                public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
                    super.itemCursorPressed(item, event);
                    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                    parentFrame.getZOrderManager().sendToBottom(item, null);
                    if (!parentFrame.isLocked()) {
                        parentFrame.sendOverlayToBottom();
                    }
                    parentFrame.bringHotSpotsToTop();
                    parentFrame.bringPaletToTop();
                 
                }
            });
	    } else if( type.equals(IMAGE) ){
	        
	        UUID randomUUID = UUID.randomUUID();
	        float xPos = Integer.valueOf(-DisplaySystem.getDisplaySystem().getWidth() / 2 + (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();
	        float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();

	        newHotSpotFrame = (IHotSpotFrame) this.getHotSpotContentFactory().createHotSpotFrame(frameName + randomUUID, randomUUID, HOTSPOT_FRAME_DIMENSION, HOTSPOT_FRAME_DIMENSION);

	        newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1, 15, new ColorRGBA(0f, 0f, 0f, 0f)));
	        newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
	        newHotSpotFrame.maintainBorderSizeDuringScale();
	        newHotSpotFrame.setRelativeLocation(new Vector2f(xPos, yPos));

	        BehaviourMaker.addBehaviour(newHotSpotFrame, RotateTranslateScaleBehaviour.class);
	        this.add(newHotSpotFrame);
	    
	    } else if((type != null && type.equals(TEXT))) {
	         
	         final IHotSpotText hotspotLabel = getHotSpotContentFactory().createEditableHotSpotText("HotSpotLabel", UUID.randomUUID());
	         hotspotLabel.setText("Replace Me");
	         hotspotLabel.setFont(new Font("Myriad Pro", Font.BOLD, 48*2));
	         hotspotLabel.setTextColour(Color.white);
	         hotspotLabel.setRelativeLocation(new Vector2f(0, 200));
	         hotspotLabel.setCursorAt(3);
	         BehaviourMaker.addBehaviour(hotspotLabel, RotateTranslateScaleBehaviour.class);       
	         add(hotspotLabel);
	         
	         hotspotLabel.addItemListener(new ItemListenerAdapter() {

	             
	                @Override
	                public void itemCursorPressed(IItem item,
	                    MultiTouchCursorEvent event) {
	                    super.itemCursorPressed(item, event);
	                    logger.debug("text label itemCursorPressed");
	                    IHotSpotText hst = (IHotSpotText) item;
	                    
	                    
                        if (hst.tap() == 2) {
    
                            if (hst.isKeyboardVisible() == false) {
                                showKeyboard(hst);
                            } else {
                                hideKeyboard(hst);
                            }
                            
                            hst.resetTaps();
    
                        }
	                }

                    @Override
	                public void itemMoved(IItem item) {
	                    super.itemMoved(item);
	                   // redrawContentHotSpotLines(item);
	                }
                    
                    private void showKeyboard(IHotSpotText hotSpotText) {
                       
                        IFrame keyboard = hotSpotText.getKeyboard();
                        add(keyboard);
                        BehaviourMaker.addBehaviour(keyboard, RotateTranslateScaleBehaviour.class);
                        zOrderManager.bringToTop(keyboard, null);
                        hotSpotText.setKeyboardVisible(true);
                        
                    }
                    
                    private void hideKeyboard(IHotSpotText hotSpotText) {
                        IFrame keyboard = hotSpotText.getKeyboard();
                        remove(keyboard);
                        hotSpotText.setKeyboardVisible(false);
                    }
	            });
	         return hotspotLabel;
	     }
	       
	     
	    
	    
	    if(type.equals(BACKGROUND) || type.equals(IMAGE)){
	        
	        newHotSpotFrame.getFrameOverlay().addItemListener(new ItemListenerAdapter(){
	            
	            
	            @Override
	            public void itemCursorPressed(IItem item,
	                    MultiTouchCursorEvent event) {
	                // TODO Auto-generated method stub
	                super.itemCursorPressed(item, event);
	                logger.debug("overlay pressed");
	                IHotSpotFrame hotspotFrame = (IHotSpotFrame)item.getParentItem();
	                hotspotFrame.bringHotSpotsToTop();
                    hotspotFrame.bringPaletToTop();
	            }
	            @Override
	            public void itemMoved(IItem item) {
	                super.itemMoved(item);
	                logger.debug("overlay moved");
	                   IHotSpotFrame hotspotFrame = (IHotSpotFrame)item.getParentItem();
	                   hotspotFrame.bringPaletToTop();
	            }
	            
	            @Override
	            public void itemCursorChanged(IItem item,
	                    MultiTouchCursorEvent event) {
	                // TODO Auto-generated method stub
	                super.itemCursorChanged(item, event);
	                logger.debug("overlay changed");
	                IHotSpotFrame hotspotFrame = (IHotSpotFrame)item.getParentItem();
                    hotspotFrame.updateOverLay();
	            }
	            
	            
	        });
	        
	        //set up the palet
	        IPalet palet = this.getPaletFactory().createPaletItem("palet",  UUID.randomUUID(), PALET_DIMENSION, new ColorRGBA(0f, 1f, 0f, 1f));
	        newHotSpotFrame.addPalet(palet);
	        palet.centerItem();
	        BehaviourMaker.addBehaviour(palet, RotateTranslateScaleBehaviour.class);
	        palet.addItemListener(new ItemListenerAdapter() {
	            
	            @Override
	            public void itemCursorPressed(IItem item,
	                    MultiTouchCursorEvent event) {
	                super.itemCursorPressed(item, event);
	                IPalet palet = (IPalet) item;
	                paletPressed(palet);
	                IHotSpotFrame hotspotFrame = (IHotSpotFrame) item.getParentItem();
	                List<IHotSpotItem> hotSpots = hotspotFrame.getHotSpots();
	                for (IHotSpotItem iHotSpotItem : hotSpots) {
                        getZOrderManager().bringToTop(iHotSpotItem.getHotSpotFrameContent(), null);
                    }
	            }

	            @Override
	            public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
	                 super.itemCursorReleased(item, event);
	                 paletReleased(item, event);
	                 
	            }
	        });
	        
	        if(  type.equals(IMAGE) ) {
	            newHotSpotFrame.addItemListener(new ItemListenerAdapter() {

                    @Override
                    public void itemMoved(IItem item) {
                        super.itemMoved(item);
                        IHotSpotFrame frame = (IHotSpotFrame) item;
                        
                        List<IHotLink> hotLinks = frame.getHotLinks();
                        for (IHotLink iHotLink : hotLinks) {
                            iHotLink.getHotSpotItem().updateHotSpot();
                        }
                        
                       
                    }
                });
	        } else if( type.equals(BACKGROUND)) {
	            newHotSpotFrame.addItemListener(new ItemListenerAdapter() {

	                @Override
	                public void itemMoved(IItem item) {
	                    super.itemMoved(item);
	                    IHotSpotFrame frame = (IHotSpotFrame) item;
	                    
	                    List<IHotSpotItem> hotSpots = frame.getHotSpots();
	                    for (IHotSpotItem iHotSpotItem : hotSpots) {
	                        iHotSpotItem.updateHotSpot();
	                    }
	                    frame.bringPaletToTop();
	                    
	                   
	                }
	            });
	        }

	        
	        
	  
            bumpHotSpotConnections();
	        //getZOrderManager().bringToTop(newHotSpotFrame, null);
	        //newHotSpotFrame.getZOrderManager().updateZOrdering();
	        
	        return newHotSpotFrame;
	    }
	    
   

	    return null;

       
	    
	}
	
	protected void redrawContentHotSpotLines(IHotSpotFrame hotspotFrame){
            List<IHotSpotItem> hotSpots = hotspotFrame.getHotSpots();
            for (IHotSpotItem iHotSpotItem : hotSpots) {
                iHotSpotItem.update(hotspotFrame.getRelativeLocation());
            }
	}

	
    protected void paletReleased(IItem item, MultiTouchCursorEvent event) {
        String message = "Palet released: ";
        logger.debug(message);
        boolean offParent = true;
        
        Vector2f locStore = new Vector2f();
        UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

        List<IItem> items = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);

        IHotSpotFrame paletParent = (IHotSpotFrame) item.getParentItem();

        for (IItem foundItem : items) {
            if (foundItem.getParentItem() != null && foundItem.getParentItem().equals(item.getParentItem())) {
                offParent = false;
                logger.debug("Palet released on its parent");
            }
        }

        if (offParent) {
            item.centerItem();
            logger.debug("Palet in the mist, come back to center");
        }

//        getZOrderManager().sendToBottom(paletParent, event);
        paletParent.bringPaletToTop();
//        paletParent.bringHotSpotsToTop();
//        paletParent.bringPaletToTop();
        
//        bumpHotSpotConnections();
    
       
	}
	protected void paletPressed(IPalet palet) {
	    logger.info("palet pressed");
        int taps = palet.tap();
        logger.info("palet clicked " + taps);
        if (taps == 2) {
            
            palet.resetTaps();
            
            IHotSpotFrame parentFrame = (IHotSpotFrame) palet.getParentItem();
            
            if( parentFrame.isLocked() ) {
                
                logger.info("unlocking palet");

                parentFrame.setLocked(false);

                palet.lockPalet(false);

//                hotspotFrameMove(parentFrame);
                 
            } else {
                logger.info("locking palet");

                parentFrame.setLocked(true);

                palet.lockPalet(true);

//                hotspotFrameMove(parentFrame);
                
                
            }
//            bumpHotSpotConnections();
            parentFrame.bringPaletToTop();
        }
	}
	
	public void addItemsToFrame(Vector<Object> itemsToAdd, Vector2f atPosition, String frameName) {
		Vector2f framePosition = null;
		Vector2f frameClosePosition = null;
		UUID uUID = UUID.randomUUID();

		IRepositoryFrame frame = (IRepositoryFrame) this.getRepositoryFactory().createRepositoryFrame(frameName, uUID, frameWidth, frameHeight);

		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, .9f), new Color(0f, 0f, 0f, .9f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();

		if (frameName.equals(BACKGROUND_NAME)) {
			frame.setSize(DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

			Float xPos = 0f;
			Float yPos = Integer.valueOf((int) (-DisplaySystem.getDisplaySystem().getHeight() / 2 - (TOP_BOTTOM_REPO_HEIGHT / 2 + BORDER_THICKNESS) + BORDER_THICKNESS)).floatValue();
			framePosition = new Vector2f(xPos, yPos);
			frame.setOpenLocation(framePosition);
			frameClosePosition = new Vector2f(framePosition.x, framePosition.y + TOP_BOTTOM_REPO_HEIGHT);
			frame.setCloseLocation(frameClosePosition);
			frame.close();
			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(.211f, .211f, .211f, 1f));
			frame.setBorder(border);
		} else if (frameName.equals(SCAN_NAME)) {
			frame.setSize(DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

			Float xPos = 0f;
			Float yPos = Integer.valueOf((int) (DisplaySystem.getDisplaySystem().getHeight() / 2 + (TOP_BOTTOM_REPO_HEIGHT / 2 + BORDER_THICKNESS) - BORDER_THICKNESS)).floatValue();
			framePosition = new Vector2f(xPos, yPos);
			frame.setOpenLocation(framePosition);
			frameClosePosition = new Vector2f(framePosition.x, framePosition.y - TOP_BOTTOM_REPO_HEIGHT);
			frame.setCloseLocation(frameClosePosition);
			frame.close();
			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(.211f, .211f, .211f, 1f));
			frame.setBorder(border);
		} else if (frameName.equals(STENCIL_NAME)) {
			frame.setSize(RIGHT_LEFT_REPO_HEIGHT, DisplaySystem.getDisplaySystem().getHeight() - 10);

			Float xPos = Integer.valueOf((int) (DisplaySystem.getDisplaySystem().getWidth() / 2 + (RIGHT_LEFT_REPO_HEIGHT / 2 + BORDER_THICKNESS) - BORDER_THICKNESS)).floatValue();
			Float yPos = 0f;
			framePosition = new Vector2f(xPos, yPos);
			frame.setOpenLocation(framePosition);
			frameClosePosition = new Vector2f(framePosition.x - RIGHT_LEFT_REPO_HEIGHT, framePosition.y);
			frame.setCloseLocation(frameClosePosition);
			frame.close();

			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
			frame.setBorder(border);
		}

		for (int i = 0; i < itemsToAdd.size(); i++) {
			@SuppressWarnings("unchecked")
			Vector<Object> itemEntry = (Vector<Object>) itemsToAdd.elementAt(i);

			IImage vecItem = (IImage) itemEntry.elementAt(1);
			float scale = (Float) itemEntry.elementAt(0);
			vecItem.setRelativeScale(scale);
			Vector2f position = generateRandomPosition(frame, vecItem);
			frame.addItem(vecItem);
			vecItem.setRelativeLocation(position);
		}

		this.add(frame);

		this.getZOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}

	public Vector2f generateRandomPosition(IFrame frame, IImage vecItem) {
		Vector2f frameSize = frame.getSize();
		Vector2f imageSize = ((JMERectangularItem) vecItem).getSize();

		float lowerBoundX = -frameSize.x / 2 + imageSize.x / 2;
		float upperBoundX = frameSize.x / 2 - imageSize.x / 2;

		float lowerBoundY = -frameSize.y / 2 + imageSize.y / 2;
		float upperBoundY = frameSize.y / 2 - imageSize.y / 2;

		float posX = (float) (lowerBoundX + (Math.random() * (upperBoundX - lowerBoundX)));
		float posY = (float) (lowerBoundY + (Math.random() * (upperBoundY - lowerBoundY)));

		return new Vector2f(posX, posY);
	}

	public void createHotSpotRepo(String type) {
	    
		UUID uUID = UUID.randomUUID();
		IFrame frame = this.getContentFactory().createFrame("hotspots", uUID, HOTSPOT_DIMENSION, HOTSPOT_DIMENSION);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 5f, 5));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();

		Float xPos = 0f;
		Float yPos = 0f;
		if( type.equals(TEXT) ) {
		    xPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getWidth() / 2 - HOTSPOT_DIMENSION / 2).floatValue();
		    yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - HOTSPOT_DIMENSION / 2).floatValue();
		} else if(type.equals(IMAGE)) {
		    xPos = Double.valueOf(DisplaySystem.getDisplaySystem().getWidth() / 2 - ((HOTSPOT_DIMENSION * 1.5)  +5)).floatValue();
	        yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - HOTSPOT_DIMENSION / 2).floatValue();
		    
		}
		
		
		frame.setRelativeLocation(new Vector2f(xPos, yPos));

		this.add(frame);
		fillHotSpotRepo(frame,type);

		this.getZOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}

	private void fillHotSpotRepo(IFrame frame, String type) {

	    ColorRGBA colorRGBA = null;
	    if( type.equals(IMAGE) ) {
	        colorRGBA = new ColorRGBA(1f, 0f, 0f, 0.6f);
	    } else if(type.equals(TEXT)) {
	        colorRGBA = new ColorRGBA(0f, 0f, 1f, 0.6f);
	    }
		IHotSpotItem hotspot = this.getHotSpotContentFactory().createHotSpotItem("cr", UUID.randomUUID(), HOTSPOT_DIMENSION / 4, colorRGBA);
		hotspot.setType(type);
		frame.addItem(hotspot);
		hotspot.centerItem();

		hotspot.addItemListener(new ItemListenerAdapter() {
	
		    @Override
		    public void itemCursorPressed(IItem item,
		            MultiTouchCursorEvent event) {
		        // TODO Auto-generated method stub
		        super.itemCursorPressed(item, event);
		        logger.debug("Hotspot cursor pressed");
		      
//              bumpHotSpotConnections();

		        if( item.getParentItem() instanceof IHotSpotFrame) {
		            
		            
		            IHotSpotItem hotSpot = (IHotSpotItem) item;
		            getZOrderManager().bringToTop(hotSpot.getHotSpotFrameContent(), null);
		            IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
		            
		            
		             if (!parentFrame.isLocked()) {
	                        parentFrame.sendOverlayToBottom();
	                    }
//		            bumpHotSpotConnections();
		           
		            parentFrame.bringPaletToTop();
		        }
		    }
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
			    super.itemCursorReleased(item, event);
			    logger.debug("Hotspot cursor released");
				String message = "Hotspot released: ";
				boolean offParent = true;
				Node s = (Node)getOrthoNode();

				JMEFrame hotSpotRepo = (JMEFrame) item.getParentItem();
				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

                List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(locStore);

                
				boolean firstFrameFound = false;
				for (IItem foundItem : findItemsOnTableAtPosition) {
					if (foundItem.getParentItem() != null && foundItem.getParentItem().equals(item.getParentItem())) {
						offParent = false;
						message = message + "on its parent. Nothing happens";

//						IHotSpotFrame hsf = (IHotSpotFrame) item.getParentItem();
//						hsf.sendHotLinksToTop();
						
					} else if (foundItem instanceof JMEFrame && !firstFrameFound) {
						try {
							JMEFrame sourceFrame = (JMEFrame)foundItem;

							if ( hotSpotRepo.getName().equals("hotspots") && (sourceFrame.getName().contains("back-") ||  sourceFrame.getName().contains("hotspotf-")) ) {
								firstFrameFound = true;
								IFrame originFrame = (IFrame) item.getParentItem();
								originFrame.removeItem(item);

								Vector2f itemWorldPos = item.getWorldLocation();
								sourceFrame.addItem(item);
								item.setWorldLocation(itemWorldPos);
								sourceFrame.getZOrderManager().bringToTop(item, null);

								// add HS to the array attached to the frame
								IHotSpotItem hsItem = (IHotSpotItem)item;
								((IHotSpotFrame) sourceFrame).addHotSpot(item);

								IHotSpotFrame hotSpotFrameContent = createNewHotSpotContentFrame(hsItem.getType());

								hsItem.setHotSpotFrameContent(hotSpotFrameContent);

								IHotLink l = (HotLink) hsItem.createHotLink();
								l.setSourceFrame((IHotSpotFrame) sourceFrame);
								l.setTargetFrame(hotSpotFrameContent);
								hotspotConnections.add(l);
								add(l);

								message = message + "on " + sourceFrame.getName() + ". Great!!";

								// create a new hotspot candidate
								fillHotSpotRepo(originFrame,hsItem.getType());
								 
								//hotSpotFrameContent.bringHotSpotsToTop();
								bumpHotSpotConnections();
								//hotSpotFrameContent.bringPaletToTop();
							}

						} catch (Exception e) {
							logger.debug("Stitcher app: itemCursorReleased: Exception: " + e);
						}
					}
				}

				if (!firstFrameFound && offParent) {
					item.centerItem();
					IHotSpotFrame hsFrame = (IHotSpotFrame) item.getParentItem();
					
					 
					message = message + "in the mist .... Let's place it back to the center of its mother frame.";
				}

				logger.info(message);
//				 bumpHotSpotConnections();

			}
          
			
		});

		BehaviourMaker.addBehaviour((IItem) hotspot, RotateTranslateScaleBehaviour.class);
	}

	public void bumpHotSpotConnections() {
		for (IHotLink hotlink : hotspotConnections) {
			this.getZOrderManager().bringToTop(hotlink, null);
		}
	}


	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(StitcherApp.class);
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
}
