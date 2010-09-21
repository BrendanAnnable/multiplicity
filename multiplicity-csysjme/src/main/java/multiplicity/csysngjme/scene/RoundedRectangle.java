package multiplicity.csysngjme.scene;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

/**
 * A Rounded rectangle TriMesh. Can be created with any corner set to
 * round or cornered via use of CornerStyle. At any time, the geometry
 * @author dcs0ah1
 *
 */
public class RoundedRectangle extends TriMesh {
	private static final long serialVersionUID = 2041806211113689899L;

	/**
	 * Create a 2D rounded rectangle.
	 * @param name a suitable name
	 * @param w total width of the rectangle
	 * @param h total height of the rectangle
	 * @param radius corner radius
	 * @param divisions number of steps used to make each corner. Higher numbers are smoother. Try 8 as a start.
	 */
	public RoundedRectangle(String name, float w, float h, float radius, int divisions) {
		super(name);
		updateGeometry(w, h, radius, divisions);
	}

	/**
	 * Create a 2D rounded rectangle.
	 * @param name a suitable name
	 * @param w total width of the rectangle
	 * @param h total height of the rectangle
	 * @param radius corner radius
	 * @param divisions number of steps used to make each corner. Higher numbers are smoother. Try 8 as a start.
	 * @param style style of corner.
	 */
	public RoundedRectangle(String name, float w, float h, float radius, int divisions, CornerStyle style) {
		super(name);
		updateGeometry(w, h, radius, divisions, style);
	}

	/**
	 * Redefines the geometry of the rounded rectangle.
	 * @param w total width of the rectangle
	 * @param h total height of the rectangle
	 * @param radius corner radius
	 * @param divisions number of steps used to make each corner. Higher numbers are smoother. Try 8 as a start.
	 */
	public void updateGeometry(float w, float h, float radius, int divisions) {
		CornerStyle style = new CornerStyle();
		updateGeometry(w, h, radius, divisions, style);
	}

	/**
	 * Redefines the geometry of the rounded rectangle.
	 * @param w total width of the rectangle
	 * @param h total height of the rectangle
	 * @param radius corner radius
	 * @param divisions number of steps used to make each corner. Higher numbers are smoother. Try 8 as a start.
	 * @param style style of corner.
	 */
	public void updateGeometry(float w, float h, float radius, int divisions, CornerStyle style) {
		Vector3f[] outline = generateRoundedRectanglePoints(-w/2, -h/2, w, h, radius, divisions, style);
		setVertexCount(outline.length + 1); // one for the middle
		setVertexBuffer(BufferUtils.createVector3Buffer(getVertexCount()));
		setNormalBuffer(BufferUtils.createVector3Buffer(getVertexCount()));        
		getTextureCoords().set(0, new TexCoords(BufferUtils.createVector3Buffer(getVertexCount())));
		setTriangleQuantity(outline.length);
		setIndexBuffer(BufferUtils.createIntBuffer(3 * getTriangleCount()));

		for (int x = 0; x < getVertexCount(); x++) {
			getNormalBuffer().put(0).put(0).put(1);
		}

		getVertexBuffer().put(0).put(0).put(0); // center of rounded rectangle

		for(int i = 0; i < outline.length; i++) { // all other vertices around the edge
			getVertexBuffer().put(outline[i].x).put(outline[i].y).put(outline[i].z);
		}

		for(int i = 0; i < getTriangleCount(); i++) {
			int first = 0; int second = i + 1; int third = i + 2;
			if(third > getVertexCount() - 1)  third = 1;			
			getIndexBuffer().put(first).put(second).put(third);
		}
	}

	private static Vector3f[] generateRoundedRectanglePoints(float x, float y, float w, float h, float radius, int divisions, CornerStyle style) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		float t = 0f;

		if(style.bottomRight == CornerStyle.ROUNDED) {
			list.add(new Vector3f(x + w-radius, y, 0));
			t = FastMath.PI * 1.5f;
			float increment = FastMath.PI / 2f / divisions;
			for(int i = 0; i < divisions; i++) {
				float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
				float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
				list.add(new Vector3f(sx, sy, 0));
			}
			float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
			float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
			list.add(new Vector3f(sx, sy, 0));
		}else{
			list.add(new Vector3f(x+w,y, 0));
		}

		if(style.topRight == CornerStyle.ROUNDED) {
			list.add(new Vector3f(x + w, y + h - radius, 0));
			t = 0;
			float increment = FastMath.PI / 2f / divisions;
			for(int i = 0; i < divisions; i++) {
				float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
				float sy = y + h -radius + FastMath.sin(t + (i * increment)) * radius;
				list.add(new Vector3f(sx, sy, 0f));
			}
			float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
			float sy = y + h -radius + FastMath.sin(t + FastMath.PI/2) * radius;
			list.add(new Vector3f(sx, sy, 0f));	
		}else{
			list.add(new Vector3f(x + w, y + h, 0));
		}

		if(style.topLeft == CornerStyle.ROUNDED) {
			list.add(new Vector3f(x + radius, y + h, 0));
			t = FastMath.PI * 0.5f;
			float increment = FastMath.PI / 2f / divisions;
			for(int i = 0; i < divisions; i++) {
				float sx = x  + radius + FastMath.cos(t + (i * increment)) * radius;
				float sy = y + h - radius + FastMath.sin(t + (i * increment)) * radius;
				list.add(new Vector3f(sx, sy, 0));			
			}
			float sx = x  + radius + FastMath.cos(t + FastMath.PI/2) * radius;
			float sy = y + h - radius + FastMath.sin(t + FastMath.PI/2) * radius;
			list.add(new Vector3f(sx, sy, 0));	
		}else{
			list.add(new Vector3f(x, y + h, 0));
		}

		if(style.bottomLeft == CornerStyle.ROUNDED) {
			list.add(new Vector3f(x, y + radius, 0)); 
			t = FastMath.PI;				
			float increment = FastMath.PI / 2f / divisions;
			for(int i = 0; i < divisions; i++) {
				float sx = x + radius + FastMath.cos(t + (i * increment)) * radius;
				float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
				list.add(new Vector3f(sx, sy, 0));
			}
			float sx = x + radius + FastMath.cos(t + FastMath.PI/2) * radius;
			float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
			list.add(new Vector3f(sx, sy, 0));	
		}else{
			list.add(new Vector3f(x, y, 0));
		}

		return (Vector3f[]) list.toArray(new Vector3f[0]);
	}

	public static class CornerStyle {
		public static final int CORNER = 0;
		public static final int ROUNDED = 1;

		public int topLeft = ROUNDED;
		public int topRight = ROUNDED;
		public int bottomLeft = ROUNDED;
		public int bottomRight = ROUNDED;

		public CornerStyle() {
			this(ROUNDED, ROUNDED, ROUNDED, ROUNDED);
		}

		public CornerStyle(int topLeft, int topRight, int bottomLeft, int bottomRight) {
			this.topLeft = topLeft;
			this.topRight = topRight;
			this.bottomLeft = bottomLeft;
			this.bottomRight = bottomRight;
		}
	}
}
