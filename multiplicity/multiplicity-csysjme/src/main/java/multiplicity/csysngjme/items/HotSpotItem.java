package multiplicity.csysngjme.items;

import java.awt.Color;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.jme.math.Vector3f;

import multiplicity.csysng.behaviours.BehaviourMaker;
import multiplicity.csysng.gfx.Gradient;
import multiplicity.csysng.gfx.Gradient.GradientDirection;
import multiplicity.csysng.items.IFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.behaviours.RotateTranslateScaleBehaviour;
import multiplicity.csysngjme.factory.hotspot.HotSpotContentItemFactory;

public class HotSpotItem extends JMEColourRectangle implements IHotSpotItem {

	private static final long serialVersionUID = 3685342474539036654L;
	private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

	boolean isOpen;
    protected String link;
    IHotSpotItem relationHotSpot;
    
    
    public HotSpotItem(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
    }

    @Override
    public void initializeGeometry() {
        super.initializeGeometry();
    }
    @Override
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }


    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public IHotSpotItem getRelationHotSpot() {
        return relationHotSpot;
    }

    @Override
    public void createLink(IHotSpotItem relationHotSpot) {
        this.relationHotSpot = relationHotSpot;
    }

    @Override
    public void createCallBackHotSpotFrame() {
//    	logger.info(this.getParent().getParent().getParent());
//    	UUID uUID = UUID.randomUUID();
//    	
//    	//scale image.
//	    Vector3f localScale = bi.getLocalScale();
//	    
//	    float newX = bi.getSize().x * localScale.x;
//	    float newY = bi.getSize().y * localScale.y;
//	    
//	    HotSpotContentItemFactory hotSpotContentItemFactory = new HotSpotContentItemFactory();
//	    IFrame frame = hotSpotContentItemFactory.createHotSpotFrame("hotspotcallbackframe-"+uUID, uUID, Float.valueOf(newX).intValue(), Float.valueOf(newY).intValue());
//        
//        frame.setBorder(new JMERoundedRectangleBorder("randomframeborder", UUID.randomUUID(), 40f, 15));
//        frame.setGradientBackground(new Gradient(new Color(0.5f, 0.5f, 0.5f, 0.8f), new Color(0f, 0f, 0f, 0.8f), GradientDirection.VERTICAL));
//        frame.maintainBorderSizeDuringScale();
//        BehaviourMaker.addBehaviour(frame, RotateTranslateScaleBehaviour.class);
//
//        (this.getParent().getParent().getParent()).add(frame);
    }

}
