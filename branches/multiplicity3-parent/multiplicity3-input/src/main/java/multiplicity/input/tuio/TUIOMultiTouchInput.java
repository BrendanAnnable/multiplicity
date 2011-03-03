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

package multiplicity.input.tuio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.jme3.math.Vector2f;

import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;
import multiplicity.input.tuio.tuioobjects.TUIOFiducialObject;
import multiplicity.input.tuio.tuioobjects.TUIOFingerCursor;
import multiplicity.input.utils.ClickDetector;

import edu.upf.mtg.reactivision.TuioClient;
import edu.upf.mtg.reactivision.TuioListener;

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
			networkClient = new TuioClient();
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
	public void addTuioCur(long sessionID) {		
		TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
		if(fingerCursor == null) {
			fingerCursor = new TUIOFingerCursor(sessionID);
			fingerCursor.setNew(true);
			fingerCursors.put(sessionID, fingerCursor);
		}
	}

	public void removeTuioCur(final long sessionID) {
		Callable<Object> c = new Callable<Object>() {

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

	public void updateTuioCur(final long sessionID, final float xpos, final float ypos, final float x_speed, final float y_speed, float m_accel) {

		Callable<Object> c = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				final TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
				if(fingerCursor != null) {			
					fingerCursor.setPosition(new Vector2f(xpos, ypos));
					fingerCursor.setVelocity(new Vector2f(x_speed, y_speed));

					if(fingerCursor.isNew()) {
						for(IMultiTouchEventListener listener : listeners) {
							clickDetector.newCursorPressed(fingerCursor.getId(), fingerCursor.getPosition());
							MultiTouchCursorEvent evt = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition(), fingerCursor.getVelocity());
							listener.cursorPressed(evt);
						}
						fingerCursor.setNew(false);
					}else{
						for(IMultiTouchEventListener listener : listeners) {
							MultiTouchCursorEvent event = new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition(), fingerCursor.getVelocity());
							listener.cursorChanged(event);
						}
					}
				}
				return null;
			}
		};
		synchronized(callingList) {
			callingList.add(c);
		}

	}


	public void addTuioObj(long sessionID, int fiducialID) {
		TUIOFiducialObject fiducial = fiducials.get(sessionID);
		if(fiducial == null) {
			fiducial = new TUIOFiducialObject(sessionID, fiducialID);
			fiducial.setNew(true);
			fiducials.put(sessionID, fiducial);			
		}
	}

	public void removeTuioObj(final long sessionID, int fiducialID) {
		Callable<Object> c = new Callable<Object>() {

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


	public void updateTuioObj(final long sessionID, int fiducial_id, final float xpos, final float ypos, final float angle, final float x_speed, final float y_speed, final float r_speed, float m_accel, final float r_accel) {
		Callable<Object> c = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				TUIOFiducialObject fiducial = fiducials.get(sessionID);
				if(fiducial != null) {
					fiducial.setPosition(new Vector2f(xpos, ypos));
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

	public void refresh() {
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

}
