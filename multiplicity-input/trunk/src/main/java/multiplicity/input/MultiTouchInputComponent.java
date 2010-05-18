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

package multiplicity.input;

import java.util.ArrayList;
import java.util.List;

import multiplicity.input.events.MultiTouchCursorEvent;
import multiplicity.input.events.MultiTouchObjectEvent;
import multiplicity.input.filters.IMultiTouchInputFilter;

public class MultiTouchInputComponent implements IMultiTouchEventListener, IMultiTouchEventProducer {
	
	private List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	private List<IMultiTouchInputFilter> filters = new ArrayList<IMultiTouchInputFilter>();
	private IMultiTouchInputSource source;
	private boolean isMultiTouchInputEnabled = true;
	
	public MultiTouchInputComponent(IMultiTouchInputSource source) {
		this.source = source;
		source.registerMultiTouchEventListener(this);
	}
	
	public boolean isMultiTouchInputEnabled() {
		return isMultiTouchInputEnabled;
	}

	public void setMultiTouchInputEnabled(boolean isMultiTouchInputEnabled) {
		this.isMultiTouchInputEnabled = isMultiTouchInputEnabled;
	}

	public void setSource(IMultiTouchInputSource source) {
		this.source = source;
		source.registerMultiTouchEventListener(filters.get(0));		
	}
	
	public void addMultiTouchInputFilter(IMultiTouchInputFilter filter) {
		if(filters.size() > 0) {
			IMultiTouchInputFilter t = getLastFilter();			
			t.setNext(filter);
			filters.add(filter);
			filter.setNext(this);
		}else{
			source.unregisterMultiTouchEventListener(this);
			source.registerMultiTouchEventListener(filter);
			filter.setNext(this);
			filters.add(filter);
		}
	}
	
	private IMultiTouchInputFilter getLastFilter() {
		return filters.get(filters.size()-1);
	}
	
	public boolean isFilterActive(Class<? extends IMultiTouchInputFilter> filter) {
		for(IMultiTouchInputFilter f : filters) {
			if(f.getClass().equals(filter)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Class<? extends IMultiTouchInputFilter>> getActiveFilterClasses() {
		List<Class<? extends IMultiTouchInputFilter>> classes = new ArrayList<Class<? extends IMultiTouchInputFilter>>();
		for(IMultiTouchInputFilter f : filters) {
			classes.add(f.getClass());
		}
		return classes;
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

	// **********************************

	public void cursorChanged(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.cursorChanged(event);
		}		
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.cursorClicked(event);
		}
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.cursorPressed(event);
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.cursorReleased(event);
		}
	}

	public void objectAdded(MultiTouchObjectEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.objectAdded(event);
		}		
	}

	public void objectChanged(MultiTouchObjectEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.objectChanged(event);
		}	
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
		if (!this.isMultiTouchInputEnabled) return;
		for(IMultiTouchEventListener l : listeners) {
			l.objectRemoved(event);
		}
	}
}
