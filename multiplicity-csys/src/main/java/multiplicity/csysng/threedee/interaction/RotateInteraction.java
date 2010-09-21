package multiplicity.csysng.threedee.interaction;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class RotateInteraction implements IMultiTouchEventListener {

	private Spatial spatial;
	private long currentCursorID;
	private Vector2f cursorPressedPosition;
	private Quaternion initialRotation;

	public RotateInteraction(Spatial s) {
		this.spatial = s;
	}


	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		this.currentCursorID = -1;
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		this.currentCursorID = event.getCursorID();
		this.cursorPressedPosition = event.getPosition();
		this.initialRotation = spatial.getLocalRotation().clone();
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	// TODO: make this a meaningful 3D rotation
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		if(currentCursorID == event.getCursorID()) {
			Vector2f change = event.getPosition().subtract(cursorPressedPosition);
			float yaw = -FastMath.PI * change.y;
			float roll = FastMath.PI * change.x;
			float pitch = 0;
			Quaternion rot = new Quaternion().fromAngles(yaw, roll, pitch);			
			spatial.getLocalRotation().set(initialRotation.mult(rot));
		}
	}
}