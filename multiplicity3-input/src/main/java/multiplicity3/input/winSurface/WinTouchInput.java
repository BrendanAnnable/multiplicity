package multiplicity3.input.winSurface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.exceptions.MultiTouchInputException;
import multiplicity3.input.utils.ClickDetector;

/**
 * Support for the Windows 7 input API.  This implementation currently
 * only supports cursor information - it does not support objects/fiducials.
 * @author dcs0ah1
 *
 */
public class WinTouchInput implements IMultiTouchInputSource {
	private static final Logger log = Logger.getLogger(WinTouchInput.class.getName());

	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();

	protected Map<Long,WinFingerCursor> fingerCursors = new HashMap<Long,WinFingerCursor>();
	protected Map<Integer, Vector2f> lastKnownPosition = new HashMap<Integer,Vector2f>();
	protected float samePositionTolerance = 0.002f;
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	private HashMap<Integer, Long> touchToCursorID;
	private boolean initialized;
	private boolean success = false;
	private static boolean loaded = false;
	private static final String dllName32 = "Win7Touch";
	private static final String dllName64 = "Win7Touch64";
	private Native_WM_TOUCH_Event wmTouchEvent;

	private int currentID;
	
	//FIXME Problem with attempts to call native functions.
	
	//Native Functions
	private native int findWindow(String tmpTitle, String subWindowTitle);
	private native boolean init(long HWND); 
	private native boolean getSystemMetrics(); 
	private native boolean quit(); 	
	private native boolean pollEvent(Native_WM_TOUCH_Event myEvent);

	public WinTouchInput() {
		success = false;		
		String platform = System.getProperty("os.name").toLowerCase();

		if (!platform.contains("windows 7") && !platform.contains("windows 8")) {
			log.severe("Win7NativeTouchSource input source can only be used on platforms running windows 7!");
			return;
		}

		if (!loaded){
			loaded = true;
			String dllName = (!System.getProperty("os.arch").equals("x86"))? dllName64 : dllName32;
			System.loadLibrary(dllName);
		}else{
			log.severe("Win7NativeTouchSource may only be instantiated once.");
			return;
		}
		
		boolean touchAvailable = this.getSystemMetrics();

		if (!touchAvailable){
			log.severe("Windows 7 Touch Input currently not available!");
			return;
		}else{
			log.info("Windows 7 Touch Input available.");
		}

		wmTouchEvent = new Native_WM_TOUCH_Event();
		wmTouchEvent.id = -1;
		wmTouchEvent.type = -1;
		wmTouchEvent.x = -1;
		wmTouchEvent.y = -1;		

		touchToCursorID = new HashMap<Integer, Long>();

		success = true;
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (isSuccessfullySetup()){
					log.severe("Cleaning up Win7 touch source..");
					quit();
				}
			}

		}));
		
		initialized = true;
	}
	
	public boolean isSuccessfullySetup() {
		return success;
	}

	private void process() throws MultiTouchInputException {
		
		if (initialized){
			if (pollEvent(wmTouchEvent)) {				
				
				switch (wmTouchEvent.type) {
					case Native_WM_TOUCH_Event.TOUCH_DOWN:{				
						WinFingerCursor fingerCursor = fingerCursors.get(wmTouchEvent.id);
						if(fingerCursor == null) {
							fingerCursor = new WinFingerCursor(wmTouchEvent.id);
							fingerCursor.setNew(true);
							fingerCursors.put((long)wmTouchEvent.id, fingerCursor);
						}	
					}case Native_WM_TOUCH_Event.TOUCH_MOVE:{
						WinFingerCursor fingerCursor = fingerCursors.get(wmTouchEvent.id);
						if(fingerCursor != null) {			
							fingerCursor.setPosition(new Vector2f(wmTouchEvent.x, wmTouchEvent.y));
							fingerCursor.setVelocity(new Vector2f());
							if(fingerCursor.isNew()) {
								for(IMultiTouchEventListener listener : listeners) {
									clickDetector.newCursorPressed(fingerCursor.getId(), fingerCursor.getPosition());
									MultiTouchCursorEvent evt = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
									listener.cursorPressed(evt);
								}
								fingerCursor.setNew(false);
							}else{
								for(IMultiTouchEventListener listener : listeners) {
									MultiTouchCursorEvent event = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
									listener.cursorChanged(event);
								}
							}
						}
						break;	
					}case Native_WM_TOUCH_Event.TOUCH_UP:{
						WinFingerCursor fingerCursor = fingerCursors.get(wmTouchEvent.id);
		
						if(fingerCursor != null) {
		
							for(IMultiTouchEventListener l : listeners) {
								MultiTouchCursorEvent event = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
								int clickCount = clickDetector.cursorReleasedGetClickCount(fingerCursor.getId(), fingerCursor.getPosition());
								if(clickCount > 0) {
									event.setClickCount(clickCount);
									l.cursorClicked(event);
								}
								l.cursorReleased(event);
							}
							fingerCursors.remove((long)wmTouchEvent.id);
		
						}
						break;
			
					}default:{
						break;
					}
				}
			}
		}
	}

	public void setClickSensitivity(long time, float distance) {
		clickDetector.setSensitivity(time, distance);
	}

	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);	
	}

	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}

	public void setSamePositionTolerance(float samePositionTolerance) {
		this.samePositionTolerance = samePositionTolerance;
	}

	@Override
	public void update(float tpf) throws MultiTouchInputException {
		process();
	}

	@Override
	public boolean requiresMouseDisplay() {
		return false;
	}
	
	private class Native_WM_TOUCH_Event{
	    public static final int TOUCH_DOWN = 0;
	    public static final int TOUCH_MOVE = 1;
	    public static final int TOUCH_UP = 2;

	    /** The type. */
	    public int type;

	    /** The id. */
	    public int id;

	    /** The x value. */
	    public int x;

	    /** The y value. */
	    public int y;

//	    /** The contact size area X dimension */
//	    public int contactSizeX;
//
//	    /** The contact size area Y dimension */
//	    public int contactSizeY;

	}

}
