/*
 * Copyright (c) 2009 University of Durham, England
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

package multiplicity.input.filters.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import multiplicity.input.IMultiTouchEventListener;
import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;
import multiplicity.input.filters.IMultiTouchInputFilter;

/**
 * Logs all actions to a log file.
 * 
 * @author dcs0ah1
 *
 */
public class LoggingFilter implements IMultiTouchInputFilter {

	public static final String CURSOR_PRESSED = "CURSOR_PRESSED";
	public static final String CURSOR_CLICKED = "CURSOR_CLICKED";
	public static final String CURSOR_RELEASED = "CURSOR_RELEASED";
	public static final String CURSOR_CHANGED = "CURSOR_CHANGED";
	public static final String OBJECT_ADDED = "OBJECT_ADDED";
	public static final String OBJECT_CHANGED = "OBJECT_CHANGED";
	public static final String OBJECT_REMOVED = "OBJECT_REMOVED";
	
	private IMultiTouchEventListener next;
	private File recordFile;
	private PrintWriter pw;
	
	public LoggingFilter(Class<?> appClass) throws FileNotFoundException {
		recordFile = new File(new File("logs"), getFileNameFromDate());
		pw = new PrintWriter(new FileOutputStream(recordFile));
		writeFileHeader(appClass);
	}
	
	private String getFileNameFromDate() {		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.S");
		return formatter.format(new Date());	
	}	
	
	private void writeFileHeader(Class<?> appClass) {
		pw.println("# Recorded from LoggingFilter v0.1");
		pw.println("# App: " + appClass.getName());
		pw.println("# Recording started at " + new Date().toString());
		pw.println("# Format is as follows:");
		pw.println("# System.nanoTime(), event type, cursor ID, x, y, velocity x, velocity y, pressure");
	}

	public void setNext(IMultiTouchEventListener el) {
		this.next = el;		
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(CURSOR_CHANGED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());
		pw.flush();
		next.cursorChanged(event);
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(CURSOR_CLICKED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());
		pw.flush();
		next.cursorClicked(event);
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(CURSOR_PRESSED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());
		pw.flush();
		next.cursorPressed(event);		
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(CURSOR_RELEASED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());		
		pw.flush();
		next.cursorReleased(event);
	}

	public void objectAdded(MultiTouchObjectEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(OBJECT_ADDED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());		
		pw.flush();
		next.objectAdded(event);
	}

	public void objectChanged(MultiTouchObjectEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(OBJECT_CHANGED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());		
		pw.flush();
		next.objectChanged(event);		
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
		pw.print(System.nanoTime() + ",");
		pw.print(OBJECT_REMOVED + ",");
		pw.print(event.getCursorID() + ",");
		pw.print(event.getPosition().x + ",");
		pw.print(event.getPosition().y + ",");
		pw.print(event.getVelocity().x + ",");
		pw.print(event.getVelocity().y + ",");
		pw.println(event.getPressure());		
		pw.flush();
		next.objectRemoved(event);		
	}


}
