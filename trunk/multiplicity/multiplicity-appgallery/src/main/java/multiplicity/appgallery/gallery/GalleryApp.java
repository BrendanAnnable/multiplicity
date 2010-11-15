package multiplicity.appgallery.gallery;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
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
import multiplicity.input.IMultiTouchEventProducer;

import com.jme.math.Vector2f;

public class GalleryApp extends AbstractMultiplicityApp {

	public GalleryApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer mtInput) {
		super(ass, mtInput);
	}

	@Override
	public void onAppStart() {
		loadContent();
	}

	private void loadContent() {
		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(GalleryApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		addItem(bg);

		IColourRectangle rect = getContentFactory().createColourRectangle("cr", UUID.randomUUID(), 100, 50);
		rect.setSolidBackgroundColour(new Color(1.0f, 0f, 0f, 0.8f));
		addItem(rect);
		BehaviourMaker.addBehaviour(rect, RotateTranslateScaleBehaviour.class);
		


		
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

		GestureDetectionBehaviour gdb = (GestureDetectionBehaviour) BehaviourMaker.addBehaviour(bg, GestureDetectionBehaviour.class);
		gdb.addListener(new IGestureListener() {				
			@Override
			public void gestureDetected(GestureMatch match, IItem item) {				
				if(match.libraryGesture.getName().equals("circle") && match.matchScore > 0.8) {
					addRandomFrame(""+(int)(Math.random() * 10));						
				}
			}
		});

		ICursorOverlay cursors = getContentFactory().createCursorOverlay("cursorOverlay", UUID.randomUUID());
		cursors.respondToMultiTouchInput(getMultiTouchEventProducer());		
		addItem(cursors);

		ICursorTrailsOverlay trails = getContentFactory().createCursorTrailsOverlay("trails", UUID.randomUUID());
		trails.respondToItem(bg);
		trails.setFadingColour(Color.white);
		addItem(trails);
		

		addNestedFrameExample();
		addRandomFrame("aotn.jpg");
		addRandomFrame("fabbey.jpg");
		addRandomFrame("wreck.jpg");
		
		zOrderManager.sendToBottom(bg, null);
		zOrderManager.neverBringToTop(bg);

	}

	private void addNestedFrameExample() {
		IFrame framewrap = getContentFactory().createFrame("superframe", UUID.randomUUID(), 800, 500);		
		framewrap.setBorder(getContentFactory().createRoundedRectangleBorder("superframeborder", UUID.randomUUID(), 20f, 8));
		framewrap.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 0.8f), 
				new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
		framewrap.maintainBorderSizeDuringScale();
		addItem(framewrap);
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
		addItem(frame);
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
		img.setImage(GalleryApp.class.getResource(photoResource));
		img.setRelativeScale(0.8f);
		BehaviourMaker.addBehaviour(img, RotateTranslateScaleBehaviour.class);
		BehaviourMaker.addBehaviour(img, InertiaBehaviour.class);
		frame.addItem(img);

		zOrderManager.bringToTop(frame, null);
	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppMultiplicitySurfaceSystem.startSystem(GalleryApp.class);
	}
}

