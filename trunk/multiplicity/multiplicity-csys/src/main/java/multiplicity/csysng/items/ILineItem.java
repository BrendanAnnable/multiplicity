package multiplicity.csysng.items;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

public interface ILineItem {
	public void setSize(float width, float height);
	public void setSize(Vector2f size);
	public Vector2f getSize();
	public float getWidth();
	public float getHeight();
	public void redrawTargetLocation(Vector2f relativeLocation);
	public void redrawLine(Vector3f[] vertices);

}
