package org.oddjob.groovy;

import java.util.Set;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.registry.BeanDirectory;
import org.oddjob.arooa.runtime.PropertyLookup;
import org.oddjob.arooa.runtime.PropertySource;

public class OddjobSessionInterfaceImpl implements OddjobSessionInterface {

	private BeanDirectory beanDirectory;
	
	private PropertyLookup propertyLookup;

	public OddjobSessionInterfaceImpl(ArooaSession session) {
		beanDirectory = session.getBeanRegistry();
		propertyLookup = session.getPropertyManager();
	}
	
	@Override
	public Object lookup(String path) throws ArooaPropertyException {
		return beanDirectory.lookup(path);
	}
	
	@Override
	public <T> T lookup(String path, Class<T> required)
			throws ArooaPropertyException, ArooaConversionException {
		return beanDirectory.lookup(path, required);
	}
	
	@Override
	public String getIdFor(Object bean) {
		return beanDirectory.getIdFor(bean);
	}
	
	@Override
	public <T> Iterable<T> getAllByType(Class<T> type) {
		return beanDirectory.getAllByType(type);
	}
	
	@Override
	public String getProperty(String propertyName) {
		return propertyLookup.lookup(propertyName);
	}
	
	@Override
	public PropertySource sourceFor(String propertyName) {
		return propertyLookup.sourceFor(propertyName);
	}
	
	@Override
	public Set<String> propertyNames() {
		return propertyLookup.propertyNames();
	}
}
