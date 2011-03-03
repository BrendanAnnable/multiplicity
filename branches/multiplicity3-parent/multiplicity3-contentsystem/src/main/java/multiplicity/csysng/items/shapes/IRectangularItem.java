package multiplicity.csysng.items.shapes;

import multiplicity.csysng.items.item.IItem;

import com.jme3.math.Vector2f;

public interface IRectangularItem extends IItem {
	public void setSize(float width, float height);
	public void setSize(Vector2f size);
	public Vector2f getSize();
	public float getWidth();
	public float getHeight();
}
