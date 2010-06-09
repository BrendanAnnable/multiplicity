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
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventProducer;
import no.uio.intermedia.snomobile.XWikiRestFulService;
import no.uio.intermedia.snomobile.interfaces.IAttachment;
import no.uio.intermedia.snomobile.interfaces.IPage;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;

public class StitcherApp extends AbstractStandaloneApp {

	private final static Logger logger = Logger.getLogger(StitcherApp.class.getName());	
	public final String MULTIPLICITY_SPACE = "multiplicity";

	public final String STENCIL_NAME = "stencils";
	public final String BACKGROUND_NAME = "backgrounds";
	public final String SCAN_NAME = "scans";
	private final ArrayList<String> pageNames = new ArrayList<String>();
	private IPage stencilsPage;
	private IPage backgroundsPage;
	private IPage scansPage;
	private HashMap<String, IPage> wikiPages;
	//private Vector<IComment> comments;
	private XWikiRestFulService wikiService;
	private Vector<IAttachment> attachments;
	//private Vector<ITag> tags;
	public String output_document_path = null;
	public String wikiUser = null;
	public String wikiPass = null;
	public Grouper smaker = null;
	public int maxFileSize = 0;
	private int frameWidth = 600;
	private int frameHeight = 600;
	

	//when this is filled the first one is at the top of the z index
	ArrayList<IItem> zOrderedItems;

	public StitcherApp(IMultiTouchEventProducer mtInput) {
		super(mtInput);
	}

	@Override
	public void onAppStart() {
		pageNames.add(STENCIL_NAME);
		pageNames.add(BACKGROUND_NAME);
		pageNames.add(SCAN_NAME);
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
			scansPage = getWikiPage(prop, prop.getProperty("DEFAULT_WIKI_NAME"), prop.getProperty("CLASS_WIKI_SPACE"), prop.getProperty("CLASS_WIKI_SPACE_SCANS"), false);
			wikiPages.put(pageNames.get(2), scansPage);
		} 
		catch (IOException e) {
			logger.debug("setup:  IOException: "+e);
		}
		
	}

	private IPage getWikiPage(Properties prop, String wikiName, String spaceName, String pageName, boolean withAttachments) {
		IPage wikiPage = null;
		wikiService = new XWikiRestFulService(prop);
		wikiPage = wikiService.getWikiPage(wikiName, spaceName, pageName, withAttachments);
		return wikiPage;
	}

	private void loadContent(HashMap<String, IPage> wikiPages) {

		zOrderedItems = new ArrayList<IItem>();
		smaker = new Grouper();
		this.getMultiTouchEventProducer().registerMultiTouchEventListener(smaker);

//		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
//		bg.setImage(StitcherApp.class.getResource("yellowflowers_1680x1050.png"));
//		bg.centerItem();
//		add(bg);

//		//load the comments
//		for (IComment comment : comments) {
//			ILabel commentLabel = getContentFactory().createLabel("comment", UUID.randomUUID());
//			commentLabel.setText(comment.getText());
//			commentLabel.setFont(new Font("Myriad Pro", Font.BOLD, 24));
//			commentLabel.setTextColour(Color.white);
//			commentLabel.setRelativeLocation(new Vector2f(10, 10));
//			ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(commentLabel, ButtonBehaviour.class);
//			bb.addListener(new IButtonBehaviourListener() {
//				@Override
//				public void buttonClicked(IItem item) {
//					System.out.println("click");                    
//				}
//
//				@Override
//				public void buttonPressed(IItem item) {
//					System.out.println("pressed");                    
//
//				}
//
//				@Override
//				public void buttonReleased(IItem item) {
//					System.out.println("released");                    
//
//
//				}
//
//			});
//
//			BehaviourMaker.addBehaviour(commentLabel, RotateTranslateScaleBehaviour.class);
//			BehaviourMaker.addBehaviour(commentLabel, InertiaBehaviour.class);
//
//			//	            smaker.register(commentLabel);
//			smaker.register(commentLabel, this);
//
//			zOrderedItems.add(commentLabel);
//			add(commentLabel);
//		}
//
//		//load the tags
//		for( ITag tag : tags) {
//			ILabel tagLabel = getContentFactory().createLabel("tag", UUID.randomUUID());
//			tagLabel.setText(tag.getName());
//			tagLabel.setFont(new Font("Myriad Pro", Font.BOLD, 18));
//			tagLabel.setTextColour(Color.BLUE);
//			tagLabel.setRelativeLocation(new Vector2f(10, 10));
//			ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(tagLabel, ButtonBehaviour.class);
//			bb.addListener(new IButtonBehaviourListener() {
//				@Override
//				public void buttonClicked(IItem item) {
//					getzOrderManager().sendToBottom(item, null);             
//				}
//
//				@Override
//				public void buttonPressed(IItem item) {}
//
//				@Override
//				public void buttonReleased(IItem item) {}
//
//			});
//
//			BehaviourMaker.addBehaviour(tagLabel, RotateTranslateScaleBehaviour.class);
//			BehaviourMaker.addBehaviour(tagLabel, InertiaBehaviour.class);
//
//			smaker.register(tagLabel, this);
//
//			zOrderedItems.add(tagLabel);
//			add(tagLabel);
//		}
		
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
			
			if(items != null) {
				addItemsToFrame(getAttachmentItems.getItems(), new Vector2f(i*5f, i*5f), pageNames.get(i));				
			}
		}

		IColourRectangle rect = getContentFactory().createColourRectangle("cr", UUID.randomUUID(), 100, 50);
		rect.setSolidBackgroundColour(new Color(1.0f, 0f, 0f, 0.8f));
		add(rect);
		BehaviourMaker.addBehaviour(rect, RotateTranslateScaleBehaviour.class);

		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());		
		add(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
//		trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		add(trails);

//		getzOrderManager().sendToBottom(bg, null);
//		getzOrderManager().neverBringToTop(bg);

	}
	
	public void addItemsToFrame(List<IItem> items, Vector2f atPosition, String frameName) {	
		UUID uUID = UUID.randomUUID();
		IFrame frame = this.getContentFactory().createFrame(frameName, uUID, frameWidth, frameHeight);
		
		frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 10f, 15));
		frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		frame.setRelativeLocation(atPosition);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		this.add(frame);
		smaker.register(frame, this);
		for (IItem item : items) {
			item.setRelativeScale(0.5f);
			frame.add(item);
		}
		this.getzOrderManager().bringToTop(frame, null);
		
		//createXMLRepresentationForGroup(uUID, items);
	}
	

	/**
	 * bumps the item down one. 
	 * @param item
	 */
	public void bumpDownZOrder(IItem item) {

		int indexOf = zOrderedItems.indexOf(item);

		//get the one above it
		try {
			IItem swapItem = zOrderedItems.get(indexOf + 1 );

			int swapItemZorder = swapItem.getZOrderManager().getItemZOrder();
			int oldZOrder = item.getZOrderManager().getItemZOrder();




			item.getZOrderManager().setItemZOrder(swapItemZorder);
			swapItem.getZOrderManager().setItemZOrder(oldZOrder);

			//getzOrderManager().


			//zOrderedItems.remove(indexOf);
			//zOrderedItems.remove(indexOf+1);
			//zOrderedItems.add(indexOf, swapItem);
			//zOrderedItems.add(indexOf+1, item);
		} catch(IndexOutOfBoundsException iobe) {

		}
	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem.startSystem(StitcherApp.class);
	}
}

