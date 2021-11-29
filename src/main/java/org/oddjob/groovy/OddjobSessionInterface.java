package org.oddjob.groovy;

import java.util.Set;

import org.oddjob.arooa.registry.BeanDirectory;
import org.oddjob.arooa.runtime.PropertySource;

public interface OddjobSessionInterface 
extends BeanDirectory {

	String getProperty(String name);
	
	PropertySource sourceFor(String propertyName);
	
	Set<String> propertyNames();
}
