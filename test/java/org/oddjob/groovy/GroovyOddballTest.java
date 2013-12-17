package org.oddjob.groovy;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.oddjob.Oddjob;
import org.oddjob.OurDirs;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.io.FilesType;
import org.oddjob.launch.Launcher;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.util.URLClassLoaderType;

public class GroovyOddballTest extends TestCase {

	private static final Logger logger = 
			Logger.getLogger(GroovyOddballTest.class);
	
	OurDirs dirs;
	
	ClassLoader oddjobLoader;
	
	String oddball;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		dirs = new OurDirs();
		
		oddball = dirs.base().getPath();
		
		String oddjobSrc = System.getProperty("oddjob.src");
		File oddjobSrcDir;
		if (oddjobSrc == null) {
			oddjobSrcDir = dirs.relative("../oddjob");
		}
		else {
			oddjobSrcDir = new File(oddjobSrc);
		}
		
		logger.info("Oddjob Home is " + oddjobSrcDir); 
		
		FilesType lib = new FilesType();
		lib.setFiles(oddjobSrcDir + "/lib/*.jar");
		
		FilesType opt = new FilesType();
		opt.setFiles(oddjobSrcDir + "/opt/lib/*.jar");
				
		FilesType tools = new FilesType();
		tools.setFiles(oddjobSrcDir + "/build/tools/*.jar");
		
		FilesType files = new FilesType();
		files.setList(0,  new File[] { dirs.relative("test/logging") } );
		files.setList(1, lib.toFiles());
		files.setList(2, opt.toFiles());
		files.setList(3, tools.toFiles());
		
		URLClassLoaderType classLoaderType = new URLClassLoaderType();
		classLoaderType.setFiles(files.toFiles());
		classLoaderType.setNoInherit(true);
		classLoaderType.configured();
		
		oddjobLoader = classLoaderType.toValue();
		
	}
	
	public void testExpressionAsAnOddball() throws ArooaPropertyException, ArooaConversionException {
		
		File file = new File(getClass().getResource(
				"GroovyExpressionSimple.xml").getFile());
		
		Launcher launcher = new Launcher();
		launcher.setArgs(new String[] 
				{ "-op", oddball, "-f", file.getPath() });
		launcher.setClassLoader(oddjobLoader);
		launcher.setClassName(Launcher.ODDJOB_MAIN_CLASS);

		ConsoleCapture console = new ConsoleCapture();
		console.capture(Oddjob.CONSOLE);
		
		launcher.run();
		
		console.close();
		
		console.dump(logger);
		
		String[] lines = console.getLines();

		assertEquals("4", lines[0].trim());
		assertEquals(1, lines.length);
	}
	
	public void testJobAsAnOddball() throws ArooaPropertyException, ArooaConversionException {
		
		File file = new File(getClass().getResource(
				"GroovyJobHello.xml").getFile());
		
		Launcher launcher = new Launcher();
		launcher.setArgs(new String[] 
				{ "-op", oddball, "-f", file.getPath() });
		launcher.setClassLoader(oddjobLoader);
		launcher.setClassName(Launcher.ODDJOB_MAIN_CLASS);

		ConsoleCapture console = new ConsoleCapture();
		console.capture(Oddjob.CONSOLE);
		
		launcher.run();
		
		console.close();
		
		console.dump(logger);
		
		String[] lines = console.getLines();

		assertEquals("Hello From Groovy", lines[0].trim());
		assertEquals(1, lines.length);
	}
	
	public void testJobWithClassLoader() throws ArooaPropertyException, ArooaConversionException {
		
		File file = dirs.relative("test/classloader/GroovyWithClassLoader.xml");
		
		Launcher launcher = new Launcher();
		launcher.setArgs(new String[] 
				{ "-op", oddball, "-f", file.getPath() });
		launcher.setClassLoader(oddjobLoader);
		launcher.setClassName(Launcher.ODDJOB_MAIN_CLASS);

		ConsoleCapture console = new ConsoleCapture();
		console.capture(Oddjob.CONSOLE);
		
		launcher.run();
		
		console.close();
		
		console.dump(logger);
		
		String[] lines = console.getLines();

		assertEquals("The apple is red", lines[0].trim());
		assertEquals(1, lines.length);
	}
}
