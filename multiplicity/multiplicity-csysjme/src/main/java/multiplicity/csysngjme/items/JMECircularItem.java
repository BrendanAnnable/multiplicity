package multiplicity.csysngjme.items;

import java.util.UUID;

import multiplicity.csysng.items.ICircularItem;

import com.jme.math.Vector2f;

public abstract class JMECircularItem extends JMEItem implements ICircularItem{

	private static final long serialVersionUID = 7887896157766135976L;
	
	private Vector2f size = new Vector2f(1f, 1f);
	
	public JMECircularItem(String name, UUID uuid) {
		super(name, uuid);
		// TODO Auto-generated constructor stub
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
