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

package multiplicity.input.events;

import com.jme.math.Vector2f;

/**
 * A multi touch cursor event whose coordinate system is based
 * on the input service that created it, and its mode.  Supports
 * position, rotation, velocity and pressure, although this values
 * will only be present for tables that provide that information.
 * 
 * @author dcs0ah1
 *
 */
public class MultiTouchCursorEvent extends MultiTouchEvent {
	private static final long serialVersionUID = 3249909209093608288L;
	
	private int clickCount = 0;
	
	public MultiTouchCursorEvent(long id, Vector2f position) {
		super(id, position);
	}
	
	public MultiTouchCursorEvent(long id, Vector2f position, Vector2f velocity) {
		super(id, position, velocity);
	}
	
	public MultiTouchCursorEvent(long id, Vector2f position, Vector2f velocity, float pressure, double angle) {
		super(id, position, velocity, pressure, angle);
	}
	
	public String toString() {
		return this.getClass().getName() + " id:" + this.getCursorID() + " @" + this.getPosition();
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getClickCount() {
		return clickCount;
	}
}
