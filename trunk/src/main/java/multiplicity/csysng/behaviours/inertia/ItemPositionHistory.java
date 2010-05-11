package multiplicity.csysng.behaviours.inertia;

import java.util.ArrayList;
import java.util.List;

import multiplicity.app.utils.UnitConversion;
import multiplicity.csysng.items.IItem;

import com.jme.math.Vector2f;

// TODO have options for limiting the amount of history recorded
public class ItemPositionHistory {
	public IItem item;
	public List<PositionTime> positions;
	public Vector2f velocity = new Vector2f(0f, 0f);
	
	public ItemPositionHistory(IItem item) {
		this.item = item;
		this.positions = new ArrayList<PositionTime>();
	}
	
	Vector2f posTemp = new Vector2f();
	public void add(Vector2f position, long timeStampMillis) {
		UnitConversion.tableToScreen(position, posTemp);
		positions.add(new PositionTime(posTemp.clone(), timeStampMillis));
	}
	
	/**
	 * screen units per second
	 * @return
	 */
	public Vector2f getVelocity() {
		updateVelocity();
		return velocity;
	}
	
	// TODO a much better velocity calculation needed!!!
	private void updateVelocity() {
		if(positions.size() < 2) return;
		
		PositionTime earlier = null, later = null;
		if(positions.size() < 3) {
			later = positions.get(1);
			earlier = positions.get(0);
		}else if(positions.size() > 10){
			later = positions.get(positions.size() - 9);
			earlier = positions.get(0);
		}else{
			later = positions.get(positions.size() - 1);
			earlier = positions.get(0);
		}
		later.pos.subtract(earlier.pos, velocity);
		float timeSeconds =  (later.timeStampMillis - earlier.timeStampMillis) / 1000f;
		velocity.multLocal(timeSeconds);
	}
	
	public void clear() {
		positions.clear();		
	}

	public static class PositionTime {
		public Vector2f pos;
		private long timeStampMillis;
		
		public PositionTime(Vector2f pos, long timeMillis) {
			this.pos = pos;
			this.timeStampMillis = timeMillis;
		}
	}

	
}
