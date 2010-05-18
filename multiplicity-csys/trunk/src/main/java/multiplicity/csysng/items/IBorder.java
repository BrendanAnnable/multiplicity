package multiplicity.csysng.items;

import com.jme.math.Vector2f;

public interface IBorder extends IItem {
	public Vector2f getSize();
	public void setSize(Vector2f size);
	public void setSize(float width, float height);
	public void setBorderWidth(float borderSize);
	public float getBorderWidth();
}
