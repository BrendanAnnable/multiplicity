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

package multiplicity.input.luminja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.math.Vector2f;

import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.IMultiTouchInputSource;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.exceptions.MultiTouchInputException;
import multiplicity.input.utils.ClickDetector;

import de.evoluce.multitouch.adapter.java.BlobJ;
import de.evoluce.multitouch.adapter.java.JavaAdapter;



/**
 * Support for the Lumin multi-touch table.  This implementation currently
 * only supports cursor information - it does not support objects/fiducials.
 * @author dcs0ah1
 *
 */
public class ThreadedLuminMultiTouchInput implements IMultiTouchInputSource {
	private static final Logger log = Logger.getLogger(ThreadedLuminMultiTouchInput.class.getName());
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	protected ConcurrentLinkedQueue<Callable<Void>> executeQueue = new ConcurrentLinkedQueue<Callable<Void>>();
	protected JavaAdapter ja;	
	protected BlobJ[] currentBlobs = new BlobJ[0];

	protected Map<Integer, BlobJ> currentBlobRegistry = new HashMap<Integer,BlobJ>();
	protected Map<Integer, Vector2f> lastKnownPosition = new HashMap<Integer,Vector2f>();
	protected float samePositionTolerance = 0.002f;
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);


	public ThreadedLuminMultiTouchInput() {
		ja = new JavaAdapter("localhost");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					currentBlobs = ja.getBlobsOfNextFrame().mBlobs;
					process();
					Thread.yield();
				} catch (Exception e) {
					log.log(Level.SEVERE, "Error getting frames.", e);		
				}
				
			}			
		});
		t.start();
	}

	private void process() throws MultiTouchInputException {


		final Vector2f pos = new Vector2f();
		Vector2f vel = new Vector2f();

		Map<Integer, BlobJ> newRegistry = new HashMap<Integer,BlobJ>();
		// notify for new blobs or changes to existing blobs
		for(final BlobJ blob : currentBlobs) {
			newRegistry.put(blob.mID, blob);
			pos.x = blob.mX;
			pos.y = blob.mY;

			final MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, pos, vel, blob.mWidth, 0f);

			if(currentBlobRegistry.containsKey(blob.mID)) {
				for(final IMultiTouchEventListener listener : listeners) {		
					executeQueue.add(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							listener.cursorChanged(event);
							return null;
						}						
					});
					
				}
				lastKnownPosition.put(blob.mID, new Vector2f(blob.mX, blob.mY));
			}else{
				executeQueue.add(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						clickDetector.newCursorPressed(blob.mID, pos);
						return null;
					}						
				});
				
				for(final IMultiTouchEventListener listener : listeners) {	
					executeQueue.add(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							listener.cursorPressed(event);
							return null;
						}						
					});
					
				}				
			}
		}


		// notify if blobs have disappeared
		for(Integer i : currentBlobRegistry.keySet()) {
			if(!newRegistry.keySet().contains(i)) {
				final BlobJ blob = currentBlobRegistry.get(i);
				pos.x = blob.mX;
				pos.y = blob.mY;			
				final MultiTouchCursorEvent event = new MultiTouchCursorEvent(blob.mID, pos, vel, blob.mWidth, 0f);

				for(final IMultiTouchEventListener l : listeners) {
					executeQueue.add(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							int clickCount = clickDetector.cursorReleasedGetClickCount(blob.mID, pos);
							if(clickCount > 0) {
								event.setClickCount(clickCount);
								l.cursorClicked(event);
							}
							l.cursorReleased(event);
							return null;
						}						
					});
					
				}
				lastKnownPosition.remove(blob.mID);
			}
		}

		currentBlobRegistry = newRegistry;
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
		for(Callable<Void> c : executeQueue) {
			try {
				c.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
			executeQueue.remove(c);
		}	
	}

}
