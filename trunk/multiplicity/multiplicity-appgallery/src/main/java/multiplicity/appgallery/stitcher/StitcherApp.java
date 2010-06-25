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

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.gesture.GestureLibrary;
import multiplicity.csysng.factory.IHotSpotContentFactory;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;
import multiplicity.csysngjme.items.JMEFrame;
import multiplicity.csysngjme.items.JMEImage;
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
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

public class StitcherApp extends AbstractStandaloneApp {

	private final static Logger logger = Logger.getLogger(StitcherApp.class.getName());
	public final String MULTIPLICITY_SPACE = "multiplicity";

	public final String STENCIL_NAME = "stencils";
	public final String BACKGROUND_NAME = "backgrounds";
	public final String SCAN_NAME = "scans";
	
	private final Float BORDER_THICKNESS = 40f;
	private final ArrayList<String> pageNames = new ArrayList<String>();
	private IPage stencilsPage;
	private IPage backgroundsPage;
	private IPage scansPage;
	private HashMap<String, IPage> wikiPages;
	// private Vector<IComment> comments;
	private XWikiRestFulService wikiService;
	private Vector<IAttachment> attachments;
	// private Vector<ITag> tags;
	public String output_document_path = null;
	public String wikiUser = null;
	public String wikiPass = null;
	public Grouper smaker = null;
	public int maxFileSize = 0;
	private int frameWidth = 600;
	private int frameHeight = 600;
	private StitcherApp stitcher;

	// when this is filled the first one is at the top of the z index
	ArrayList<IItem> zOrderedItems;
    private IHotSpotContentFactory hotSpotContentFactory;

	public StitcherApp(IMultiTouchEventProducer mtInput) {
		super(mtInput);
		stitcher = this;
	}

	@Override
	public void onAppStart() {
	    setHotSpotContentFactory(new HotSpotContentItemFactory());
    	pageNames.add(STENCIL_NAME);
		pageNames.add(BACKGROUND_NAME);
//		pageNames.add(SCAN_NAME);
		populateFromWiki();
		loadContent(wikiPages);
		
		
	}

	private void populateFromWiki() {
		Properties prop = new Properties();
		wikiPages = new HashMap<String, IPage>();

		try {
			prop.load(StitcherApp.class.getResourceAsStream("xwiki.properties"));
			this.wikiUser = prop.getProperty("DEFAULT_USER");
			this.wikiPass = prop.getProperty("DEFAULT_PASS");
			this.maxFileSize = Integer.valueOf(prop.getProperty("MAX_ATTCHMENT_SIZE"));
			stencilsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("REPOSITORY_WIKI_SPACE"), prop.getProperty("REPOSITORY_WIKI_SPACE_STENCILS"), false);
			wikiPages.put(pageNames.get(0), stencilsPage);
			backgroundsPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_BACKGROUNDS"), false);
			wikiPages.put(pageNames.get(1), backgroundsPage);
//			scansPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_SCANS"), false);
//			wikiPages.put(pageNames.get(2), scansPage);
		} catch (IOException e) {
			logger.debug("setup:  IOException: " + e);
		}

	}

	private IPage getWikiPage(Properties prop, String wikiName,
			String spaceName, String pageName, boolean withAttachments) {
		IPage wikiPage = null;
		wikiService = new XWikiRestFulService(prop);
		wikiPage = wikiService.getWikiPage(wikiName, spaceName, pageName,
				withAttachments);
		return wikiPage;
	}

	private void loadContent(HashMap<String, IPage> wikiPages) {

		zOrderedItems = new ArrayList<IItem>();
		smaker = new Grouper();
		this.getMultiTouchEventProducer().registerMultiTouchEventListener(smaker);

		// IImage bg = getContentFactory().createImage("backgroundimage",
		// UUID.randomUUID());
		// bg.setImage(StitcherApp.class.getResource("yellowflowers_1680x1050.png"));
		// bg.centerItem();
		// add(bg);

		// //load the comments
		// for (IComment comment : comments) {
		// ILabel commentLabel = getContentFactory().createLabel("comment",
		// UUID.randomUUID());
		// commentLabel.setText(comment.getText());
		// commentLabel.setFont(new Font("Myriad Pro", Font.BOLD, 24));
		// commentLabel.setTextColour(Color.white);
		// commentLabel.setRelativeLocation(new Vector2f(10, 10));
		// ButtonBehaviour bb = (ButtonBehaviour)
		// BehaviourMaker.addBehaviour(commentLabel, ButtonBehaviour.class);
		// bb.addListener(new IButtonBehaviourListener() {
		// @Override
		// public void buttonClicked(IItem item) {
		// System.out.println("click");
		// }
		//
		// @Override
		// public void buttonPressed(IItem item) {
		// System.out.println("pressed");
		//
		// }
		//
		// @Override
		// public void buttonReleased(IItem item) {
		// System.out.println("released");
		//
		//
		// }
		//
		// });
		//
		// BehaviourMaker.addBehaviour(commentLabel,
		// RotateTranslateScaleBehaviour.class);
		// BehaviourMaker.addBehaviour(commentLabel, InertiaBehaviour.class);
		//
		// // smaker.register(commentLabel);
		// smaker.register(commentLabel, this);
		//
		// zOrderedItems.add(commentLabel);
		// add(commentLabel);
		// }
		//
		// //load the tags
		// for( ITag tag : tags) {
		// ILabel tagLabel = getContentFactory().createLabel("tag",
		// UUID.randomUUID());
		// tagLabel.setText(tag.getName());
		// tagLabel.setFont(new Font("Myriad Pro", Font.BOLD, 18));
		// tagLabel.setTextColour(Color.BLUE);
		// tagLabel.setRelativeLocation(new Vector2f(10, 10));
		// ButtonBehaviour bb = (ButtonBehaviour)
		// BehaviourMaker.addBehaviour(tagLabel, ButtonBehaviour.class);
		// bb.addListener(new IButtonBehaviourListener() {
		// @Override
		// public void buttonClicked(IItem item) {
		// getzOrderManager().sendToBottom(item, null);
		// }
		//
		// @Override
		// public void buttonPressed(IItem item) {}
		//
		// @Override
		// public void buttonReleased(IItem item) {}
		//
		// });
		//
		// BehaviourMaker.addBehaviour(tagLabel,
		// RotateTranslateScaleBehaviour.class);
		// BehaviourMaker.addBehaviour(tagLabel, InertiaBehaviour.class);
		//
		// smaker.register(tagLabel, this);
		//
		// zOrderedItems.add(tagLabel);
		// add(tagLabel);
		// }

		List<IItem> items = null;
		IPage iPage = null;
		GetAttachmentItems getAttachmentItems;
		for (int i = 0; i < wikiPages.size(); i++) {
			iPage = wikiPages.get(pageNames.get(i));
			attachments = iPage.getAttachments();
			items = new ArrayList<IItem>();

			getAttachmentItems = new GetAttachmentItems(this, iPage, attachments, items, pageNames.get(i));
			try {
				getAttachmentItems.start();
				getAttachmentItems.join();
				items = getAttachmentItems.getItems();
			} catch (InterruptedException e) {
				logger.debug("GetAttachmentItems:  InterruptedException: " + e);
			}

			if (items != null) {
				addItemsToFrame(getAttachmentItems.getItems(), new Vector2f(i * 5f, i * 5f), pageNames.get(i));
			}
		}
		
		createHotSpotRepo();

		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

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

	
	public void moveItemToNewFrame(IItem item, Vector2f atPosition, String frameName) {
	    UUID uUID = UUID.randomUUID();
	    
	    JMEImage bi = (JMEImage) item;
	    
	    //scale image.
	    Vector3f localScale = bi.getLocalScale();
	    
	    float newX = bi.getSize().x * localScale.x;
	    float newY = bi.getSize().y * localScale.y;
	    
        IFrame frame = this.getHotSpotContentFactory().createHotSpotFrame(frameName, uUID, Float.valueOf(newX).intValue(), Float.valueOf(newY).intValue());
        
        frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), BORDER_THICKNESS, 15));
        frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
        frame.maintainBorderSizeDuringScale();
        frame.setRelativeLocation(atPosition);
        BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

        this.add(frame);
       
        Vector2f itemWorldPos = item.getWorldLocation();
        frame.addItem(item);
        item.setWorldLocation(itemWorldPos);
        frame.getZOrderManager().bringToTop(item, null);    
//        BehaviourMaker.removeBehavior(item, RotateTranslateScaleBehaviour.class);
        item.centerItem();
        
        this.getzOrderManager().bringToTop(frame, null);
        
     
        frame.getZOrderManager().updateZOrdering();
	}
	
	public void addItemsToFrame(List<IItem> items, Vector2f atPosition, String frameName) {
		UUID uUID = UUID.randomUUID();
		IFrame frame = this.getContentFactory().createFrame(frameName, uUID, frameWidth, frameHeight);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), BORDER_THICKNESS, 15));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		frame.setRelativeLocation(atPosition);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		this.add(frame);
		for (IItem item : items) {
			item.setRelativeScale(0.5f);
			frame.addItem(item);
		}
		
		
		this.getzOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}
	
	public void createHotSpotRepo() {
		UUID uUID = UUID.randomUUID();
		IFrame frame = this.getContentFactory().createFrame("hotspots", uUID, 60, 60);

		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 5f, 5));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		
		//TODO: use width/height of app instead
		Float xPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getWidth()/2 - 60/2).floatValue();
		Float yPos = Integer.valueOf(DisplaySystem.getDisplaySystem().getHeight()/2 - 60/2).floatValue();
		
		frame.setRelativeLocation(new Vector2f(xPos, yPos));

		this.add(frame);
		fillHotSpotRepo(frame);
		
		this.getzOrderManager().bringToTop(frame, null);

		// createXMLRepresentationForGroup(uUID, items);
	}

	private void fillHotSpotRepo(IFrame frame) {
	    
	    IHotSpotItem hotspot = this.getHotSpotContentFactory().createHotSpotItem("cr", UUID.randomUUID(), 20, 20);
		hotspot.setSolidBackgroundColour(new Color(1.0f, 0f, 0f, 0.8f));
		frame.addItem(hotspot);
		hotspot.centerItem();
		
		hotspot.addItemListener(new ItemListenerAdapter() {
			@Override
			public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {
				String message = "Hotspot released: ";
				boolean offParent = true;
				Node s = (Node) stitcher.getOrthoNode();

				Vector2f locStore = new Vector2f();
				UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, locStore);

				List<PickedSpatial> spatialsList = AccuratePickingUtility.pickAllOrthogonal(s.getParent().getParent(), locStore);
				
				boolean firstFrameFound = false;
				for (PickedSpatial pickedSpatial : spatialsList) {
					if (pickedSpatial.getSpatial().equals(((JMEFrame) item.getParentItem().getTreeRootSpatial()).getMaskGeometry())) {
						offParent = false;
						message = message + "on its parent. Nothing happens";
						HotSpotFrame hsFrame = (HotSpotFrame) item.getParentItem();
						hsFrame.connectHotSpots(hsFrame.getHotSpots().get(0), hsFrame.getHotSpots().get(1));
					}
					else if((pickedSpatial.getSpatial().toString()).equals("maskGeometry") && !firstFrameFound ) {
						try {
							Geometry geometry = (Geometry) pickedSpatial.getSpatial();
							JMEFrame targetFrame = (JMEFrame) geometry.getParent();
							
							if(targetFrame.getName().contains("back-")) {
								firstFrameFound = true;
								IFrame frame = (IFrame) item.getParentItem();
								frame.removeItem(item);
								
								Vector2f itemWorldPos = item.getWorldLocation();
								targetFrame.addItem(item);
						        item.setWorldLocation(itemWorldPos);
						        targetFrame.getZOrderManager().bringToTop(item, null);    

						        IFrame hsFrame = (IFrame) item.getParentItem();
						        if( hsFrame instanceof HotSpotFrame) {
						            drawLineBetweenHotSpots(((HotSpotFrame) hsFrame).addHotSpot(item), (HotSpotFrame)hsFrame);
						            message = message + "on "+targetFrame.getName()+". Great!!";
						            fillHotSpotRepo(frame);
						        }
							}
							
						}
						catch (Exception e) {
							logger.debug("GetAttachmentItems: itemCursorReleased: Exception: "+e);
						}
					}
				}
				
				if(!firstFrameFound && offParent) {
					item.centerItem();
					IFrame hsFrame = (IFrame) item.getParentItem();
			        if( hsFrame instanceof HotSpotFrame) {
			        	((HotSpotFrame) hsFrame).connectHotSpots(((HotSpotFrame) hsFrame).getHotSpots().get(0), ((HotSpotFrame) hsFrame).getHotSpots().get(1));			        	
			        }
					message = message + "in the mist .... Let's place it back to the center of its mother frame.";
				}
				
				logger.info(message);
			}

			private void drawLineBetweenHotSpots(int addHotSpot, HotSpotFrame hsFrame) {
				if(addHotSpot > 1) {
					logger.info("let's draw some lines");		
					hsFrame.connectHotSpots(hsFrame.getHotSpots().get(0), hsFrame.getHotSpots().get(1));
				}
			}
		});
		
		BehaviourMaker.addBehaviour((IItem) hotspot, RotateTranslateScaleBehaviour.class);
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
		SingleAppTableSystem.startSystem(StitcherApp.class);
	}

    public void setHotSpotContentFactory(IHotSpotContentFactory hotSpotContentFactory) {
        this.hotSpotContentFactory = hotSpotContentFactory;
    }

    public IHotSpotContentFactory getHotSpotContentFactory() {
        return hotSpotContentFactory;
    }
}
