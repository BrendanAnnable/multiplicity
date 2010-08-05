package multiplicity.appgallery.stitcher;

import java.awt.Color;
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
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.factory.IPaletFactory;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysng.items.repository.IRepositoryFrame;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.factory.PaletItemFactory;
import multiplicity.csysngjme.factory.Repository.RepositoryContentItemFactory;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMEColourRectangle;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
import multiplicity.csysngjme.items.JMELine;
import multiplicity.csysngjme.items.JMERectangularItem;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.csysngjme.items.hotspots.HotSpotFrame;
import multiplicity.csysngjme.picking.AccuratePickingUtility;
import multiplicity.csysngjme.picking.PickedSpatial;
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
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class StitcherApp extends AbstractMultiplicityApp {

	private final static Logger logger = Logger.getLogger(StitcherApp.class.getName());
	public final String MULTIPLICITY_SPACE = "multiplicity";
	public final String STENCIL_NAME = "stencils";
	public final String BACKGROUND_NAME = "backgrounds";
	public final String SCAN_NAME = "scans";
	private final List<String> pageNames = new ArrayList<String>();
	private final List<JMELine> hotspotConnections = new ArrayList<JMELine>();
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
	private StitcherApp stitcher;

	public final Float BORDER_THICKNESS = 40f;
	public final Float TOP_BOTTOM_REPO_HEIGHT = 300f;
	public final Float RIGHT_LEFT_REPO_HEIGHT = 400f;
	public final int HOTSPOT_DIMENSION = 80;
	public final int HOTSPOT_FRAME_DIMENSION = 200;
	public final int PALET_DIMENSION = 75;
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
		stitcher = this;
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

	    
//	    ILabel instruction = contentFactory.createLabel("instruction", UUID.randomUUID());
//        instruction.setText("Draw a C shape, but in reverse...");
//        instruction.setFont(new Font("Arial Narrow", Font.PLAIN, 14));
//        instruction.setRelativeLocation(new Vector2f(0, 0));
//        instruction.setTextColour(Color.white);
//        add(instruction);
//        zOrderManager.bringToTop(instruction, null);
        
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
		

       // AnimationSystem.getInstance().add(new Fader((Geometry)instruction.getManipulableSpatial(), Fader.MODE_FADE_IN, 2, 10));
        
       

		createHotSpotRepo();

//		GestureLibrary.getInstance().loadGesture("circle");
//		GestureLibrary.getInstance().loadGesture("line");

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());
		add(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
		// trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		add(trails);

		// getzOrderManager().sendToBottom(bg, null);
		// getzOrderManager().neverBringToTop(bg);
		


	}

	public IHotSpotFrame createNewHotSpotContentFrame() {
		UUID uUID = UUID.randomUUID();

		// TODO: use width/height of app instead
		float xPos = Integer.valueOf(-DisplaySystem.getDisplaySystem().getWidth() / 2 + (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();
		float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - (HOTSPOT_FRAME_DIMENSION / 2 + Float.valueOf(BORDER_THICKNESS).intValue())).floatValue();

		IHotSpotFrame frame = (IHotSpotFrame) this.getHotSpotContentFactory().createHotSpotFrame("hotspotf-" + uUID, uUID, HOTSPOT_FRAME_DIMENSION, HOTSPOT_FRAME_DIMENSION);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1, 15, new ColorRGBA(0f, 0f, 0f, 0f)));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		frame.setRelativeLocation(new Vector2f(xPos, yPos));

		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		this.add(frame);

		// add the smaller palet, let's make it green ..
		UUID paluUID = UUID.randomUUID();
		IPalet palet = this.getPaletFactory().createPaletItem("palet", paluUID, SMALLER_PALET_DIMENSION, new ColorRGBA(0f, 1f, 0f, 1f));
		frame.addItem(palet);
		frame.getZOrderManager().bringToTop(palet, null);
		palet.centerItem();
		BehaviourMaker.addBehaviour(palet, RotateTranslateScaleBehaviour.class);
		palet.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
			    IPalet palet = (IPalet) item;
			    int taps = palet.tap();
				logger.info("palet clicked " + taps);
				
				
                if (taps == 2) {
                    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                    parentFrame.toggleLock();
                    palet.updatePalet(parentFrame.isLocked());
                    parentFrame.bringHotSpotsToTop();
                    parentFrame.bringPaletToTop();
                    palet.resetTaps();
                }
			}

			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				String message = "Palet released: ";
				boolean offParent = true;
				Node s = (Node) stitcher.getOrthoNode();

				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);

				IHotSpotFrame paletParent = (IHotSpotFrame) item.getParentItem();

				for (PickedSpatial pickedSpatial : spatialsList) {
					if (pickedSpatial.getSpatial().equals(((JMEFrame) paletParent.getTreeRootSpatial()).getMaskGeometry())) {
						offParent = false;
						message = message + "on its parent. Nothing happens";
					}
				}

				if (offParent) {
					item.centerItem();
					paletParent.bringHotSpotsToTop();
					paletParent.bringPaletToTop();
					message = message + "in the mist .... Let's place it back to the center of its mother frame.";
				}

				logger.info(message);
			}
		});

		this.getZOrderManager().bringToTop(frame, null);

		return frame;
	}

	public void moveItemToNewFrame(IItem item, Vector2f atPosition, String frameName) {
		UUID uUID = UUID.randomUUID();

		item.setRelativeScale(INITIAL_DROP_SCALE);
		JMEImage bi = (JMEImage) item;

		// scale image.
		Vector3f localScale = bi.getLocalScale();

		float newX = bi.getSize().x * localScale.x;
		float newY = bi.getSize().y * localScale.y;

		IHotSpotFrame frame = this.getHotSpotContentFactory().createHotSpotFrame(frameName, uUID, Float.valueOf(newX).intValue(), Float.valueOf(newY).intValue());

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 1, 15, new ColorRGBA(0f, 0f, 0f, 0f)));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		frame.setRelativeLocation(atPosition);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);
		
		JMEColourRectangle frameOverlay = ((HotSpotFrame) frame).getFrameOverlay();
		frameOverlay.addItemListener(new IItemListener() {
			
			@Override
			public void itemZOrderChanged(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemScaled(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemRotated(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemMoved(IItem item) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();				
				parentFrame.bringHotSpotsToTop();
				parentFrame.bringPaletToTop();
				stitcher.bumpHotSpotConnections();
			}
			
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.add(frame);

		Vector2f itemWorldPos = item.getWorldLocation();
		frame.addItem(item);
		item.setWorldLocation(itemWorldPos);
		frame.getZOrderManager().bringToTop(item, null);
		BehaviourMaker.removeBehavior(item, RotateTranslateScaleBehaviour.class);
		item.centerItem();
		// little trick to make sure palet and hotspots are always on top
		item.addItemListener(new ItemListenerAdapter() {
			@Override
			public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
				IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
				parentFrame.getZOrderManager().sendToBottom(item, null);
				if (!parentFrame.isLocked()) {
					parentFrame.sendOverlaytoBottom();
				}
				parentFrame.bringHotSpotsToTop();
				parentFrame.bringPaletToTop();
				stitcher.bumpHotSpotConnections();
			}
		});

		// add the palet, let's make it green ..
		UUID paluUID = UUID.randomUUID();
		IPalet palet = this.getPaletFactory().createPaletItem("palet", paluUID, PALET_DIMENSION, new ColorRGBA(0f, 1f, 0f, 1f));
		frame.addItem(palet);
		frame.getZOrderManager().bringToTop(palet, null);
		palet.centerItem();
		BehaviourMaker.addBehaviour(palet, RotateTranslateScaleBehaviour.class);
		palet.addItemListener(new ItemListenerAdapter() {
			public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {
				logger.info("palet clicked");
				
				
		        IPalet palet = (IPalet) item;
                int taps = palet.tap();
                logger.info("palet clicked " + taps);
                
                
                if (taps == 2) {
                    IHotSpotFrame parentFrame = (IHotSpotFrame) item.getParentItem();
                    parentFrame.toggleLock();

                    palet.updatePalet(parentFrame.isLocked());

                    parentFrame.bringHotSpotsToTop();
                    parentFrame.bringPaletToTop();
                    stitcher.bumpHotSpotConnections();
                    palet.resetTaps();
                }
				
				
			
			}

			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				String message = "Palet released: ";
				boolean offParent = true;
				Node s = (Node) stitcher.getOrthoNode();

				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);

				IHotSpotFrame paletParent = (IHotSpotFrame) item.getParentItem();

				for (PickedSpatial pickedSpatial : spatialsList) {
					if (pickedSpatial.getSpatial().equals(((JMEFrame) paletParent.getTreeRootSpatial()).getMaskGeometry())) {
						offParent = false;
						message = message + "on its parent. Nothing happens";
					}
				}

				if (offParent) {
					item.centerItem();
					paletParent.bringHotSpotsToTop();
					paletParent.bringPaletToTop();
					message = message + "in the mist .... Let's place it back to the center of its mother frame.";
				}

				logger.info(message);
				paletParent.bringHotSpotsToTop();
				paletParent.bringPaletToTop();
				stitcher.bumpHotSpotConnections();
			}
		});

		this.getZOrderManager().bringToTop(frame, null);
		frame.getZOrderManager().updateZOrdering();
		frame.addItemListener(new ItemListenerAdapter() {

			@Override
			public void itemMoved(IItem item) {
				logger.debug("hot spot frame moved");

				IHotSpotFrame frame = (IHotSpotFrame) item;
				// update all the hotspotitems which will update all the
				// hotlinks
				List<IHotSpotItem> hotSpots = frame.getHotSpots();
				for (IHotSpotItem iHotSpotItem : hotSpots) {
					iHotSpotItem.update(frame.getRelativeLocation());
				}
				frame.bringHotSpotsToTop();
				stitcher.bumpHotSpotConnections();
			}
		});

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

	public void createHotSpotRepo() {
		UUID uUID = UUID.randomUUID();
		IFrame frame = this.getContentFactory().createFrame("hotspots", uUID, HOTSPOT_DIMENSION, HOTSPOT_DIMENSION);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 5f, 5));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();

		Float xPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getWidth() / 2 - HOTSPOT_DIMENSION / 2).floatValue();
		Float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight() / 2 - HOTSPOT_DIMENSION / 2).floatValue();

		frame.setRelativeLocation(new Vector2f(xPos, yPos));

		this.add(frame);
		fillHotSpotRepo(frame);

		this.getZOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}

	private void fillHotSpotRepo(IFrame frame) {

		IHotSpotItem hotspot = this.getHotSpotContentFactory().createHotSpotItem("cr", UUID.randomUUID(), PALET_DIMENSION / 4, new ColorRGBA(1f, 0f, 0f, 0.6f));
		frame.addItem(hotspot);
		hotspot.centerItem();

		hotspot.addItemListener(new ItemListenerAdapter() {
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				String message = "Hotspot released: ";
				boolean offParent = true;
				Node s = (Node) stitcher.getOrthoNode();

				JMEFrame hotSpotRepo = (JMEFrame) item.getParentItem();
				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);

				boolean firstFrameFound = false;
				for (PickedSpatial pickedSpatial : spatialsList) {
					if (pickedSpatial.getSpatial().equals(((JMEFrame) item.getParentItem().getTreeRootSpatial()).getMaskGeometry())) {
						offParent = false;
						message = message + "on its parent. Nothing happens";

					} else if ((pickedSpatial.getSpatial().toString()).equals("maskGeometry") && !firstFrameFound) {
						try {
							Geometry geometry = (Geometry) pickedSpatial.getSpatial();
							JMEFrame targetFrame = (JMEFrame) geometry.getParent();

							if (targetFrame.getName().contains("back-") && hotSpotRepo.getName().equals("hotspots")) {
								firstFrameFound = true;
								IFrame originFrame = (IFrame) item.getParentItem();
								originFrame.removeItem(item);

								Vector2f itemWorldPos = item.getWorldLocation();
								targetFrame.addItem(item);
								item.setWorldLocation(itemWorldPos);
								targetFrame.getZOrderManager().bringToTop(item, null);

								// JMEFrame hsFrame = (JMEFrame)
								// item.getParentItem();
								// if (hsFrame instanceof HotSpotFrame) {

								// add HS to the array attached to the frame
								
								IHotSpotItem hsItem = (IHotSpotItem)item;
								((IHotSpotFrame) targetFrame).addHotSpot(item);

								IHotSpotFrame hotSpotFrameContent = createNewHotSpotContentFrame();

								hsItem.setHotSpotFrameContent(hotSpotFrameContent);

								JMELine l = (JMELine) hsItem.createHotLink();

								hotspotConnections.add((JMELine) l);
								stitcher.add((JMELine) l);

								message = message + "on " + targetFrame.getName() + ". Great!!";

								// create a new hotspot candidate
								fillHotSpotRepo(originFrame);
								// }
							} else if (hotSpotRepo.getName().equals("hotspots") && targetFrame.getName().contains("hotspotf-")) {
								firstFrameFound = true;
								IFrame originFrame = (IFrame) item.getParentItem();
								originFrame.removeItem(item);

								Vector2f itemWorldPos = item.getWorldLocation();
								targetFrame.addItem(item);
								item.setWorldLocation(itemWorldPos);
								targetFrame.getZOrderManager().bringToTop(item, null);

								
								((IHotSpotFrame) targetFrame).addHotSpot(item);

								IHotSpotFrame hotSpotFrameContent = createNewHotSpotContentFrame();
                                IHotSpotItem hsItem = (IHotSpotItem)item;

								hsItem.setHotSpotFrameContent(hotSpotFrameContent);

								JMELine l = (JMELine) hsItem.createHotLink();
								hotspotConnections.add((JMELine) l);
								stitcher.add((JMELine) l);

								message = message + "on " + targetFrame.getName() + ". Great!!";

								// create a new hotspot candidate
								fillHotSpotRepo(originFrame);
							}

						} catch (Exception e) {
							logger.debug("GetAttachmentItems: itemCursorReleased: Exception: " + e);
						}
					}
				}

				if (!firstFrameFound && offParent) {
					item.centerItem();
					IFrame hsFrame = (IFrame) item.getParentItem();
					if (hsFrame instanceof HotSpotFrame) {
						// ((HotSpotFrame) hsFrame).connectHotSpots();
					}
					message = message + "in the mist .... Let's place it back to the center of its mother frame.";
				}

				logger.info(message);
				stitcher.bumpHotSpotConnections();
			}

		});

		BehaviourMaker.addBehaviour((IItem) hotspot, RotateTranslateScaleBehaviour.class);
	}

	public void bumpHotSpotConnections() {
		for (JMELine jMELine : hotspotConnections) {
			this.getZOrderManager().bringToTop(jMELine, null);
		}
	}

	/**
	 * bumps the item down one.
	 * 
	 * @param item
	 */
	public void bumpDownZOrder(IItem item) {

		int indexOf = zOrderedItems.indexOf(item);

		// get the one above it
		try {
			IItem swapItem = zOrderedItems.get(indexOf + 1);

			int swapItemZorder = swapItem.getZOrderManager().getItemZOrder();
			int oldZOrder = item.getZOrderManager().getItemZOrder();

			item.getZOrderManager().setItemZOrder(swapItemZorder);
			swapItem.getZOrderManager().setItemZOrder(oldZOrder);

			// getzOrderManager().

			// zOrderedItems.remove(indexOf);
			// zOrderedItems.remove(indexOf+1);
			// zOrderedItems.add(indexOf, swapItem);
			// zOrderedItems.add(indexOf+1, item);
		} catch (IndexOutOfBoundsException iobe) {

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