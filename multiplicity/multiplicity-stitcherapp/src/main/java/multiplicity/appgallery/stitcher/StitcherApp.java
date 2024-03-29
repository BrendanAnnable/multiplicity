


















package multiplicity.appgallery.stitcher;

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
import multiplicity.appgallery.stitcher.listeners.BackgroundImageRotateScaleBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotFrameBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotImageBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotItemBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotRotateScaleBehavior;
import multiplicity.appgallery.stitcher.listeners.HotSpotTextBehavior;
import multiplicity.appgallery.stitcher.listeners.PaletBehavior;
import multiplicity.appgallery.stitcher.listeners.RepositoryBehavior;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
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
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.hotspot.IHotSpotRepo;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardDefinition;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IImageRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysngjme.factory.PaletItemFactory;
import multiplicity.csysngjme.factory.Repository.RepositoryContentItemFactory;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMEImage;
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

public class StitcherApp extends AbstractMultiplicityApp implements IStitcherContants {

	
    private final static Logger logger = Logger.getLogger(StitcherApp.class.getName());
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

	public StitcherApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		setHotSpotContentFactory(new HotSpotContentItemFactory());
		setPaletFactory(new PaletItemFactory());
		setRepositoryFactory(new RepositoryContentItemFactory());
		StitcherUtils.stitcherApp = this;
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
			prop.load(StitcherApp.class.getResourceAsStream("xwiki.properties"));
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
//        
        IImage bg = getContentFactory().createImage("backgroundimage",
                UUID.randomUUID());
        bg.setImage(StitcherApp.class
                .getResource("white1920x1200.png"));
        bg.centerItem();
        add(bg);
        
        zOrderManager.sendToBottom(bg, null);
        zOrderManager.neverBringToTop(bg);
	    
	    
	    createHotSpotRepo(IMAGE);
        createHotSpotRepo(TEXT);
        
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
		
		  ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
	        trails.respondToItem(bg);
	        trails.setFadingColour(Color.black);
	        add(trails);
	}

    public IHotSpotFrame createNewHotSpotContentFrame(String type) {
		return createNewFrame(null, null, null, type);
	}

	public IHotSpotFrame createNewFrame(IItem backgroundImage,Vector2f atPosition, String frameName, String type) {
	    final IHotSpotFrame newHotSpotFrame;

	       
	    if( type.equals(BACKGROUND) ) {
	        backgroundImage.setRelativeScale(INITIAL_DROP_SCALE);
	        JMEImage bi = (JMEImage)backgroundImage;

	        // scale image.
	        Vector3f localScale = bi.getLocalScale();

	        float newX = bi.getSize().x * localScale.x;
	        float newY = bi.getSize().y * localScale.y;
	        
	        newHotSpotFrame = this.getHotSpotContentFactory().createHotSpotFrame(frameName,  UUID.randomUUID(), Float.valueOf(newX).intValue(), Float.valueOf(newY).intValue());
	        newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 0, 0, new ColorRGBA(0f, 0f, 0f, 0f)));
	        newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
	        newHotSpotFrame.maintainBorderSizeDuringScale();
	        newHotSpotFrame.setRelativeLocation(atPosition);
	        BehaviourMaker.addBehaviour(newHotSpotFrame, BackgroundImageRotateScaleBehavior.class);
	        
            Vector2f itemWorldPos = backgroundImage.getWorldLocation();
            newHotSpotFrame.addItem(backgroundImage);
            backgroundImage.setWorldLocation(itemWorldPos);
            BehaviourMaker.removeBehavior(backgroundImage, BackgroundImageRotateScaleBehavior.class);
            backgroundImage.centerItem();
//            newHotSpotFrame.getZOrderManager().bringToTop(backgroundImage, null);
            // little trick to make sure palet and hotspots are always on top
            backgroundImage.addItemListener(new ItemListenerAdapter() {
                @Override
                public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
                    super.itemCursorPressed(item, event);
                    logger.debug("background image pressed");
                    if( item.getParentItem() instanceof IHotSpotFrame) {
                        IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                        parentFrame.getZOrderManager().sendToBottom(item, null);
                        if (!parentFrame.isLocked()) {
                            parentFrame.sendOverlayToBottom();
                        }
                        
                        StitcherUtils.updateAllHotLinkConnections();
                        parentFrame.bringHotSpotsToTop();
                        StitcherUtils.bringAllHotLinksToTheTop(parentFrame);
                        StitcherUtils.bringAllHotSpotFramesToTheTop();
                        
                        parentFrame.bringPaletToTop();
                    }
                 
                }
            });
           
            

	    } else if( type.equals(IMAGE) ){
	        
	        UUID randomUUID = UUID.randomUUID();
	        float xPos = Integer.valueOf(-DisplaySystem.getDisplaySystem().getWidth() / 2 + (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();
	        float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();

	        newHotSpotFrame = (IHotSpotFrame) this.getHotSpotContentFactory().createHotSpotFrame(HOTSPOT_FRAME_NAME_IMAGE + randomUUID, randomUUID, HOTSPOT_FRAME_DIMENSION, HOTSPOT_FRAME_DIMENSION);

	        newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 0,0, new ColorRGBA(0f, 0f, 0f, 0f)));
	        newHotSpotFrame.setSolidBackgroundColour(Color.black);
//	        newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
	        newHotSpotFrame.maintainBorderSizeDuringScale();
	        newHotSpotFrame.setRelativeLocation(new Vector2f(xPos, yPos));

	        BehaviourMaker.addBehaviour(newHotSpotFrame, RotateTranslateScaleBehaviour.class);

	        //we dont want to scale hotspots until a pic is dropped
	       
	    
	    } else if(type != null && type.equals(TEXT)) {
	         
	        UUID randomUUID = UUID.randomUUID();
            float xPos = Integer.valueOf(-DisplaySystem.getDisplaySystem().getWidth() / 2 + (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();
            float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();

            newHotSpotFrame  =  this.getHotSpotContentFactory().createEditableHotSpotTextFrame(HOTSPOT_FRAME_NAME_TEXT + randomUUID, randomUUID, HOTSPOT_FRAME_DIMENSION, HOTSPOT_FRAME_DIMENSION,StitcherApp.class.getResource("keyboard.png"));
            
            newHotSpotFrame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 0, 0, new ColorRGBA(0f, 0f, 0f, 0f)));
            
            
            newHotSpotFrame.setSolidBackgroundColour(Color.black);
//            newHotSpotFrame.setGradientBackground(StitcherUtils.burColorRGBA, StitcherUtils.burColorRGBA, GradientDirection.VERTICAL));
//            newHotSpotFrame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
            newHotSpotFrame.maintainBorderSizeDuringScale();
            newHotSpotFrame.setRelativeLocation(new Vector2f(xPos, yPos));
            ((IHotSpotText)newHotSpotFrame).createText("tag");
//            ((IHotSpotText)newHotSpotFrame).setTextColour(new Color(90,18,18));

            BehaviourMaker.addBehaviour(newHotSpotFrame, RotateTranslateScaleBehaviour.class);

            ((IHotSpotText)newHotSpotFrame).createKeyboard(NorwegianKeyboardDefinition.class);
            
            newHotSpotFrame.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter(){
                
                    @Override
                    public void cursorChanged(MultiTouchCursorEvent event) {
                        super.cursorChanged(event);
                        ((IHotSpotText)newHotSpotFrame).makeSureKeyboardImageIsOnTop();
                    }
                
                    @Override
                    public void cursorReleased(MultiTouchCursorEvent event) {
                        super.cursorReleased(event);
                        ((IHotSpotText)newHotSpotFrame).makeSureKeyboardImageIsOnTop();
                        
                    }
            });
	    } else {
	        newHotSpotFrame = null; 
	    }
	         
		BehaviourMaker.addBehaviour(newHotSpotFrame, HotSpotTextBehavior.class);

		newHotSpotFrame.setType(type);
		BehaviourMaker
				.addBehaviour(newHotSpotFrame, HotSpotFrameBehavior.class);

		if (type.equals(BACKGROUND) || type.equals(IMAGE)) {

			BehaviourMaker.addBehaviour(newHotSpotFrame, OverlayBehavior.class);

			// set up the palet
			IPalet palet = this.getPaletFactory().createPaletItem("palet",
					UUID.randomUUID(), PALET_DIMENSION,
					new ColorRGBA(0f, 1f, 0f, .5f));
			
			
			newHotSpotFrame.addPalet(palet);
			palet.centerItem();
//			palet.setRelativeScale(.8f);
			BehaviourMaker.addBehaviour(palet,
					HotSpotRotateScaleBehavior.class);

			BehaviourMaker.addBehaviour(palet, PaletBehavior.class);

		}
	    
	    
	    //add for later
		this.add(newHotSpotFrame);
        hotSpotFrames.add(newHotSpotFrame);
	    
	    return newHotSpotFrame;
	}
	
   
	
	public void repostoryFactory(Vector<Object> imagesToAdd, Vector2f atPosition, String frameName) {

		Color startColor = new Color(0.6f, 0.6f, 0.6f, 1f);
		Color endColor = new Color(0f, 0f, 0f, 1f);

        if (frameName.equals(STENCIL_REPO_NAME)) {
        	IImageRepositoryFrame frame = this.getRepositoryFactory().createImageRepositoryFrame(frameName, UUID.randomUUID(), DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

//			frame.setSize(DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

			Float xPos = 0f;
			Float yPos = Integer.valueOf((int) (-DisplaySystem.getDisplaySystem().getHeight() /2 - (TOP_BOTTOM_REPO_HEIGHT / 2 + BORDER_THICKNESS) + BORDER_THICKNESS)).floatValue();
			Vector2f closePosition = new Vector2f(xPos, yPos);
			
	         Vector2f openPosition = new Vector2f(closePosition.x, closePosition.y + TOP_BOTTOM_REPO_HEIGHT);
	         frame.setOpenLocation(openPosition);
	            frame.setCloseLocation(closePosition);
			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder( frameName +"-border", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(.211f, .211f, .211f, 1f));
            frame.setBorder(border);
            frame.maintainBorderSizeDuringScale();
            frame.setGradientBackground(new Gradient(startColor, endColor,
                    GradientDirection.VERTICAL));
           
            add(frame);
            BehaviourMaker.addBehaviour(frame, RepositoryBehavior.class);
            addImagesToFrame(frame, imagesToAdd);
//            getZOrderManager().bringToTop(frame, null);
            frame.close();
		} else if (frameName.equals(SCAN_REPO_NAME)) {
	        IImageRepositoryFrame frame = this.getRepositoryFactory().createImageRepositoryFrame(frameName, UUID.randomUUID(), DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

//			frame.setSize(DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

			Float xPos = 0f;
			Float yPos = Integer.valueOf((int) (DisplaySystem.getDisplaySystem().getHeight() / 2 + (TOP_BOTTOM_REPO_HEIGHT / 2 + BORDER_THICKNESS) - BORDER_THICKNESS)).floatValue();
			Vector2f  closePosition = new Vector2f(xPos, yPos);
			
			Vector2f openPosition = new Vector2f(closePosition.x, closePosition.y - TOP_BOTTOM_REPO_HEIGHT);
			frame.setOpenLocation(openPosition);
			frame.setCloseLocation(closePosition);
			
			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder( frameName +"-border", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(.211f, .211f, .211f, 1f));
			frame.setBorder(border);
            frame.maintainBorderSizeDuringScale();
            frame.setGradientBackground(new Gradient(startColor, endColor,
                    GradientDirection.VERTICAL));
            add(frame);
            BehaviourMaker.addBehaviour(frame, RepositoryBehavior.class);
            frame.close();
            addImagesToFrame(frame, imagesToAdd);
//            getZOrderManager().bringToTop(frame, null);
            frame.close();
		} else if (frameName.equals(BACKGROUND_REPO_NAME)) {

		    IBackgroundRepositoryFrame frame = this.getRepositoryFactory().createBackgroundRepositoryFrame(frameName, UUID.randomUUID(), RIGHT_LEFT_REPO_HEIGHT, DisplaySystem.getDisplaySystem().getHeight() - 10);

//			frame.setSize(RIGHT_LEFT_REPO_HEIGHT, DisplaySystem.getDisplaySystem().getHeight() - 10);

			Float xPos = Integer.valueOf((int) (DisplaySystem.getDisplaySystem().getWidth() / 2 + (RIGHT_LEFT_REPO_HEIGHT / 2 + BORDER_THICKNESS) - BORDER_THICKNESS)).floatValue();
			Float yPos = 0f;
			Vector2f closePosition = new Vector2f(xPos, yPos);
			Vector2f  openPosition = new Vector2f(closePosition.x - RIGHT_LEFT_REPO_HEIGHT, closePosition.y);
			frame.setCloseLocation(closePosition);
			frame.setOpenLocation(openPosition);

			JMERoundedRectangleBorder border = new JMERoundedRectangleBorder( frameName +"-border", UUID.randomUUID(), BORDER_THICKNESS, 0);
			border.setColor(new ColorRGBA(.9f, .9f, .9f, 1f));
            frame.setBorder(border);
            frame.maintainBorderSizeDuringScale();
            frame.setGradientBackground(new Gradient(startColor, endColor,
                    GradientDirection.VERTICAL));
            BehaviourMaker.addBehaviour(frame, RepositoryBehavior.class);
            add(frame);
            frame.close();
            addImagesToFrame(frame, imagesToAdd);
           getZOrderManager().bringToTop(frame, null);
            frame.open();
		}

	}

	public void addImagesToFrame(IFrame frame, Vector<Object> imagesToAdd) {
	    for (int i = 0; i < imagesToAdd.size(); i++) {
            @SuppressWarnings("unchecked")
            Vector<Object> itemEntry = (Vector<Object>) imagesToAdd.elementAt(i);

            IImage image = (IImage) itemEntry.elementAt(1);
            BehaviourMaker.addBehaviour(image, HotSpotImageBehavior.class);
            float scale = (Float) itemEntry.elementAt(0);
            image.setRelativeScale(scale);
//            new ImageMultiTouchListener(image, this);
            frame.addItem(image);
           
            //add behavior
//            BehaviourMaker.addBehaviour(image, InertiaBehaviour.class);
            Vector2f position = StitcherUtils.generateRandomPosition(frame, image);
            image.setRelativeLocation(position);
        }
	}


	public void createHotSpotRepo(String type) {
	    
	    IHotSpotRepo frame = this.getHotSpotContentFactory().createHotSpotRepo(HOTSPOTS, UUID.randomUUID(), HOTSPOT_DIMENSION, HOTSPOT_DIMENSION);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), .1f, 5, new ColorRGBA(0f, 0f, 0f, .1f) ));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0f), new Color(0.2f, 0.2f, 0.2f, 0f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();

		Float xPos = 0f;
		Float yPos = 0f;
		if( type.equals(TEXT) ) {
		    xPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getWidth() / 2 - (HOTSPOT_DIMENSION / 2)-20).floatValue();
		    yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_DIMENSION / 2)-360).floatValue();
		    logger.debug("HSPOT TEXT REPO " + xPos + "," +yPos);
		} else if(type.equals(IMAGE)) {
		    xPos = Double.valueOf(DisplaySystem.getDisplaySystem().getWidth() / 2 - (HOTSPOT_DIMENSION / 2)-20).floatValue();
	        yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_DIMENSION / 2) - 460).floatValue();
	        logger.debug("HSPOT IMAGE REPO " + xPos + "," +yPos);
		}
		
		
		frame.setRelativeLocation(new Vector2f(-xPos, yPos));

		this.add(frame);
		fillHotSpotRepo(frame,type);

		this.getZOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}

	public void fillHotSpotRepo(IFrame frame, String type) {

	    
	    IHotSpotItem hotSpotItemImage = null;
	    URL resource = null;
	    
	    ColorRGBA colorRGBA = null;
	    if( type.equals(IMAGE) ) {
	    	resource = StitcherApp.class.getResource("imagesymbol.png");
	    } else if(type.equals(TEXT)) {
	    	resource = StitcherApp.class.getResource("textsymbol.png");
	    }
		hotSpotItemImage = this.getHotSpotContentFactory().createHotSpotItemImage("hotspot", UUID.randomUUID(), resource);
		hotSpotItemImage.setType(type);
		frame.addItem(hotSpotItemImage);
		hotSpotItemImage.centerItem();
//		hotSpotItemImage.setRelativeScale(.2f);
		
		BehaviourMaker.addBehaviour(hotSpotItemImage, HotSpotItemBehavior.class);


		BehaviourMaker.addBehaviour((IItem) hotSpotItemImage, RotateTranslateScaleBehaviour.class);
		 List<IBehaviour> behaviours = hotSpotItemImage.getBehaviours();
		 StitcherUtils.modScaleBehavior(behaviours,false);

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

    public List<IHotSpotFrame> getHotSpotFrames() {
        return hotSpotFrames;
    }

}
