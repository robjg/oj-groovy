package org.oddjob.groovy;

import java.io.File;

import junit.framework.TestCase;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.state.ParentState;

public class GroovyExpressionTest extends TestCase {

	public void testSimpleExression() {
		
		GroovyExpression test = new GroovyExpression();
		test.setArooaSession(new StandardArooaSession());
		test.setExpression("2+2");
		
		assertEquals(4, test.evaluate());
		
	}
	
	public void testSimpleExample() throws ArooaPropertyException, ArooaConversionException {
		
		File file = new File(getClass().getResource(
				"GroovyExpressionSimple.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		assertEquals(new Integer(4), lookup.lookup("vars.result", Integer.class));
		assertEquals("4", lookup.lookup("echo.text"));
		
		oddjob.destroy();
	}
}
