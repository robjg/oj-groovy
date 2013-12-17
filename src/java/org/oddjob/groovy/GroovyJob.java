package org.oddjob.groovy;

import groovy.lang.GroovyShell;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.deploy.annotations.ArooaText;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.life.Destroy;
import org.oddjob.framework.HardReset;
import org.oddjob.framework.SoftReset;

/**
 * @oddjob.description Execute a Groovy Script.
 * 
 * In addition to the <code>osi</code> available to {@link GroovyExpression},
 * The logger is made available to the script with the variable <code>logger</code>
 * and an integer result for this job, which will affect it's completion
 * state, can be set by setting the variable <code>oddjobResult</code>.
 * 
 * @author rob
 *
 */
public class GroovyJob implements Callable<Integer>, ArooaSessionAware {

	private static final Logger logger = Logger.getLogger(GroovyJob.class);
	
	private OddjobWriteBinding binding;
	
	/**
	 * @oddjob.proeprty
	 * @oddjob.description The class loader for the script to use.
	 * @oddjob.required No.
	 */
	private ClassLoader classLoader;
	
	private String name;
	
	private Object evalResult;
	
	private String script;
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		binding = new OddjobWriteBinding(session, logger);
	}
	
	@Override
	public Integer call() throws Exception {

		if (script == null) {
			throw new NullPointerException("No script.");
		}
		
		GroovyShell shell = new GroovyShell(classLoader, binding);

		evalResult = shell.evaluate(script);
		
		if (binding.hasVariable(OddjobWriteBinding.ODDJOB_RESULT_BINDING)) {
			Object result = binding.getVariable(
					OddjobWriteBinding.ODDJOB_RESULT_BINDING);
			if (result instanceof Number) {
				return ((Number) result).intValue();
			}
		}
		
		return 0;
	}
	
	@HardReset
	@SoftReset
	@Destroy
	public void reset() {
		binding.reset();
	}
	
	@ArooaText
	public void setScript(String script) {
		this.script = script;
	}
	
	public String getScript() {
		return script;
	}
	
	public Object getEvalResult() {
		return evalResult;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	@Override
	public String toString() {
		if (name == null) {
			return getClass().getSimpleName();
		}
		else {
			return name;
		}
	}

}
