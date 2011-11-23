package multiplicity3.csys.behaviours.inertia;

import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

// TODO have options for limiting the amount of history recorded
public class ItemPositionHistory {
	
	private static final int SAMPLE_LIMIT = 4;
	private static final float THRESHOLD = 100;
	
	public IItem item;
	public List<PositionTime> positions;
	public Vector2f velocity = new Vector2f(0f, 0f);
	
	public ItemPositionHistory(IItem item) {
		this.item = item;
		this.positions = new ArrayList<PositionTime>();
	}
	
	Vector2f posTemp = new Vector2f();
	private IStage stage;
	public void add(Vector2f position, long timeStampMillis) {
		stage.tableToScreen(position, posTemp);		
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
	
	private void updateVelocity() {
		if(positions.size() < SAMPLE_LIMIT) return;
		
		PositionTime earlier = null, later = null;

		earlier = positions.get(positions.size() - SAMPLE_LIMIT);
		later = positions.get(positions.size() - 1);
		
		later.pos.subtract(earlier.pos, velocity);
		float timeSeconds =  (later.timeStampMillis - earlier.timeStampMillis);
		
		if (timeSeconds > THRESHOLD){
			clear();
		}else{
			velocity.multLocal(timeSeconds);
		}

			
	}
	
	public void clear() {
		positions.clear();		
		velocity = new Vector2f(0f, 0f);
	}

	public static class PositionTime {
		public Vector2f pos;
		public long timeStampMillis;
		
		public PositionTime(Vector2f pos, long timeMillis) {
			this.pos = pos;
			this.timeStampMillis = timeMillis;
		}
	}

	public void setStage(IStage stage) {
		this.stage = stage;
		
	}

	
}
