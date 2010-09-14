package multiplicity.csysngjme.items.hotspots;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.behaviours.IBehaviour;
import multiplicity.csysng.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.items.IBorder;
import multiplicity.csysng.items.IColourRectangle;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.IHotSpotText;
import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.IPalet;
import multiplicity.csysng.items.events.IItemListener;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysng.items.keyboard.IKeyboard;
import multiplicity.csysng.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity.csysng.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity.csysng.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.norwegian.NorwegianKeyboardListener;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity.csysng.items.keyboard.model.KeyModifiers;
import multiplicity.csysng.items.keyboard.model.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.model.KeyboardKey;
import multiplicity.csysngjme.factory.JMEContentItemFactory;
import multiplicity.csysngjme.items.JMEEditableText;

import com.jme.math.Vector2f;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;

public class HotSpotText extends JMEEditableText implements IHotSpotText {
    
    private final static Logger logger = Logger.getLogger(HotSpotText.class.getName());


    public List<IHotLink> hotLinks = new CopyOnWriteArrayList<IHotLink>();
    public List<IHotSpotItem> hotSpots = new CopyOnWriteArrayList<IHotSpotItem>();
    private IKeyboard keyboard; 
    private boolean isKeyboardShown;
    private JMEContentItemFactory contentItemFactory = new JMEContentItemFactory();
    private IFrame keyboardFrame;
    private int taps = 0;
    private boolean isVisible;

    private boolean canScale = true;
    
    public HotSpotText(String name, UUID uuid) {
        super(name, uuid);
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
        KeyboardBehaviour kbb = (KeyboardBehaviour) BehaviourMaker.addBehaviour(keyboard, KeyboardBehaviour.class);
        kbb.addListener(keyboardRenderer);
        kbb.addListener(new NorwegianKeyboardListener(this, keyboard));
        
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
    public void addHotLink(IHotLink hotLink) {
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
    public void setHotLinks(List<IHotLink> hotLinks) {
        this.hotLinks = hotLinks;
    }

    public List<IHotLink> getHotLinks() {
        return hotLinks;
    }

    
    @Override
    public void bringPaletToTop() {
        // TODO Auto-generated method stub

    }

    @Override
        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
            
            if( isVisible ) {
                this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_ORTHO);
            } else {
                this.getManipulableSpatial().setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
            }
        }

    @Override
    public void toggle() {
        if( isVisible ) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    @Override
    public boolean isVisible() {
        return isVisible;
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

    @Override
    public IColourRectangle getFrameOverlay() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateOverLay() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addFrameOverlay() {
        // TODO Auto-generated method stub
        
    }


    public List<IHotSpotItem> getHotSpots() {
        return hotSpots;
    }
    
    public void setHotSpots(List<IHotSpotItem> hotSpots) {
        this.hotSpots = hotSpots;
    }

    @Override
    public void addHotSpot(IHotSpotItem hotspotItem) {
        hotSpots.add(hotspotItem);
        addItem(hotspotItem);
    }

    @Override
    public void removeHotSpot(IHotSpotItem hotspotItem) {
        logger.debug("removing hotspot from hotspotframe");
        hotSpots.remove(hotspotItem);
        ((Spatial)hotspotItem).removeFromParent();
//      removeItem(hotspotItem);
        
    }
    public void bringHotSpotsToTop() {
        if (!hotSpots.isEmpty()) {
            for (IHotSpotItem iHotSpotItem : hotSpots) {
                this.getZOrderManager().bringToTop(iHotSpotItem, null);
            }
        }
    }

    @Override
    public void setScalable(boolean canScale) {
        this.canScale  = canScale;
    }
    
    @Override
    public boolean canScale() {
        return this.canScale;
    }

    @Override
    public void createText(String text) {
        // TODO Auto-generated method stub
        
    }

}
