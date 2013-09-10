package org.oddjob.groovy;

import java.util.Set;

import org.oddjob.arooa.registry.BeanDirectory;
import org.oddjob.arooa.runtime.PropertySource;

public interface OddjobSessionInterface 
extends BeanDirectory {

	public String getProperty(String name);
	
	public PropertySource sourceFor(String propertyName);
	
	public Set<String> propertyNames();
}
