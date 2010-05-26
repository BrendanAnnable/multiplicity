package multiplicity.input.data;

import com.jme.math.Vector2f;

/**
 * Stores history instance of a cursor position in screen coordinates with time in millis
 * @author dcs0ah1
 *
 */
public class CursorPositionRecord {
	private long timeMillis;
	private Vector2f position;

	public CursorPositionRecord(Vector2f position, long timeMillis) {
		this.setPosition(position);
		this.setTimeMillis(timeMillis);
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getPosition() {
		return position;
	}
}
