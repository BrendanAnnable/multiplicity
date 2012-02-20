/*
 * Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplicity3.input.tuio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

import com.jme3.math.Vector2f;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import multiplicity3.input.tuio.tuioobjects.TUIOFiducialObject;
import multiplicity3.input.tuio.tuioobjects.TUIOFingerCursor;
import multiplicity3.input.utils.ClickDetector;

/**
 * Support for tables which use the TUIO protocol. This implementation supports
 * both cursors and objects/fiducials.
 * 
 * @author dcs0ah1
 */
public class TUIOMultiTouchInput implements IMultiTouchInputSource, TuioListener {

	protected TuioClient networkClient;

	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	protected Map<Long,TUIOFingerCursor> fingerCursors = new HashMap<Long,TUIOFingerCursor>();
	protected Map<Long,TUIOFiducialObject> fiducials = new HashMap<Long,TUIOFiducialObject>();

	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();

	public TUIOMultiTouchInput() {
		start();
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


	public void setClickSensitivity(long time, float distance) {
		this.clickDetector = new ClickDetector(time, distance);
	}

	public void start() {
		synchronized(this) {
			TUIOPrefsItem tablePrefs = new TUIOPrefsItem();
			networkClient = new TuioClient(tablePrefs.getTuioPort());
			networkClient.addTuioListener(this);
			networkClient.connect();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					networkClient.disconnect();
				}
			});
		}
	}

	public void stop() {
		networkClient.removeTuioListener(this);
		networkClient.disconnect();
		networkClient = null;
	}

	// TuioListener methods

	/**
	 * Unfortunately, we don't have positional information at this point
	 * so we need to flag the object as being new, and rely on it being
	 * subsequently updated in order for that to decide whether to inform
	 * about being new or just updated.
	 */
	public void addTuioCursor(final TuioCursor tuioCursor) {		
		final long sessionID = tuioCursor.getSessionID();
		TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
		if(fingerCursor == null) {
			fingerCursor = new TUIOFingerCursor();
			fingerCursors.put(sessionID, fingerCursor);
			
			Callable<Object> c = new Callable<Object>() {
				
				float xpos = tuioCursor.getX();
				float ypos = tuioCursor.getY();
				float x_speed = tuioCursor.getXSpeed();
				float y_speed = tuioCursor.getYSpeed();		
				
				@Override
				public Object call() throws Exception {
					final TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
					if(fingerCursor != null) {			
						fingerCursor.setPosition(new Vector2f(xpos, 1-ypos));
						fingerCursor.setVelocity(new Vector2f(x_speed, y_speed));
						for(IMultiTouchEventListener listener : listeners) {
							clickDetector.newCursorPressed(fingerCursor.getId(), fingerCursor.getPosition());
							MultiTouchCursorEvent evt = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition(), fingerCursor.getVelocity());
							listener.cursorPressed(evt);
						}
					}
					return null;
				}
			};
			synchronized(callingList) {
				callingList.add(c);
			}
			
			
		}
	}

	public void removeTuioCursor(final TuioCursor tuioCursor) {
		Callable<Object> c = new Callable<Object>() {
			
			long sessionID = tuioCursor.getSessionID();

			@Override
			public Object call() throws Exception {
				final TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);

				if(fingerCursor != null) {

					for(IMultiTouchEventListener l : listeners) {
						MultiTouchCursorEvent event = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition(), fingerCursor.getVelocity());
						int clickCount = clickDetector.cursorReleasedGetClickCount(fingerCursor.getId(), fingerCursor.getPosition());
						if(clickCount > 0) {
							event.setClickCount(clickCount);
							l.cursorClicked(event);
						}
						l.cursorReleased(event);
					}
					fingerCursors.remove(sessionID);

				}
				return null;
			}
		};
		synchronized(callingList) {
			callingList.add(c);
		}

	}

	public void updateTuioCursor(final TuioCursor tuioCursor){
		Callable<Object> c = new Callable<Object>() {
			
			float xpos = tuioCursor.getX();
			float ypos = tuioCursor.getY();
			float x_speed = tuioCursor.getXSpeed();
			float y_speed = tuioCursor.getYSpeed();		
			long sessionID = tuioCursor.getSessionID();
			
			@Override
			public Object call() throws Exception {
				final TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
				if(fingerCursor != null) {			
					fingerCursor.setPosition(new Vector2f(xpos, 1-ypos));
					fingerCursor.setVelocity(new Vector2f(x_speed, y_speed));
					for(IMultiTouchEventListener listener : listeners) {
						MultiTouchCursorEvent event = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition(), fingerCursor.getVelocity());
						listener.cursorChanged(event);
					}
				}
				return null;
			}
		};
		synchronized(callingList) {
			callingList.add(c);
		}

	}


	public void addTuioObject(TuioObject tuioObject){
		long sessionID = tuioObject.getSessionID();
		int fiducialID = tuioObject.getSymbolID();
		TUIOFiducialObject fiducial = fiducials.get(sessionID);
		if(fiducial == null) {
			fiducial = new TUIOFiducialObject(sessionID, fiducialID);
			fiducials.put(sessionID, fiducial);			
		}
	}

	public void removeTuioObject(final TuioObject tuioObject) {
		Callable<Object> c = new Callable<Object>() {
			
			long sessionID = tuioObject.getSessionID();

			@Override
			public Object call() throws Exception {
				TUIOFiducialObject fiducial = fiducials.get(sessionID);
				if(fiducial != null) { 
					for(IMultiTouchEventListener listener : listeners) {
						MultiTouchObjectEvent event = new MultiTouchObjectEvent(fiducial.getId(), fiducial.getPosition(), fiducial.getVelocity());
						listener.objectRemoved(event);
					}
					fiducials.remove(sessionID);
				}
				return null;
			}

		};
		synchronized(callingList) {
			callingList.add(c);
		}
	}


	public void updateTuioObject(final TuioObject tuioObject){
		Callable<Object> c = new Callable<Object>() {
			
			long sessionID = tuioObject.getSessionID();
			float angle = tuioObject.getAngle();
			float x_speed = tuioObject.getXSpeed();
			float y_speed = tuioObject.getYSpeed();
			float r_speed = tuioObject.getRotationSpeed();
			float r_accel = tuioObject.getRotationAccel();
			float xpos = tuioObject.getX();
			float ypos = tuioObject.getY();

			@Override
			public Object call() throws Exception {
				TUIOFiducialObject fiducial = fiducials.get(sessionID);
				if(fiducial != null) {
					fiducial.setPosition(new Vector2f(xpos, 1-ypos));
					fiducial.setVelocity(new Vector2f(x_speed, y_speed));
					fiducial.setAngle(angle);
					fiducial.setAngleVelocity(r_speed);
					fiducial.setAngleAcceleration(r_accel);
					for(IMultiTouchEventListener listener : listeners) {
						MultiTouchObjectEvent event = new MultiTouchObjectEvent(fiducial.getId(), fiducial.getPosition(), fiducial.getVelocity());
						listener.objectChanged(event);
					}
				}
				return null;
			}

		};
		synchronized(callingList) {
			callingList.add(c);
		}

	}

	public void refresh(TuioTime tuioTime) {
		// unused
	}

	public void update(float tpf) {
		synchronized(callingList) {
			for(Callable<Object> c : callingList) {
				try {
					c.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callingList.clear();
		}
	}

	@Override
	public boolean requiresMouseDisplay() {
		return false;
	}
	
}