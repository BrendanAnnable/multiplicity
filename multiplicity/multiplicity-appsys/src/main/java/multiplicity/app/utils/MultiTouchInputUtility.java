package multiplicity.app.utils;

import com.jme.input.MouseInput;
import com.jme.system.DisplaySystem;

import multiplicity.config.table.TableConfigPrefsItem;
import multiplicity.config.table.TableConfigPrefsItem.TableType;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.luminja.LuminMultiTouchInput;
import multiplicity.input.simulator.jme.JMEDirectSimulator;
import multiplicity.input.simulator.jmetuio.JMETUIOSimulator;
import multiplicity.input.tuio.TUIOMultiTouchInput;

public class MultiTouchInputUtility {
	

	public static IMultiTouchInputSource getInputSource() {
		TableConfigPrefsItem tablePrefs = new TableConfigPrefsItem();
		TableType tabletype = tablePrefs.getTableType();
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		IMultiTouchInputSource multiTouchInput = null;
		
		System.out.println(tabletype);
		
		switch(tabletype) {
		
		case JMEDIRECT: {
			try {
				multiTouchInput = new JMEDirectSimulator(display.getWidth(), display.getHeight());
				MouseInput.get().setCursorVisible(true);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case LUMIN: {
			try {
				multiTouchInput = new LuminMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case TUIO: {
			try {
				multiTouchInput = new TUIOMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			break;
		}
		
		case TUIOSIM: {
			try {
				multiTouchInput = new TUIOMultiTouchInput();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			JMETUIOSimulator simulator = new JMETUIOSimulator(display.getWidth(), display.getHeight());
			simulator.start();	
			break;
		}		
		}

		return multiTouchInput;
	}
}
