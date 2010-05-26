package multiplicity.csysngjme.items;

import java.util.UUID;

import com.jme.math.Vector2f;

import multiplicity.csysng.items.IRectangularItem;

public abstract class JMERectangularItem extends JMEItem implements IRectangularItem {
	private static final long serialVersionUID = 60718470575127512L;

	private Vector2f size = new Vector2f(1f, 1f);
	
	public JMERectangularItem(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public void setSize(float width, float height) {
		this.size.x = width;
		this.size.y = height;
	}
	
	@Override
	public void setSize(Vector2f size) {
		this.size.x = size.x;
		this.size.y = size.y;
	}
	
	@Override
	public Vector2f getSize() {
		return size;
	}
	
	@Override
	public float getWidth() {
		return size.x;
	}
	
	@Override
	public float getHeight() {
		return size.y;
	}

}
