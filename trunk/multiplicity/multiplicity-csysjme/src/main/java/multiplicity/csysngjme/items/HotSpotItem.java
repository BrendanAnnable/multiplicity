package multiplicity.csysngjme.items;

import java.nio.FloatBuffer;
import java.util.UUID;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;

import org.apache.log4j.Logger;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Line.Mode;
import com.jme.util.geom.BufferUtils;

public class HotSpotItem extends JMEColourRectangle implements IHotSpotItem {

	private static final long serialVersionUID = 3685342474539036654L;
	private final static Logger logger = Logger.getLogger(HotSpotItem.class.getName());

	boolean isOpen;
    protected String link;
    IHotSpotItem relationHotSpot;
    private IHotSpotFrame hotSpotFrameContent;
    
    
    public HotSpotItem(String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
    }

    public HotSpotItem(IHotSpotFrame hotSpotFrameContent, String name, UUID uuid, int width, int height) {
        super(name, uuid, width, height);
        this.setHotSpotFrameContent(hotSpotFrameContent);
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
    public Line createLink() {

		//get the location of the 2 hotspots relative to the parent frame
		Vector2f xyHS1 = this.getRelativeLocation();
		Vector2f xyHS2 = hotSpotFrameContent.getRelativeLocation();
		
		Vector3f[] vertices = new Vector3f[2];
		vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
		vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
		final Line line = new Line("link", vertices, null, null, null);
		line.setMode(Mode.Connected);
		line.setLineWidth(1f);
		line.setSolidColor(ColorRGBA.red);		
		//this.attachChild(line);			
		
		this.addItemListener(new ItemListenerAdapter() {
            
            public void itemMoved(IItem item) {
                
                Vector2f xyHS1 = item.getRelativeLocation();
                Vector2f xyHS2 = hotSpotFrameContent.getRelativeLocation();
                Vector3f[] vertices = new Vector3f[2];

                vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
                vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
                
                FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
                line.reconstruct(fBuffer, null, null, null);
                line.setSolidColor(ColorRGBA.red);	
            };
                   
        } );
		
		hotSpotFrameContent.addItemListener(new ItemListenerAdapter() {
            
            public void itemMoved(IItem item) {
                
                Vector2f xyHS1 = getRelativeLocation();
                Vector2f xyHS2 = item.getRelativeLocation();
                Vector3f[] vertices = new Vector3f[2];

                vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
                vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
                
                FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
                line.reconstruct(fBuffer, null, null, null);
                line.setSolidColor(ColorRGBA.red);	
                
            };
        } );
    	
    	return line;
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

    public void setHotSpotFrameContent(IHotSpotFrame hotSpotFrameContent) {
        this.hotSpotFrameContent = hotSpotFrameContent;
        this.createLink();
    }

    public IHotSpotFrame getHotSpotFrameContent() {
        return hotSpotFrameContent;
    }

}
