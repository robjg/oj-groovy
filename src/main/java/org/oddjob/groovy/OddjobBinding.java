package org.oddjob.groovy;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.registry.BeanRegistry;

/**
 * A binding that allows Groovy access to objects registered in Oddjob. 
 * This binding also exposes an {@link OddjobSessionInterface} object
 * to Groovy via the special property <code>osi</code>.
 * 
 * @author rob
 *
 */
public class OddjobBinding extends Binding {

	public static final String ODDJOB_SESSION_INTERFACE_BINDING = "osi";
	
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
