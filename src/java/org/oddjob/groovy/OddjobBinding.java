package org.oddjob.groovy;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.registry.BeanRegistry;

public class OddjobBinding extends Binding {

	public static final String ODDJOB_SESSION_INTERFACE_BINDING = "osi";
	
	public static final String ODDJOB_LOGGER_BINDING = "logger";
	
	public static final String ODDJOB_RESULT_BINDING = "oddjobResult";
	
	protected BeanRegistry beanRegistry;

	public OddjobBinding(ArooaSession session) {
		this.beanRegistry = session.getBeanRegistry();
		super.setVariable(ODDJOB_SESSION_INTERFACE_BINDING, 
				new OddjobSessionInterfaceImpl(session));
	}
	
	@Override
	public Object getVariable(String name) {
		if (super.hasVariable(name)) {
			return super.getVariable(name);
		}

		Object result = beanRegistry.lookup(name);
		if (result == null) {
            throw new MissingPropertyException(name, this.getClass());
		}
		
		return result;
	}
	
	@Override
	public boolean hasVariable(String name) {
		if (super.hasVariable(name)) {
			return true;
		}
		return beanRegistry.lookup(name) != null;
	}
	
}
