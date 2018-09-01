/*
 * Copyright (C) 2018 L2J Server
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.l2jserver.script.compiler;

import static com.l2jserver.script.engine.JavaScriptEngine.CLASSLOADER;
import static com.l2jserver.script.engine.JavaScriptEngine.CLASSPATH;
import static com.l2jserver.script.engine.JavaScriptEngine.MAINCLASS;
import static javax.script.ScriptContext.ENGINE_SCOPE;
import static javax.script.ScriptEngine.FILENAME;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Java Compiler test.
 *
 * @author Zoey76
 */
public class JavaCompilerTest {

    private static final String CLASS_PATH = "src/test/resources/";

    private static final String CLASS = "TestClass";

    private static final String EXTENSION = "java";

    private static final String PACKAGE_PATH = "com/l2jserver/script/java/";

    private static final String PACKAGE = "com.l2jserver.script.java";

    private static final String METHOD_NAME = "main";

    @Test
    void testJavaCompiler() throws Exception {
	final ScriptEngineManager sem = new ScriptEngineManager();
	final ScriptEngine scriptEngine = sem.getEngineByExtension(EXTENSION);
	scriptEngine.getContext().setAttribute(CLASSPATH, CLASS_PATH, ENGINE_SCOPE);
	scriptEngine.getContext().setAttribute(MAINCLASS, PACKAGE + "." + CLASS, ENGINE_SCOPE);
	scriptEngine.getContext().setAttribute(FILENAME, CLASS + "." + EXTENSION, ENGINE_SCOPE);
	scriptEngine.getContext().setAttribute(CLASSLOADER, getClass().getClassLoader(), ENGINE_SCOPE);

	try (FileInputStream fis = new FileInputStream(CLASS_PATH + PACKAGE_PATH + CLASS + "." + EXTENSION);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr)) {

	    final Compilable compilableEngine = (Compilable) scriptEngine;
	    final CompiledScript compiledScript = compilableEngine.compile(br);
	    final Class<?> scriptClass = (Class<?>) compiledScript.eval(scriptEngine.getContext());
	    Assert.assertEquals(scriptClass.getName(), PACKAGE + "." + CLASS);
	    Assert.assertEquals(scriptClass.getPackageName(), PACKAGE);

	    final Method main = scriptClass.getMethod(METHOD_NAME, String[].class);
	    final Object script = scriptClass.getDeclaredConstructor().newInstance();
	    main.invoke(script, (Object) (String[]) null);
	}
    }
}
