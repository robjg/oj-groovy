package org.oddjob.groovy;

import javax.inject.Inject;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.ArooaValue;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.convert.ArooaConverter;
import org.oddjob.arooa.convert.ConversionLookup;
import org.oddjob.arooa.convert.ConversionProvider;
import org.oddjob.arooa.convert.ConversionRegistry;
import org.oddjob.arooa.convert.ConversionStep;
import org.oddjob.arooa.convert.Joker;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;

public class GroovyExpression implements ArooaValue, ArooaSessionAware {

	public static class Conversions implements ConversionProvider {
		
		public void registerWith(ConversionRegistry registry) {
			registry.registerJoker(GroovyExpression.class,
					new Joker<GroovyExpression>() {
				public <T> ConversionStep<GroovyExpression, T> lastStep(
								Class<? extends GroovyExpression> from, 
								final Class<T> to, 
								ConversionLookup conversions) {
					
					return new ConversionStep<GroovyExpression, T>() {
						
						public Class<GroovyExpression> getFromClass() {
							return GroovyExpression.class;
						}
						
						public Class<T> getToClass() {
							return to;
						}
						
						public T convert(GroovyExpression from,
								ArooaConverter converter)
								throws ArooaConversionException {
							return converter.convert(from.evaluate(), to);
						}
					};
				}
			});
		}
	}
	
	private ArooaSession session;
	
	private ClassLoader classLoader;
	
	private String expression;
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.session = session;
	}
	
	public Object evaluate() {

		if (expression == null) {
			return null;
		}
		
		Binding binding = new OddjobBinding(session);
		
		return new GroovyShell(classLoader, binding).evaluate(expression);
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	@Inject
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	@Override
	public String toString() {
		return expression;
	}

}
