package multiplicity.csysngjme.items.hotspots;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.UUID;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Line.Mode;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;
import multiplicity.csysngjme.items.JMEFrame;

public class HotSpotFrame extends JMEFrame implements IHotSpotFrame {
	
	private static final long serialVersionUID = 8114328886119432460L;
	
	protected ArrayList<IHotSpotItem> hotSpots = new ArrayList<IHotSpotItem>(); 
	protected ArrayList<Line> lines = new ArrayList<Line>(); ;

	public HotSpotFrame(String name, UUID uuid, int width, int height) {
		super(name, uuid, width, height);
	}

	public ArrayList<IHotSpotItem> getHotSpots() {
		return hotSpots;
	}
	
	public void setHotSpots(ArrayList<IHotSpotItem> hotSpots) {
		this.hotSpots = hotSpots;
	}

	public void addHotSpot(IItem item) {
		hotSpots.add((IHotSpotItem) item);
		connectHotSpots();
	}

	public void bringHotSpotsToTop() {
		for (IHotSpotItem iHotSpotItem : hotSpots) {
			this.getZOrderManager().bringToTop(iHotSpotItem, null);  
		}
	}

	public void connectHotSpots() {
		if(hotSpots.size() > 1) {
			IHotSpotItem ihotSpotItem1 = hotSpots.get(0);
			final IHotSpotItem ihotSpotItem2 = hotSpots.get(1);
			
			// TODO Auto-generated method stub
			if(lines.size() > 0) {
				this.detachChild(lines.get(0));
				lines = new ArrayList<Line>();
			}
			
			//get the location of the 2 hotspots relative to the parent frame
			Vector2f xyHS1 = ihotSpotItem1.getRelativeLocation();
			Vector2f xyHS2 = ihotSpotItem2.getRelativeLocation();
			
			Vector3f[] vertices = new Vector3f[2];
			vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
			vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
			final Line line = new Line("link", vertices, null, null, null);
			line.setMode(Mode.Connected);
			line.setLineWidth(1f);
			line.setSolidColor(ColorRGBA.red);		
			this.attachChild(line);
			lines.add(line);
			
			
			ihotSpotItem1.addItemListener(new ItemListenerAdapter() {
                
                public void itemMoved(IItem item) {
                    
                    Vector2f xyHS1 = item.getRelativeLocation();
                    Vector2f xyHS2 = ihotSpotItem2.getRelativeLocation();
                    Vector3f[] vertices = new Vector3f[2];

                    vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
                    vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
//                    line.reconstruct(vertices, null, null, null);
                    
                };
                
                
                
            } );
			
			
		}
	}
}
