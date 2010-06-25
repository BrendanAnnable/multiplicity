package multiplicity.appgallery.stitcher;

import java.nio.FloatBuffer;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Line.Mode;
import com.jme.util.geom.BufferUtils;

public class HotLink {
	public HotLink(Vector3f[] vertices) {
//		// TODO Auto-generated constructor stub
//		final IItem ihotSpotItem1 = iHotSpotItem;
//		final IItem ihotSpotItem2 = iHotSpotFrame;
//		
//		//get the location of the 2 hotspots relative to the parent frame
//		Vector2f xyHS1 = ihotSpotItem1.getRelativeLocation();
//		Vector2f xyHS2 = ihotSpotItem2.getRelativeLocation();
//		
//		Vector3f[] vertices = new Vector3f[2];
//		vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
//		vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
//		final Line line = new Line("link", vertices, null, null, null);
//		line.setMode(Mode.Connected);
//		line.setLineWidth(1f);
//		line.setSolidColor(ColorRGBA.red);		
//		this.attachChild(line);			
//		
//		ihotSpotItem1.addItemListener(new ItemListenerAdapter() {
//            
//            public void itemMoved(IItem item) {
//                
//                Vector2f xyHS1 = item.getRelativeLocation();
//                Vector2f xyHS2 = ihotSpotItem2.getRelativeLocation();
//                Vector3f[] vertices = new Vector3f[2];
//
//                vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
//                vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
//                
//                FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
//                line.reconstruct(fBuffer, null, null, null);
//                line.setSolidColor(ColorRGBA.red);	
//            };
//                   
//        } );
//		
//		ihotSpotItem2.addItemListener(new ItemListenerAdapter() {
//            
//            public void itemMoved(IItem item) {
//                
//                Vector2f xyHS1 = ihotSpotItem1.getRelativeLocation();
//                Vector2f xyHS2 = item.getRelativeLocation();
//                Vector3f[] vertices = new Vector3f[2];
//
//                vertices[0] = new Vector3f(xyHS1.x, xyHS1.y, 0f);
//                vertices[1] = new Vector3f(xyHS2.x, xyHS2.y, 0f);
//                
//                FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
//                line.reconstruct(fBuffer, null, null, null);
//                line.setSolidColor(ColorRGBA.red);	
//                
//            };
//        } );
	}
}
