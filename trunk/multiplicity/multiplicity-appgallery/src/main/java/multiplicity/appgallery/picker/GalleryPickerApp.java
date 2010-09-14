package multiplicity.appgallery.picker;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.behaviours.button.ButtonBehaviour;
import multiplicity.csysng.behaviours.button.IButtonBehaviourListener;
import multiplicity.csysng.behaviours.gesture.GestureDetectionBehaviour;
import multiplicity.csysng.behaviours.gesture.GestureLibrary;
import multiplicity.csysng.behaviours.gesture.GestureMatch;
import multiplicity.csysng.behaviours.gesture.IGestureListener;
import multiplicity.csysng.behaviours.inertia.InertiaBehaviour;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysng.items.overlays.ICursorOverlay;
import multiplicity.csysng.items.overlays.ICursorTrailsOverlay;
import multiplicity.csysng.items.repository.IBackgroundRepositoryFrame;
import multiplicity.csysng.items.repository.IRepositoryContentItemFactory;
import multiplicity.csysngjme.factory.Repository.RepositoryContentItemFactory;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventProducer;
import multiplicity.input.MultiTouchEventAdapter;
import multiplicity.input.events.MultiTouchCursorEvent;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

public class GalleryPickerApp extends AbstractMultiplicityApp {

    private final static Logger logger = Logger.getLogger(GalleryPickerApp.class.getName());

    
	public GalleryPickerApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		loadContent();
	}

	private void loadContent() {
//		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
//		bg.setImage(GalleryPickerApp.class.getResource("yellowflowers_1680x1050.png"));
//		bg.centerItem();
//		add(bg);


		
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

//		GestureDetectionBehaviour gdb = (GestureDetectionBehaviour) BehaviourMaker.addBehaviour(bg, GestureDetectionBehaviour.class);
//		gdb.addListener(new IGestureListener() {				
//			@Override
//			public void gestureDetected(GestureMatch match, IItem item) {				
//				if(match.libraryGesture.getName().equals("circle") && match.matchScore > 0.8) {
//					addRandomFrame(""+(int)(Math.random() * 10));						
//				}
//			}
//		});

//		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
//		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());		
//		add(cursors);
//
//		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
//		trails.respondToItem(bg);
//		trails.setFadingColour(Color.white);
//		add(trails);
		

		 addTestFrameWithPic();
//		addNestedFrameExample();
//		addRandomFrame("aotn.jpg");
//		addRandomFrame("fabbey.jpg");
//		addRandomFrame("wreck.jpg");
//		
//		zOrderManager.sendToBottom(bg, null);
//		zOrderManager.neverBringToTop(bg);

	}
	
	   private void addTestFrameWithPic() {
	       IRepositoryContentItemFactory repositoryFactory = new RepositoryContentItemFactory();
	       
	       Color startColor = new Color(0.6f, 0.6f, 0.6f, 1f);
	        Color endColor = new Color(0f, 0f, 0f, 1f);
	        
	       String frameName = "test_frame";
	       
	       int frameWidth = 600;

	       int frameHeight = 600;
	       
	       int frameWidth2 =  DisplaySystem.getDisplaySystem().getWidth() - 10;
	       int frameHeight2 =  400;
	       
	       Float BORDER_THICKNESS = 40f;
	       
	       Float TOP_BOTTOM_REPO_HEIGHT = 300f;

	       Float RIGHT_LEFT_REPO_HEIGHT = 400f;
	       
	        IBackgroundRepositoryFrame frame = repositoryFactory.createBackgroundRepositoryFrame(frameName, UUID.randomUUID(),frameWidth, frameHeight);

	        frame.setSize(DisplaySystem.getDisplaySystem().getWidth() - 10, TOP_BOTTOM_REPO_HEIGHT);

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
	        
	        
	        IImage img = getContentFactory().createImage("test photo", UUID.randomUUID());
	        img.setImage(GalleryPickerApp.class.getResource("wreck.jpg"));
	        img.setRelativeScale(0.3f);
	        BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
	        img.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
	            
	            @Override
	            public void cursorReleased(MultiTouchCursorEvent event) {
	                // TODO Auto-generated method stub
	                super.cursorReleased(event);
	                
	                Vector2f screenPos = new Vector2f();
	                ContentSystem.getContentSystem().getDisplayManager().tableToScreen(event.getPosition(), screenPos);
//	                UnitConversion.tableToScreen(event.getPosition().x, event.getPosition().y, screenPos);
	                List<IItem> findItemsOnTableAtPosition = ContentSystem.getContentSystem().getPickSystem().findItemsOnTableAtPosition(screenPos);
	                logger.debug("items found on drop: " + findItemsOnTableAtPosition.toString());
	            }
	            
	        });
	        frame.addItem(img);
	        
	        getZOrderManager().bringToTop(frame, null);
	        frame.open();
	        
	    }

	private void addNestedFrameExample() {
		IFrame framewrap = getContentFactory().createFrame("superframe", UUID.randomUUID(), 800, 500);		
		framewrap.setBorder(getContentFactory().createRoundedRectangleBorder("superframeborder", UUID.randomUUID(), 20f, 8));
		framewrap.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		framewrap.maintainBorderSizeDuringScale();
		add(framewrap);
		BehaviourMaker.addBehaviour(framewrap, RotateTranslateScaleBehaviour.class);

		IFrame f = getContentFactory().createFrame("innerframe", UUID.randomUUID(), 200, 100);		
		f.setBorder(getContentFactory().createRoundedRectangleBorder("innerframeborder", UUID.randomUUID(), 20f, 8));
		f.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		f.maintainBorderSizeDuringScale();
		framewrap.addItem(f);
		BehaviourMaker.addBehaviour(f, RotateTranslateScaleBehaviour.class);
		

	}

	private void addRandomFrame(String photoResource) {		
		IFrame frame = getContentFactory().createFrame("randomframe", UUID.randomUUID(), 400, 200);		
		frame.setBorder(getContentFactory().createRoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 20f, 8));
		frame.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		frame.maintainBorderSizeDuringScale();
		add(frame);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		ILabel label2 = getContentFactory().createLabel("label", UUID.randomUUID());
		label2.setText("MultiTouch");
		label2.setFont(new Font("Myriad Pro", Font.BOLD, 24));
		label2.setTextColour(Color.white);
		label2.setRelativeLocation(new Vector2f(10, 10));
		ButtonBehaviour bb = (ButtonBehaviour) BehaviourMaker.addBehaviour(label2, ButtonBehaviour.class);
		bb.addListener(new IButtonBehaviourListener() {
			@Override
			public void buttonClicked(IItem item) {}

			@Override
			public void buttonPressed(IItem item) {}

			@Override
			public void buttonReleased(IItem item) {}

		});


		frame.addItem(label2);

		IImage img = getContentFactory().createImage("photo", UUID.randomUUID());
		img.setImage(GalleryPickerApp.class.getResource(photoResource));
		img.setRelativeScale(0.8f);
		BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
		BehaviourMaker.addBehaviour(img, InertiaBehaviour.class);
		frame.addItem(img);

		zOrderManager.bringToTop(frame, null);
	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(GalleryPickerApp.class);
	}
}

