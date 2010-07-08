package multiplicity.csysng.items.keyboard.test;

import java.awt.Graphics;

import javax.swing.JPanel;

import multiplicity.csysng.items.keyboard.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;

public class KeyboardViewPanel extends JPanel {
	private static final long serialVersionUID = 2179702392158132563L;
	private KeyboardDefinition kbd;
	private SimpleAlphaKeyboardRenderer kir;

	public KeyboardViewPanel(KeyboardDefinition kd) {
		this.kbd = kd;
		kir = new SimpleAlphaKeyboardRenderer(kbd);
	}

	public void paintComponent(Graphics g) {
		clear(g);
		kir.draw(g);
	}
	
	protected void clear(Graphics g) {
		super.paintComponent(g);
	}
}
