package multiplicity.csysng.animation.elements.behaviourelements;

import com.jme3.math.Vector2f;

import multiplicity.csysng.animation.elements.AnimationElement;
import multiplicity.csysng.items.item.IItem;

public class InertiaAnimationElement extends AnimationElement {
	
	private IItem item;
	private boolean finished;
	private Vector2f currentVelocity;
	private float dragFactor = 1f;

	public InertiaAnimationElement(IItem item) {
		this.item = item;
	}

	/*
	 * Value > 1 for less than default drag, value < 1 for more than default drag.
	 */
	public void setDragFactor(float drag) {
		this.dragFactor = drag;
	}
	
	public float getDragFactor() {
		return this.dragFactor;
	}

	@Override
	public void elementStart(float tpf) {}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void updateAnimationState(float tpf) {
		item.setRelativeLocation(item.getRelativeLocation().add(currentVelocity.mult(tpf)));
		
		Vector2f reduceBy = currentVelocity.mult(1/dragFactor  * tpf);
		currentVelocity.subtractLocal(reduceBy);
		
		if(currentVelocity.length() < 1f) {
			finished = true;
		}
	}

	public void moveWithVelocity(Vector2f velocity) {
		this.currentVelocity = velocity.clone();
		this.finished = false;
	}

}
