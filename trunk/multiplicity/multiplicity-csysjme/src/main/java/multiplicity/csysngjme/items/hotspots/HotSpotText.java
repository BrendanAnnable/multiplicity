package multiplicity.csysngjme.items.hotspots;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.ILineItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity.csysng.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity.csysng.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity.csysng.items.keyboard.model.KeyModifiers;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;
import multiplicity.csysngjme.factory.JMEContentItemFactory;
import multiplicity.csysngjme.items.JMEEditableText;

import com.jme.math.Vector2f;

public class HotSpotText extends JMEEditableText implements IHotSpotText {

    public List<ILineItem> hotLinks = new CopyOnWriteArrayList<ILineItem>();
    public List<IHotSpotItem> hotSpots = new CopyOnWriteArrayList<IHotSpotItem>();
    private IKeyboard keyboard; 
    private boolean isKeyboardShown;
    private JMEContentItemFactory contentItemFactory = new JMEContentItemFactory();
    private IFrame keyboardFrame;
    private int taps = 0;
    
    public HotSpotText(String name, UUID uuid) {
        super(name, uuid);
        createKeyboard();
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
    
    private void createKeyboard() {
        keyboard = contentItemFactory.createKeyboard("kb", UUID.randomUUID());
        KeyboardDefinition kbd = new SimpleAlphaKeyboardDefinition();
        keyboard.setKeyboardDefinition(kbd);
        IKeyboardGraphicsRenderer keyboardRenderer = new SimpleAlphaKeyboardRenderer(kbd);
        keyboard.setKeyboardRenderer(keyboardRenderer);
        KeyboardBehaviour kbb = (KeyboardBehaviour) BehaviourMaker.addBehaviour(keyboard, KeyboardBehaviour.class);
        kbb.addListener(keyboardRenderer);
        kbb.addListener(new IMultiTouchKeyboardListener() {
            
            @Override
            public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown) {
                if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    removeChar();
                    
                }else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
                    // ignore
                }else if(k.getModifiers() == KeyModifiers.NONE) {               
                    if(shiftDown) {
                        String txt = KeyEvent.getKeyText(k.getKeyCode()).toUpperCase();
                        appendChar(txt.charAt(0));
                    }else{
                        String txt = KeyEvent.getKeyText(k.getKeyCode()).toLowerCase();
                        appendChar(txt.charAt(0));
                    }
                }
                keyboard.reDrawKeyboard(shiftDown, altDown, ctlDown);
            }
            
            @Override
            public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown) {
                keyboard.reDrawKeyboard(shiftDown, altDown, ctlDown);
            }
        });
        
        keyboardFrame = contentItemFactory.createFrame("keyboardFrame", UUID.randomUUID(), keyboard.getSize().x, keyboard.getSize().y);     
        keyboardFrame.maintainBorderSizeDuringScale();
        keyboardFrame.addItem(keyboard);
        keyboardFrame.setRelativeLocation(new Vector2f(0f, -200f));
        keyboardFrame.setBorder(contentItemFactory.createRoundedRectangleBorder("innerframeborder", UUID.randomUUID(), 20f, 8));    
        BehaviourMaker.addBehaviour(keyboardFrame, RotateTranslateScaleBehaviour.class);
        
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
    public void addHotLink(ILineItem hotLink) {
        this.hotLinks.add(hotLink);
    }
    
    public void removeHotLink(IHotLink hotLink) {
        if( !hotLinks.isEmpty() && hotLinks.contains(hotLink)) {
            hotLinks.remove(hotLink);
        }
            
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
    public void setHotLinks(List<ILineItem> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public List<ILineItem> getHotLinks() {
        return hotLinks;
    }

    public List<IHotSpotItem> getHotSpots() {
        return hotSpots;
    }
    
    public void setHotSpots(List<IHotSpotItem> hotSpots) {
        this.hotSpots = hotSpots;
    }

    public void addHotSpot(IItem item) {
        hotSpots.add((IHotSpotItem) item);
    }

    public void bringHotSpotsToTop() {
        for (IHotSpotItem iHotSpotItem : hotSpots) {
            this.getZOrderManager().bringToTop(iHotSpotItem, null);  
            iHotSpotItem.redrawHotlink(iHotSpotItem);
        }
    }

    @Override
    public void bringPaletToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isVisable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setLocked(boolean isLocked) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void toggleLock() {
        // TODO Auto-generated method stub

    }

    @Override
    public IPalet getPalet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendOverlayToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendOverlayToBottom() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPalet(IPalet palet) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendHotLinksToTop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendHotLinksToBottom() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBorder(IBorder b) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBorder getBorder() {
        return null;
    }

    @Override
    public boolean hasBorder() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSolidBackgroundColour(Color c) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setGradientBackground(Gradient g) {
        // TODO Auto-generated method stub

    }

    @Override
    public IItemListener maintainBorderSizeDuringScale() {
        // TODO Auto-generated method stub
        return null;
    }

    

}