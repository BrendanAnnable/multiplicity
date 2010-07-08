package multiplicity.csysng.items.keyboard.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import multiplicity.csysng.items.keyboard.KeyboardDefinition;
import multiplicity.csysng.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;

public class TestKeyboard {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame jf = new JFrame();
				KeyboardDefinition kd = new SimpleAlphaKeyboardDefinition();
				KeyboardViewPanel kvp = new KeyboardViewPanel(kd);				
				jf.add(kvp);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setSize(840,480);
				jf.setVisible(true);
			}			
		});
		
	}
}
