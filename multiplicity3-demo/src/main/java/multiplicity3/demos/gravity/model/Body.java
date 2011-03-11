package multiplicity3.demos.gravity.model;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

public class Body {

	

	
	public double mass;
	public Vector2f velocity;
	private IItem representation;

	public Body(IItem representation, double mass, Vector2f position, Vector2f velocity) {
		this.representation = representation;
		this.mass = mass;
		this.velocity = velocity;
	}
	
	public void setPosition(Vector2f pos) {
		representation.setRelativeLocation(pos);
	}

	public Vector2f getPosition() {
		return representation.getRelativeLocation();
	}

	public String getName() {
		return representation.getName();
	}

	public void setPosition(float x, float y) {
		representation.setRelativeLocation(new Vector2f(x,y));		
	}
}
