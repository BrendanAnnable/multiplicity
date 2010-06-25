package multiplicity.csysngjme.items.hotspots;

import java.nio.FloatBuffer;

import multiplicity.csysng.items.IItem;
import multiplicity.csysng.items.events.ItemListenerAdapter;
import multiplicity.csysng.items.hotspot.IHotLink;
import multiplicity.csysng.items.hotspot.IHotSpotFrame;
import multiplicity.csysng.items.hotspot.IHotSpotItem;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Line.Mode;
import com.jme.util.geom.BufferUtils;

public class HotLink extends Line implements IHotLink {
    
	public Vector3f[] vertices;

    public HotLink(Vector3f[] vertices) {
        super("Link", vertices, null, null, null);
	    this.vertices = vertices;
        this.setMode(Mode.Connected);
        this.setLineWidth(4f);
        this.setSolidColor(ColorRGBA.red);  
        this.setAntialiased(true);
	}
	
	
	public void redrawSourceLocation(Vector2f vertex) {
	    System.out.println("OLD source line loc " + vertices[0]  );
	    vertices[0] = new Vector3f(vertex.x, vertex.y, 0f);
	    this.redrawLine(vertices);
	    System.out.println("New source line loc " + vertices[0]  );

	}
	
	public void redrawTargetLocation(Vector2f vertex) {
	    System.out.println("OLD targe line loc " + vertices[1]  );
	    vertices[1] = new Vector3f(vertex.x, vertex.y, 0f);
	    this.redrawLine(vertices);
	    System.out.println("NEW targe line loc " + vertices[1]  );
	    
    }
	
	/* (non-Javadoc)
     * @see multiplicity.csysngjme.items.hotspots.IHotLink#redrawLine(com.jme.math.Vector3f[])
     */
	public void redrawLine(Vector3f[] vertices) {
	    FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
        this.reconstruct(fBuffer, null, null, null);
        this.setSolidColor(ColorRGBA.blue);  
	}
}
