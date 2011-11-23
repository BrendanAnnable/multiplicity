package multiplicity3.appsystem;

import java.util.logging.Logger;

import com.jme3.input.InputManager;

import multiplicity3.config.input.InputConfigPrefsItem;
import multiplicity3.config.input.InputConfigPrefsItem.InputType;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.luminja.LuminMultiTouchInput;
import multiplicity3.input.simulator.jme.JMEDirectSimulator;
import multiplicity3.input.tuio.TUIOMultiTouchInput;

public class MultiTouchInputUtility {
	
	private static final Logger log = Logger.getLogger(MultiTouchInputUtility.class.getName());

	public static IMultiTouchInputSource getInputSource(InputManager inputManager, int displayWidth, int displayHeight) {
		InputConfigPrefsItem tablePrefs = new InputConfigPrefsItem();
		InputType tabletype = tablePrefs.getInputType();
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
		
		case EVOLUCE: {
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
		
//		case TUIOSIM: {
//			try {
//				multiTouchInput = new TUIOMultiTouchInput();
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			}
//			//JMETUIOSimulator simulator = new JMETUIOSimulator(displayWidth, displayHeight);
//			//simulator.start();
//			throw new UnsupportedOperationException();
////			break;
//		}		
		}

		return multiTouchInput;
	}
}
