package multiplicity.csysng.items.keyboard.defs.simple;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import multiplicity.csysng.items.keyboard.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.KeyboardKey;

public class SimpleAlphaKeyboardRenderer {
	
	private Font keyboardFont = new Font("Arial", Font.BOLD, 12);
	private FontMetrics fontMetrics;
	private KeyboardDefinition kbd;

	
	public SimpleAlphaKeyboardRenderer(KeyboardDefinition kbd) {
		this.kbd = kbd;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		if(fontMetrics == null) {
			fontMetrics = g2d.getFontMetrics(keyboardFont);
		}

		System.out.println(kbd.getBounds());
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, (int)kbd.getBounds().getMaxX(), (int)kbd.getBounds().getMaxY());
		g2d.setColor(Color.black);
		
		for(KeyboardKey k : kbd.getKeysIterator()) {				
			Point p = getShapeCenter(k.getKeyShape());				
			g2d.draw(k.getKeyShape());				
			g2d.setFont(keyboardFont);
			int strWidth = fontMetrics.stringWidth(k.getKeyStringRepresentation());
			g2d.drawString(k.getKeyStringRepresentation(), p.x - strWidth/2, p.y + fontMetrics.getAscent()/2);
		}
		
	}

	private Point getShapeCenter(Shape keyShape) {
		Rectangle2D bounds = keyShape.getBounds2D();
		Point p = new Point((int)bounds.getCenterX(), (int)bounds.getCenterY());
		return p;
	}
}
