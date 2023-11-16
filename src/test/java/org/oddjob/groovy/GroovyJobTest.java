package org.oddjob.groovy;

import java.io.File;
import java.util.Objects;

import junit.framework.TestCase;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.Resettable;
import org.oddjob.arooa.types.ValueType;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyJobTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(GroovyJobTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------  " + getName() + "  --------------------");
	}
	
	public void testSimpleGroovyExample() {
		
		File file = new File(getClass().getResource(
				"GroovyJobHello.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture capture = new ConsoleCapture();
		try (ConsoleCapture.Close ignored = capture.captureConsole()) {

			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());
		
		String[] lines = capture.getLines();
		
		assertEquals("Hello From Groovy", lines[0].trim());
		
		assertEquals(1, lines.length);
		
		oddjob.destroy();
	}
	
	public void testGroovySettingInRegistry() {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"GroovyJobSetting.xml")).getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		assertEquals("Hello From Groovy", lookup.lookup("greeting"));
		
		Object test = lookup.lookup("groovy");

		((Resettable) test).hardReset();
		
		assertEquals(null, lookup.lookup("greeting"));
		
		oddjob.destroy();
	}
	
	public void testGroovyAndVariables() {
		
		File file = new File(getClass().getResource(
				"GroovyAndVariables.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture capture = new ConsoleCapture();
		try (ConsoleCapture.Close ignored = capture.captureConsole()) {

			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());

		String[] lines = capture.getLines();
		
		assertEquals("" + ValueType.class, lines[0].trim());
		assertEquals("Apple", lines[1].trim());
		assertEquals("" + String.class, lines[2].trim());
		
		oddjob.destroy();
	}
	
	
}
