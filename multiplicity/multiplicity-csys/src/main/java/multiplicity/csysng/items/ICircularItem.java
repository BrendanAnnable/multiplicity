package multiplicity.csysng.items;

import com.jme.math.Vector2f;

public interface ICircularItem extends IItem {
	public void setSize(float width, float height);
	public void setSize(Vector2f size);
	public Vector2f getSize();
	public float getWidth();
	public float getHeight();
}
