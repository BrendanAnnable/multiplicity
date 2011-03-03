package multiplicity2.app;

import java.util.logging.Logger;

import com.jme3.input.InputManager;

import multiplicity.config.table.TableConfigPrefsItem;
import multiplicity.config.table.TableConfigPrefsItem.TableType;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.luminja.LuminMultiTouchInput;
import multiplicity.input.simulator.jme.JMEDirectSimulator;
import multiplicity.input.tuio.TUIOMultiTouchInput;

public class MultiTouchInputUtility {
	
	private static final Logger log = Logger.getLogger(MultiTouchInputUtility.class.getName());

	public static IMultiTouchInputSource getInputSource(InputManager inputManager, int displayWidth, int displayHeight) {
		TableConfigPrefsItem tablePrefs = new TableConfigPrefsItem();
		TableType tabletype = tablePrefs.getTableType();
		IMultiTouchInputSource multiTouchInput = null;
		
		log.info("Table type: " + tabletype.toString());
		
		switch(tabletype) {
		
		case JMEDIRECT: {
			try {
				multiTouchInput = new JMEDirectSimulator(inputManager, displayWidth, displayHeight);
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
			//TODO: update - but nobody uses this, right?
			//JMETUIOSimulator simulator = new JMETUIOSimulator(displayWidth, displayHeight);
			//simulator.start();
			throw new UnsupportedOperationException();
//			break;
		}		
		}

		return multiTouchInput;
	}
}
