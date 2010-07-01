package multiplicity.csysngjme.items.hotspots;

import java.nio.FloatBuffer;

import multiplicity.csysng.items.hotspot.IHotLink;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.util.geom.BufferUtils;

public class HotLink extends Line implements IHotLink {
    
	private static final long serialVersionUID = 1677415896709510948L;
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
	    vertices[0] = new Vector3f(vertex.x, vertex.y, 0f);
	    this.redrawLine(vertices);
	}
	
	public void redrawTargetLocation(Vector2f vertex) {
		vertices[1] = new Vector3f(vertex.x, vertex.y, 0f);
	    this.redrawLine(vertices);
    }
	
	/* (non-Javadoc)
     * @see multiplicity.csysngjme.items.hotspots.IHotLink#redrawLine(com.jme.math.Vector3f[])
     */
	public void redrawLine(Vector3f[] vertices) {
	    FloatBuffer fBuffer = BufferUtils.createFloatBuffer(vertices);                    
        this.reconstruct(fBuffer, null, null, null);
        this.setSolidColor(ColorRGBA.red);  
	}


    @Override
    public boolean isVisable() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void setVisable(boolean isVisable) {
        // TODO Auto-generated method stub
        
    }
}
