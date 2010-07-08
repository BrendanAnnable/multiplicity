package multiplicity.appgallery.symbolicinput;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.jme.math.Vector2f;

import multiplicity.app.singleappsystem.AbstractStandaloneApp;
import multiplicity.app.singleappsystem.SingleAppTableSystem;
import multiplicity.appgallery.gallery.GalleryApp;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.keyboard.KeyboardBehaviour;
import multiplicity.csysng.behaviours.keyboard.MultiTouchKeyboardListener;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.KeyboardKey;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.input.IMultiTouchEventProducer;

public class SymbolicInputApp extends AbstractStandaloneApp {

	public SymbolicInputApp(IMultiTouchEventProducer producer) {
		super(producer);
	}

	@Override
	public void onAppStart() {
		createBackground();
		
		final IEditableText label2 = contentFactory.createEditableText("label", UUID.randomUUID());
		label2.setText("abc");
		label2.setFont(new Font("Myriad Pro", Font.BOLD, 48*4));
		label2.setTextColour(Color.white);
		label2.setRelativeLocation(new Vector2f(10, 100));
		label2.setCursorAt(3);
		BehaviourMaker.addBehaviour(label2, RotateTranslateScaleBehaviour.class);		
		add(label2);
		zOrderManager.bringToTop(label2, null);
		
		final IKeyboard kb = contentFactory.createKeyboard("kb", UUID.randomUUID());
		kb.setKeyboardDefinition(new SimpleAlphaKeyboardDefinition());
		kb.setRelativeLocation(new Vector2f(0, -200));
		KeyboardBehaviour kbb = (KeyboardBehaviour) BehaviourMaker.addBehaviour(kb, KeyboardBehaviour.class);
		kbb.addListener(new MultiTouchKeyboardListener() {
			
			@Override
			public void keyReleased(KeyboardKey k) {
				if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					// something...
				}else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
					// something...
				}else{				
					String txt = KeyEvent.getKeyText(k.getKeyCode());
					label2.appendChar(txt.charAt(0));
				}
			}
			
			@Override
			public void keyPressed(KeyboardKey k) {
			}
		});
		
		add(kb);
		
		zOrderManager.bringToTop(kb, null);
		

	}
	
	private void createBackground() {
		IImage bg = contentFactory.createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(GalleryApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		add(bg);		
		zOrderManager.sendToBottom(bg, null);
		zOrderManager.neverBringToTop(bg);
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
		SingleAppTableSystem.startSystem(SymbolicInputApp.class);
	}

}
