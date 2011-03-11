package multiplicity3.demos.gravity.model;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.elements.AnimationElement;

public class Universe extends AnimationElement {
	
	//public static final double MASS_SUN = 1.98892e30;
	//public static final double MASS_EARTH = 5.9736e24;
	
	public static final double MASS_SUN = 1e6;
	public static final double MASS_EARTH = 1;
	public static final double G = 1e5;
	public static final double TIME_SCALE = 0.001;	
	
	public float minimum_distance_apart = 1f;	
	
	private List<Body> bodies;
	
	public Universe() {
		bodies = new ArrayList<Body>();
	}
	
	public void addBody(Body b) {
		this.bodies.add(b);
	}

	@Override
	public void updateAnimationState(float tpf) {
		if(bodies.size() < 2) return;
		
		double universeTime = tpf * TIME_SCALE;
		Body a, b;
		Vector2f oldVelocity = new Vector2f();
		for(int i = 0; i < bodies.size(); i++) {			
			a = bodies.get(i);
			oldVelocity.set(a.velocity.x, a.velocity.y);			

			double accel_x = 0;
			double accel_y = 0;
			double dist_x = 0;
			double dist_y = 0;
			for(int j = 0; j < bodies.size(); j++) {				
				if(i != j) {
					b = bodies.get(j);	
					dist_x = b.getPosition().x - a.getPosition().x;
					dist_y = b.getPosition().y - a.getPosition().y;					
					double r2 = b.getPosition().subtract(a.getPosition()).length();
					r2 = r2 * r2;					
					double force = G * b.mass / r2;
					accel_x += dist_x * force;
					accel_y += dist_y * force;
				}				
			}
			
			accel_x /= bodies.size() - 1;
			accel_y /= bodies.size() - 1;
			
			a.velocity.addLocal((float) (universeTime * accel_x), (float) (universeTime * accel_y));
			
		}
		
		for(int i = 0; i < bodies.size(); i++) {
			a = bodies.get(i);
			a.setPosition(
					a.getPosition().x + (float)(a.velocity.x * universeTime),
					a.getPosition().y + (float)(a.velocity.y * universeTime));
		}		
	}

	@Override
	public void reset() {}

	@Override
	public void elementStart(float tpf) {}

	@Override
	public boolean isFinished() {
		return false;
	}

	public boolean canAddMore() {
		return bodies.size() < 20;
	}
}
