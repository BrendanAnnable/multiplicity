package multiplicity.csysngjme.scene;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

public class RoundedRectangleFrame extends TriMesh {
	private static final long serialVersionUID = 2041806211113689899L;
	private float borderSize;
	private int cornerDivisions;
	
	
	/* MAP!
	 * 
	 * top left                     top right
	 *         F                  E
	 *      /-------------------------
	 *     /__|____________________|   \
	 *   H|   |G                 D |   | C
	 *    |   |                    |   |
	 *    |   |                    |   |
	 *    |   |                    |   |
	 *    |   |                    |   |
	 *    |   |                    |   |
	 *    |   |                    |   |
	 *  I |   |_J________________A_|   | B
	 *     \__|_______________________/
	 *        K                  L
	 *        
	 *  bottom left                   bottom right     
     */
	
	
	public RoundedRectangleFrame(String name, float iw, float ih, float borderSize, int cornerDivisions) {
		this(name, iw, ih, iw + (borderSize * 2), ih + (borderSize * 2), borderSize, cornerDivisions);
	}
	
	public RoundedRectangleFrame(String name, float iw, float ih, float ow, float oh, float radius, int cornerDivisions) {
		super(name);
		this.borderSize = radius;
		this.cornerDivisions = cornerDivisions;
		updateGeometry(iw, ih, ow, oh);
	}
	
	public void updateWithSize(float iw, float ih) {
		updateGeometry(iw, ih, iw + (borderSize * 2), ih + (borderSize * 2));
	}

	public void updateWithSizeAndBorder(float width, float height, float borderSize) {
		this.borderSize = borderSize;
		updateWithSize(width, height);
	}
	
	private void updateGeometry(float iw, float ih, float ow, float oh) {
		updateGeometry(iw, ih, ow, oh, this.borderSize, this.cornerDivisions);
	}

	private void updateGeometry(float iw, float ih, float ow, float oh, float radius, int cornerDivisions) {
		Vector3f[] topLeft = (Vector3f[]) getTopLeft(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] topRight = (Vector3f[]) getTopRight(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] bottomLeft = (Vector3f[]) getBottomLeft(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		Vector3f[] bottomRight = (Vector3f[]) getBottomRight(-ow/2, -oh/2, ow, oh, radius, cornerDivisions).toArray(new Vector3f[0]);
		
		setVertexCount(topLeft.length + topRight.length + bottomLeft.length + bottomRight.length + 4); // 4 inner vertices
		setVertexBuffer(BufferUtils.createVector3Buffer(getVertexCount()));
		setNormalBuffer(BufferUtils.createVector3Buffer(getVertexCount()));        
		getTextureCoords().set(0, new TexCoords(BufferUtils.createVector3Buffer(getVertexCount())));
		setTriangleQuantity(getVertexCount() + 4);
		setIndexBuffer(BufferUtils.createIntBuffer(3 * getTriangleCount()));
		for (int x = 0; x < getVertexCount(); x++) {
			getNormalBuffer().put(0).put(0).put(1);
		}

		int pos = 0;
		int positionA = pos;
		getVertexBuffer().put(iw/2).put(-ih/2).put(0); // inner bottom right

		for(int i = 0; i < bottomRight.length; i++) { // all other vertices on the bottom right
			getVertexBuffer().put(bottomRight[i].x).put(bottomRight[i].y).put(bottomRight[i].z);
		}

		for(int i = 0; i < bottomRight.length - 1; i++) {
			int first = pos; int second = pos + i + 1; int third = pos + i + 2;
			getIndexBuffer().put(first).put(second).put(third);			
		}

		pos = getVertexBuffer().position() / 3;
		int positionD = pos;
		getVertexBuffer().put(iw/2).put(ih/2).put(0); // inner top right
		for(int i = 0; i < topRight.length; i++) { // all other vertices on the bottom right
			getVertexBuffer().put(topRight[i].x).put(topRight[i].y).put(topRight[i].z);
		}

		for(int i = 0; i < topRight.length - 1; i++) {
			int first = pos; int second = pos + i + 1; int third = pos + i + 2;
			getIndexBuffer().put(first).put(second).put(third);			
		}


		pos = getVertexBuffer().position() / 3;
		int positionG = pos;
		getVertexBuffer().put(-iw/2).put(ih/2).put(0); // inner top left
		for(int i = 0; i < topLeft.length; i++) { // all other vertices on the bottom right
			getVertexBuffer().put(topLeft[i].x).put(topLeft[i].y).put(topLeft[i].z);
		}

		for(int i = 0; i < topLeft.length - 1; i++) {
			int first = pos; int second = pos + i + 1; int third = pos + i + 2;
			getIndexBuffer().put(first).put(second).put(third);			
		}

		pos = getVertexBuffer().position() / 3;
		int positionJ = pos;
		getVertexBuffer().put(-iw/2).put(-ih/2).put(0); // inner bottom left
		for(int i = 0; i < bottomLeft.length; i++) { // all other vertices on the bottom right
			getVertexBuffer().put(bottomLeft[i].x).put(bottomLeft[i].y).put(bottomLeft[i].z);
		}

		for(int i = 0; i < bottomLeft.length - 1; i++) {
			int first = pos; int second = pos + i + 1; int third = pos + i + 2;
			getIndexBuffer().put(first).put(second).put(third);			
		}
		
		int positionB = positionA + bottomRight.length;
		int positionC = positionD + 1;
		int positionE = positionD + topRight.length;
		int positionF = positionE + 2;
		int positionH = positionG + topLeft.length;
		int positionI = positionH + 2;
		int positionK = positionJ + bottomLeft.length;
		int positionL = positionA + 1;
		
		// join the corners
		getIndexBuffer().put(positionA).put(positionB).put(positionC);
		getIndexBuffer().put(positionA).put(positionC).put(positionD);
		getIndexBuffer().put(positionD).put(positionE).put(positionF);
		getIndexBuffer().put(positionD).put(positionF).put(positionG);
		getIndexBuffer().put(positionG).put(positionH).put(positionI);
		getIndexBuffer().put(positionG).put(positionI).put(positionJ);
		getIndexBuffer().put(positionJ).put(positionK).put(positionL);
		getIndexBuffer().put(positionJ).put(positionL).put(positionA);
	}


	public static List<Vector3f> getBottomRight(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + w-radius, y, 0));
		float t = FastMath.PI * 1.5f;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));
		}
		float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));
		return list;
	}

	public static List<Vector3f> getTopRight(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + w, y + h - radius, 0)); 
		float t = 0;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + w - radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + h -radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0f));
		}
		float sx = x + w - radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + h -radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0f));		
		return list;
	}

	public static List<Vector3f> getTopLeft(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x + radius, y + h, 0));
		float t = FastMath.PI * 0.5f;
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x  + radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + h - radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));			
		}
		float sx = x  + radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + h - radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));	
		return list;
	}

	public static List<Vector3f> getBottomLeft(float x, float y, float w, float h, float radius, int divisions) {
		List<Vector3f> list = new ArrayList<Vector3f>();
		list.add(new Vector3f(x, y + radius, 0)); 
		float t = FastMath.PI;				
		float increment = FastMath.PI / 2f / divisions;
		for(int i = 0; i < divisions; i++) {
			float sx = x + radius + FastMath.cos(t + (i * increment)) * radius;
			float sy = y + radius + FastMath.sin(t + (i * increment)) * radius;
			list.add(new Vector3f(sx, sy, 0));
		}
		float sx = x + radius + FastMath.cos(t + FastMath.PI/2) * radius;
		float sy = y + radius + FastMath.sin(t + FastMath.PI/2) * radius;
		list.add(new Vector3f(sx, sy, 0));		
		return list;
	}

}
