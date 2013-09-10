package org.oddjob.groovy;

import groovy.lang.GroovyShell;

import java.util.concurrent.Callable;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.deploy.annotations.ArooaText;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.life.Destroy;
import org.oddjob.framework.HardReset;
import org.oddjob.framework.SoftReset;

public class GroovyJob implements Callable<Integer>, ArooaSessionAware {

	private OddjobWriteBinding binding;
	
	private String name;
	
	private Object evalResult;
	
	private String script;
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		binding = new OddjobWriteBinding(session);
	}
	
	@Override
	public Integer call() throws Exception {

		if (script == null) {
			throw new NullPointerException("No script.");
		}
		
		GroovyShell shell = new GroovyShell(binding);

		evalResult = shell.evaluate(script);
		
		if (binding.hasVariable(OddjobBinding.ODDJOB_RESULT_BINDING)) {
			Object result = binding.getVariable(
					OddjobBinding.ODDJOB_RESULT_BINDING);
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
