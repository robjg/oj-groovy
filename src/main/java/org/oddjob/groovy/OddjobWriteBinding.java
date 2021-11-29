package org.oddjob.groovy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oddjob.arooa.ArooaSession;

/**
 * 
 * @author rob
 *
 */
public class OddjobWriteBinding extends OddjobBinding {

	public static final String ODDJOB_LOGGER_BINDING = "logger";
	
	public static final String ODDJOB_RESULT_BINDING = "oddjobResult";
	
	private Set<Object> registered = Collections.synchronizedSet(
			new HashSet<Object>());
	
	public OddjobWriteBinding(ArooaSession session, Logger logger) {
		super(session);
		super.setVariable(ODDJOB_LOGGER_BINDING, logger);
	}

	@Override
	public void setVariable(String name, Object value) {
		if (ODDJOB_RESULT_BINDING.equals(name)) {
			super.setVariable(name, value);
		}
		beanRegistry.register(name, value);
		
		registered.add(value);
	}
	
	public synchronized void reset() {

		for (Object o : registered) {
			beanRegistry.remove(o);
		}
		
		registered.clear();
	}
}
