package multiplicity.csysng.items;

import multiplicity.csysng.items.hotspot.IHotSpotItem;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

 public interface ILineItem extends IItem{
	public void setSize(float width, float height);
	public void setSize(Vector2f size);
	public Vector2f getSize();
	public float getWidth();
	public float getHeight();
	public void redrawTargetLocation(Vector2f relativeLocation);
	public void redrawLine(Vector3f[] vertices);
	public IHotSpotItem getHotSpotItem();

}
