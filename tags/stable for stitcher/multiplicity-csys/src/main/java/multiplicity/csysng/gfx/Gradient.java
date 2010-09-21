package multiplicity.csysng.gfx;

import java.awt.Color;

public class Gradient {
	private Color from;
	private Color to;
	private GradientDirection direction;

	public Gradient(Color from, Color to, GradientDirection direction) {
		this.setFrom(from);
		this.setTo(to);
		this.setDirection(direction);
	}
	
	public void setFrom(Color from) {
		this.from = from;
	}

	public Color getFrom() {
		return from;
	}

	public void setDirection(GradientDirection direction) {
		this.direction = direction;
	}

	public GradientDirection getDirection() {
		return direction;
	}

	public void setTo(Color to) {
		this.to = to;
	}

	public Color getTo() {
		return to;
	}

	public static enum GradientDirection {
		VERTICAL,
		HORIZONTAL,
		DIAGONAL
	}
}
