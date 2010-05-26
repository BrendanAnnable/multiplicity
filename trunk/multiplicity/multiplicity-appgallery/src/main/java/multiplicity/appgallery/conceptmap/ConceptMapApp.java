package multiplicity.appgallery.conceptmap;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.scene.Geometry;


import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.appgallery.gallery.GalleryApp;
import multiplicity.csysng.animation.AnimationSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.gesture.GestureDetectionBehaviour;
import multiplicity.csysng.behaviours.gesture.GestureLibrary;
import multiplicity.csysng.behaviours.gesture.GestureMatch;
import multiplicity.csysng.behaviours.gesture.IGestureListener;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILabel;
import multiplicity.csysngjme.animation.animelements.Fader;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.items.JMERoundedRectangleBorder;
import multiplicity.input.IMultiTouchEventProducer;

public class ConceptMapApp extends AbstractStandaloneApp {

    /**
     * test again
     * 
     * @param producer
     */
	public ConceptMapApp(IMultiTouchEventProducer producer) {
		super(producer);
	}

	@Override
	public void onAppStart() {
		addInstruction();
		createGesturableBackground();
	}
	
	/**
	 * adds instructions
	 * 
	 */
	private void addInstruction() {
		ILabel instruction = contentFactory.createLabel("instruction", UUID.randomUUID());
		instruction.setText("Draw a C shape, but in reverse...");
		instruction.setFont(new Font("Arial Narrow", Font.PLAIN, 14));
		instruction.setRelativeLocation(new Vector2f(0, 0));
		instruction.setTextColour(Color.white);
		add(instruction);
		zOrderManager.bringToTop(instruction, null);
		AnimationSystem.getInstance().add(new Fader((Geometry)instruction.getManipulableSpatial(), Fader.MODE_FADE_IN, 2, 10));
	}

	protected void createNewNode() {
		IFrame frame = contentFactory.createFrame("randomframe", UUID.randomUUID(), 160, 70);		
		frame.setBorder(new JMERoundedRectangleBorder("frameborder", UUID.randomUUID(), 10f, 8));
		frame.setGradientBackground(new Gradient(
				new Color(0.5f, 0.5f, 0.5f, 1f), 
				new Color(0f, 0f, 0f,1f), GradientDirection.VERTICAL));
		add(frame);
		BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);

		ILabel label2 = contentFactory.createLabel("label", UUID.randomUUID());
		label2.setText("<New Node>");
		label2.setFont(new Font("Arial", Font.PLAIN, 18));
		label2.setTextColour(Color.white);
		frame.add(label2);
		label2.centerItem();
		
		zOrderManager.bringToTop(frame, null);
	}
	
	private void createGesturableBackground() {
		IImage bg = contentFactory.createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(GalleryApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		add(bg);		
		zOrderManager.sendToBottom(bg, null);
		zOrderManager.neverBringToTop(bg);
		
		GestureLibrary.getInstance().loadGesture("circle");
		GestureLibrary.getInstance().loadGesture("line");

		GestureDetectionBehaviour gdb = (GestureDetectionBehaviour) BehaviourMaker.addBehaviour(bg, GestureDetectionBehaviour.class);
		gdb.addListener(new IGestureListener() {				
			@Override
			public void gestureDetected(GestureMatch match, IItem item) {				
				if(match.libraryGesture.getName().equals("circle") && match.matchScore > 0.8) {
					createNewNode();						
				}
			}
		});
	}
	


	/**
	 * This is the main method
	 * 
	 * @param args
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		SingleAppTableSystem.startSystem(ConceptMapApp.class);
	}

}
