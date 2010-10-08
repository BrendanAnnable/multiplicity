package multiplicity.csysngjme.items.hotspots;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.UUID;

import multiplicity.csysng.ContentSystem;
import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.items.IEditableText;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IImage;
import multiplicity.csysng.items.IImage.AlphaStyle;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity.csysng.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardListener;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;
import multiplicity.csysngjme.factory.JMEContentItemFactory;

import com.jme.math.Vector2f;

public class HotSpotTextFrame extends HotSpotFrame implements IHotSpotText{

	private static final long serialVersionUID = 1L;
	private IKeyboard keyboard; 
    private boolean isKeyboardShown;
    private JMEContentItemFactory contentItemFactory = new JMEContentItemFactory();
    private IFrame keyboardFrame;
    private int taps = 0;
    private IEditableText labelText;
    private IImage keyboardImage;
    private URL keyboardImageUrl;
    private KeyboardBehaviour keyBoardBehavior;
    
    public HotSpotTextFrame(String name, UUID uuid, int width, int height, URL keyboardImageUrl) {
        super(name, uuid, width, height);
        this.keyboardImageUrl = keyboardImageUrl;
    }

    
    @Override
    public void setCursorAt(int index) {
        labelText.setCursorAt(index);
    }

    @Override
    public void setCursorDisplay(boolean onOrOff) {
        labelText.setCursorDisplay(onOrOff);
    }

    @Override
    public void insertChar(char c) {
        labelText.insertChar(c);
    }

    @Override
    public void removeChar() {
        
    }

    @Override
    public void appendChar(char c) {
    	labelText.appendChar(c);
    }

    @Override
    public void appendString(String charSet) {
    	labelText.appendString(charSet);
    }

    @Override
    public void setFont(Font f) {
    	labelText.setFont(f);
    }

    @Override
    public void setText(String text) {
        labelText.setText(text);
    }

    @Override
    public String getText() {
        return labelText.getText();
    }

    @Override
    public void setTextColour(Color c) {
        labelText.setTextColour(c);
    }

    @Override
    public IFrame getKeyboard() {
        return keyboardFrame;
    }

    @Override
    public boolean isKeyboardVisible() {
        return isKeyboardShown;
    }

    @Override
    public void setKeyboardVisible(boolean isKeyboardShown) {
        this.isKeyboardShown = isKeyboardShown;
    }


    @Override
    public int tap() {
        return ++taps ;
    }

    @Override
    public void resetTaps() {
        taps = 0;
    }

    @Override
    public void toggle() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createKeyboard(Class<? extends KeyboardDefinition> keyboardDef) {
        keyboard = contentItemFactory.createKeyboard("kb", UUID.randomUUID());
        
        KeyboardDefinition kbd = null;
        try {
            kbd = keyboardDef.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        keyboard.setKeyboardDefinition(kbd);
        IKeyboardGraphicsRenderer keyboardRenderer = new SimpleAlphaKeyboardRenderer(kbd);
        keyboard.setKeyboardRenderer(keyboardRenderer);
        setKeyBoardBehavior((KeyboardBehaviour) BehaviourMaker.addBehaviour(keyboard, KeyboardBehaviour.class));
        getKeyBoardBehavior().addListener(keyboardRenderer);
        getKeyBoardBehavior().addListener(new NorwegianKeyboardListener(labelText, keyboard));
        keyboardFrame = contentItemFactory.createFrame("keyboardFrame", UUID.randomUUID(), keyboard.getSize().x, keyboard.getSize().y);     
        keyboardFrame.maintainBorderSizeDuringScale();
        keyboardFrame.addItem(keyboard);
        keyboardFrame.setRelativeLocation(new Vector2f(0f, -200f));
        keyboardFrame.setBorder(contentItemFactory.createRoundedRectangleBorder("innerframeborder", UUID.randomUUID(),1, 15));    
        BehaviourMaker.addBehaviour(keyboardFrame, RotateTranslateScaleBehaviour.class);
        
        keyboard.getMultiTouchDispatcher().addListeners(keyboardFrame.getMultiTouchDispatcher().getListeners());
    }


    @Override
    public void createText(String t) {
        setLabelText(ContentSystem.getContentSystem().getContentFactory().createEditableText("hotspot-text", UUID.randomUUID()));
        getLabelText().setText(t);
        getLabelText().setFont(new Font("Arial", Font.BOLD, 48*2));
        getLabelText().setTextColour(Color.white);
        getLabelText().setRelativeLocation(new Vector2f(0, -10));
        getLabelText().setCursorAt(t.length()-1);
        addItem(getLabelText());
//        getLabelText().centerItem();
        
        
//        this.getSize().setY(this.getSize().getY()*.8f);
//        getFrameOverlay().setSize(this.getSize());
//        updateModelBound();
//        getLabelText().getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
        
        setKeyboardImage(ContentSystem.getContentSystem().getContentFactory().createImage("keyboard-icon", UUID.randomUUID()));
        getKeyboardImage().setImage(keyboardImageUrl);
        //img.setRelativeScale(0.6f);
        getKeyboardImage().setAlphaBlending(AlphaStyle.USE_TRANSPARENCY);
        getKeyboardImage().setRelativeLocation(new Vector2f(0, 53));
        addItem(getKeyboardImage());

        this.setSize(getLabelText().getSize().x, getLabelText().getSize().y+getKeyboardImage().getSize().y);
        
        if( t != null && t.equals("tag")) {
            getLabelText().setText(" ");
        }
        
    }
    
    @Override
    public void behaviourAdded(IBehaviour behaviour) {
        super.behaviourAdded(behaviour);
        getLabelText().getMultiTouchDispatcher().addListeners(getMultiTouchDispatcher().getListeners());
    }


    public void setLabelText(IEditableText labelText) {
        this.labelText = labelText;
    }
    
    @Override
    public boolean hasPalet() {
        return false;
    }


    public IEditableText getLabelText() {
        return labelText;
    }
    
    @Override
    public void updateOverLay() {
        super.updateOverLay();
    }


    public void setKeyboardImage(IImage keyboardImage) {
        this.keyboardImage = keyboardImage;
    }


    @Override
    public IImage getKeyboardImage() {
        return keyboardImage;
    }
    
    @Override
    public void makeSureKeyboardImageIsOnTop() {
        this.getZOrderManager().bringToTop(getKeyboardImage(), null);
    }


    public void setKeyBoardBehavior(KeyboardBehaviour keyBoardBehavior) {
        this.keyBoardBehavior = keyBoardBehavior;
    }


    @Override
    public KeyboardBehaviour getKeyBoardBehavior() {
        return keyBoardBehavior;
    }

}
