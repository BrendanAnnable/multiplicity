package multiplicity.csysng.behaviours;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;

import multiplicity.jmeutils.UnitConversion;
import multiplicity.csysng.items.IItem;
import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;

public class RotateTranslateScaleBehaviour implements IBehaviour, IMultiTouchEventListener {

	protected Vector2f cursor1Pos = new Vector2f();
	protected Vector2f cursor2Pos = new Vector2f();
	protected Vector2f cursor1OldPos = new Vector2f();
	protected Vector2f cursor2OldPos = new Vector2f();

	protected float maxScale = 4.0f;
	protected float minScale = 0.1f;

	private IItem item;

	private long cursor1ID = Long.MAX_VALUE;
	private long cursor2ID = Long.MAX_VALUE;
	private boolean scaleDisabled = false;

	@Override
	public void setItemActingOn(IItem item) {
		this.item = item;
		item.getMultiTouchDispatcher().addListener(this);
	}	

	@Override
	public void removeItemActingOn() {
		this.item.getMultiTouchDispatcher().remove(this);        
	}


	private void applyMultiCursorTransform() {
		Vector2f oldCenter = new Vector2f();
		oldCenter.interpolate(cursor1OldPos, cursor2OldPos, 0.5f);
		Vector2f currentCenter = new Vector2f();
		currentCenter.interpolate(cursor1Pos, cursor2Pos, 0.5f);

		float oldAngle = cursor2OldPos.subtract(cursor1OldPos).getAngle();
		float curAngle = cursor2Pos.subtract(cursor1Pos).getAngle();
		float angleChange = curAngle - oldAngle;

		Vector2f spatialLoc = item.getWorldLocation();
		Vector2f centerToSpatial = spatialLoc.subtract(oldCenter);
		float currentCenterToSpatialAngle = centerToSpatial.getAngle() + angleChange;

		float oldLength = cursor2OldPos.subtract(cursor1OldPos).length();
		float newLength = cursor2Pos.subtract(cursor1Pos).length();
		float scaleChange = newLength / oldLength;

		if(scaleDisabled  || item.getRelativeScale() * scaleChange < minScale || item.getRelativeScale() * scaleChange > maxScale) {
			scaleChange = 1f;
		}

		float newDistFromCurrentCenterToSpatial = scaleChange * centerToSpatial.length();

		float dx = newDistFromCurrentCenterToSpatial * FastMath.cos(currentCenterToSpatialAngle);
		float dy = newDistFromCurrentCenterToSpatial * FastMath.sin(currentCenterToSpatialAngle);

		Vector2f newScreenPosition = currentCenter.add(new Vector2f(dx, -dy));
		if(Float.isNaN(dx) || Float.isNaN(dy)) newScreenPosition = currentCenter;
		item.setWorldLocation(newScreenPosition);
		item.setRelativeRotation(item.getRelativeRotation() - angleChange);
		item.setRelativeScale(item.getRelativeScale() * scaleChange);

		float angle = item.getRelativeRotation();
		if(item.getRelativeRotation() <0) angle = FastMath.TWO_PI - angle;
	}

	private void applySingleCursorTransform() {
		if(cursor1ID != Long.MAX_VALUE) {
			item.setWorldLocation(item.getWorldLocation().add(cursor1Pos.subtract(cursor1OldPos)));
		}else if(cursor2ID != Long.MAX_VALUE) {
			item.setWorldLocation(item.getWorldLocation().add(cursor2Pos.subtract(cursor2OldPos)));	
		}
	}

	public void setScaleEnabled(boolean b) {
		scaleDisabled = !b;
	}

	public boolean isScaleEnabled() {
		return !scaleDisabled;
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(event.getCursorID() == cursor1ID || event.getCursorID() == cursor2ID) {
			return;
		}

		if(cursor1ID == Long.MAX_VALUE) {
			UnitConversion.tableToScreen(event.getPosition(), cursor1Pos);
			UnitConversion.tableToScreen(event.getPosition(), cursor1OldPos);
			cursor1ID = event.getCursorID();
		}else if(cursor2ID == Long.MAX_VALUE) {
			UnitConversion.tableToScreen(event.getPosition(), cursor2Pos);
			UnitConversion.tableToScreen(event.getPosition(), cursor2OldPos);
			cursor2ID = event.getCursorID();
		}		
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		updateCursor(event);

		if(getCursorCount() == 1) {			
			applySingleCursorTransform();
		}else if (getCursorCount() == 2 && event.getCursorID() == cursor2ID){			
			applyMultiCursorTransform();			
		}		
	}

	private int getCursorCount() {
		int count = 0;
		if(cursor1ID != Long.MAX_VALUE) count++;
		if(cursor2ID != Long.MAX_VALUE) count++;		
		return count;
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		if(event.getCursorID() == cursor1ID && cursor1ID != Long.MAX_VALUE) {
			cursor1ID = Long.MAX_VALUE;
		}else if(event.getCursorID() == cursor2ID && cursor2ID != Long.MAX_VALUE) {
			cursor2ID = Long.MAX_VALUE;
		}

		if(getCursorCount() == 1) {
			updateCursor(event);
			applySingleCursorTransform();
		}
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	//******

	protected void updateCursor(MultiTouchCursorEvent event) {
		if(event.getCursorID() == cursor1ID) {
			updateCursor1(event);
		}else if(event.getCursorID() == cursor2ID) {
			updateCursor2(event);
		}		
	}

	protected void updateCursor1(MultiTouchCursorEvent event) {		
		cursor1OldPos.x = cursor1Pos.x;
		cursor1OldPos.y = cursor1Pos.y;
		UnitConversion.tableToScreen(event.getPosition(), cursor1Pos);
	}

	protected void updateCursor2(MultiTouchCursorEvent event) {
		cursor2OldPos.x = cursor2Pos.x;
		cursor2OldPos.y = cursor2Pos.y;
		UnitConversion.tableToScreen(event.getPosition(), cursor2Pos);
	}



}
