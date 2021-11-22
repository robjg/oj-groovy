package org.oddjob.groovy;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.oddjob.OddjobSrc;
import org.oddjob.OurDirs;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.io.FilesType;
import org.oddjob.launch.Launcher;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.util.URLClassLoaderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore // Todo Integration tests
public class GroovyOddballTest {

	private static final Logger logger =
			LoggerFactory.getLogger(GroovyOddballTest.class);
	
	OurDirs dirs;
	
	ClassLoader oddjobLoader;
	
	String oddball;
	
	@Before
	public void setUp() throws Exception {

		dirs = new OurDirs();
		
		oddball = dirs.base().getPath();

		Path oddjobApp = OddjobSrc.oddjobApp();

		logger.info("Oddjob Home is " + oddjobApp);
		
		FilesType lib = new FilesType();
		lib.setFiles(oddjobApp + "/lib/*.jar");
		
		FilesType opt = new FilesType();
		opt.setFiles(oddjobApp + "/opt/lib/*.jar");
				
		FilesType tools = new FilesType();
		tools.setFiles(oddjobApp + "/build/tools/*.jar");
		
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

	@Test
	public void testExpressionAsAnOddball() throws ArooaPropertyException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"GroovyExpressionSimple.xml")).getFile());
		
		Launcher launcher = new Launcher(oddjobLoader, Launcher.ODDJOB_MAIN_CLASS,
				new String[]
						{ "-op", oddball, "-f", file.getPath() });

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close ignored = console.captureConsole()) {
			launcher.launch();
		}

		console.dump(logger);
		
		String[] lines = console.getLines();

		assertThat(lines[0].trim(), is("4"));
		assertThat(lines.length, is(1));
	}
	
	@Test
	public void testJobAsAnOddball() throws ArooaPropertyException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"GroovyJobHello.xml")).getFile());
		
		Launcher launcher = new Launcher(oddjobLoader, Launcher.ODDJOB_MAIN_CLASS,
				new String[]
						{ "-op", oddball, "-f", file.getPath() });

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close ignored = console.captureConsole()) {

			launcher.launch();
		}

		console.dump(logger);
		
		String[] lines = console.getLines();

		assertThat(lines[0].trim(), is("Hello From Groovy"));
		assertThat(lines.length, is(1));
	}

	@Test
	public void testJobWithClassLoader() throws ArooaPropertyException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		
		File file = dirs.relative("test/classloader/GroovyWithClassLoader.xml");
		
		Launcher launcher = new Launcher(oddjobLoader, Launcher.ODDJOB_MAIN_CLASS,
				new String[] { "-op", oddball, "-f", file.getPath() });

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close ignored =  console.captureConsole()) {

			launcher.launch();
		}

		console.dump(logger);
		
		String[] lines = console.getLines();

		assertThat(lines[0].trim(), is("The apple is red"));
		assertThat(lines.length, is(1));
	}
}
