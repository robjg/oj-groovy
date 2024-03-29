package org.oddjob.groovy;

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

/**
 * @oddjob.description A Groovy expression.
 *
 * @oddjob.example Evaluate a simple expression. A Groovy expression is held in a Variable and
 * evaluated during the running of the Echo job.
 *
 * {@oddjob.xml.resource org/oddjob/groovy/GroovyExpressionSimple.xml}
 *
 * @author rob
 *
 */
public class GroovyExpression implements ArooaValue, ArooaSessionAware {

	public static class Conversions implements ConversionProvider {
		
		public void registerWith(ConversionRegistry registry) {
			registry.registerJoker(GroovyExpression.class,
                    new Joker<>() {
                        public <T> ConversionStep<GroovyExpression, T> lastStep(
                                Class<? extends GroovyExpression> from,
                                final Class<T> to,
                                ConversionLookup conversions) {

                            return new ConversionStep<>() {

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

	/**
	 * @oddjob.property
	 * @oddjob.description The expression.
	 * @oddjob.required No, evaluate to null if missing.
	 */
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
		
		return new GroovyShell(binding).evaluate(expression);
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		return expression;
	}

}
