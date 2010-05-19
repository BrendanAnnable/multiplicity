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

package multiplicity.servicesys;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import multiplicity.servicesys.exceptions.CouldNotStartServiceException;

public class ServiceManager {
	private static final Logger log = Logger.getLogger(ServiceManager.class.getName());
	private static ServiceManager instance;

	protected Map<String, AbstractMultiplicityService> services = new HashMap<String,AbstractMultiplicityService>();

	public static ServiceManager getInstance() {
		synchronized(ServiceManager.class) {
			if(instance == null) {
				instance = new ServiceManager();
			}
			return instance;
		}
	}

	private ServiceManager() {
		log.info("ServiceManager created. Adding shutdown hook to runtime.");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});
	}
	
	public AbstractMultiplicityService get(Class<? extends AbstractMultiplicityService> theClass) throws CouldNotStartServiceException {
		String classname = theClass.getName();
		if(services.get(classname) != null) {
			return services.get(classname);
		}
		log.info("Attempting to register " + classname);
		try {
			log.info("Creating an instance of " + classname);
			AbstractMultiplicityService s = (AbstractMultiplicityService)Class.forName(classname).newInstance();			
			services.put(classname, s);
			s.start();
			return s;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void shutdown() {
		for(String key : services.keySet()) {			
			AbstractMultiplicityService s = services.get(key);
			log.info("Shutting down " + key);
			s.shutdown();
		}
		services.clear();
	}
	
	public void unregister(String classname){
		if(services.containsKey(classname)) services.remove(classname);
	}
	
	public void update() {
		for(AbstractMultiplicityService s : services.values()) {
			s.update();
		}
	}
}
