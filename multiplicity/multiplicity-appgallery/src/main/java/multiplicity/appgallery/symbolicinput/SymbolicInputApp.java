package multiplicity.appgallery.symbolicinput;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;

import multiplicity.app.AbstractMultiplicityApp;
import multiplicity.app.AbstractSurfaceSystem;
import multiplicity.app.singleappsystem.SingleAppMultiplicitySurfaceSystem;
import multiplicity.appgallery.gallery.GalleryApp;
import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity.csysng.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity.csysng.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity.csysng.items.keyboard.model.KeyModifiers;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;
import multiplicity.input.IMultiTouchEventProducer;

public class SymbolicInputApp extends AbstractMultiplicityApp {

	public SymbolicInputApp(AbstractSurfaceSystem ass, IMultiTouchEventProducer producer) {
		super(ass, producer);
	}

	@Override
	public void onAppStart() {
		createBackground();
		
		final IEditableText label2 = getContentFactory().createEditableText("label", UUID.randomUUID());
		label2.setText("abc");
		label2.setFont(new Font("Myriad Pro", Font.BOLD, 48*4));
		label2.setTextColour(Color.white);
		label2.setRelativeLocation(new Vector2f(0, 200));
		label2.setCursorAt(3);
		BehaviourMaker.addBehaviour(label2, RotateTranslateScaleBehaviour.class);		
		addItem(label2);
		zOrderManager.bringToTop(label2, null);
		
		
		final IKeyboard kb = getContentFactory().createKeyboard("kb", UUID.randomUUID());
		KeyboardDefinition kbd = new SimpleAlphaKeyboardDefinition();
		kb.setKeyboardDefinition(kbd);
		IKeyboardGraphicsRenderer keyboardRenderer = new SimpleAlphaKeyboardRenderer(kbd);
		kb.setKeyboardRenderer(keyboardRenderer);
		KeyboardBehaviour kbb = (KeyboardBehaviour) BehaviourMaker.addBehaviour(kb, KeyboardBehaviour.class);
		kbb.addListener(keyboardRenderer);
		kbb.addListener(new IMultiTouchKeyboardListener() {
			
			@Override
			public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown) {
				if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					label2.removeChar();
					
				}else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
					// ignore
				}else if(k.getModifiers() == KeyModifiers.NONE) {				
					if(shiftDown) {
						String txt = KeyEvent.getKeyText(k.getKeyCode()).toUpperCase();
						label2.appendChar(txt.charAt(0));
					}else{
						String txt = KeyEvent.getKeyText(k.getKeyCode()).toLowerCase();
						label2.appendChar(txt.charAt(0));
					}
				}
				kb.reDrawKeyboard(shiftDown, altDown, ctlDown);
			}
			
			@Override
			public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown) {
				kb.reDrawKeyboard(shiftDown, altDown, ctlDown);
			}
		});
		
		IFrame framewrap = getContentFactory().createFrame("keyboardFrame", UUID.randomUUID(), kb.getSize().x, kb.getSize().y);		
		framewrap.maintainBorderSizeDuringScale();
		framewrap.addItem(kb);
		framewrap.setRelativeLocation(new Vector2f(0f, -200f));
		
		IBorder jmeRoundedRectangleBorder = ContentSystem.getContentSystem().getContentFactory().createRoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 15f, 30, new ColorRGBA(1f,0f, 1f, 1f));
		jmeRoundedRectangleBorder.setColor(new ColorRGBA(.211f, .211f, .211f, 1f));
		framewrap.setBorder(jmeRoundedRectangleBorder);
		
		addItem(framewrap);		
		BehaviourMaker.addBehaviour(framewrap, RotateTranslateScaleBehaviour.class);
		
		zOrderManager.bringToTop(framewrap, null);
	}
	
	private void createBackground() {
		IImage bg = getContentFactory().createImage("backgroundimage", UUID.randomUUID());
		bg.setImage(GalleryApp.class.getResource("yellowflowers_1680x1050.png"));
		bg.centerItem();
		addItem(bg);		
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
		SingleAppMultiplicitySurfaceSystem.startSystem(SymbolicInputApp.class);
	}

}
