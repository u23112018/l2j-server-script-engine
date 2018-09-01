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
package com.l2jserver.script.engine.tools;

import static com.l2jserver.script.engine.JavaScriptEngine.CLASSLOADER;
import static com.l2jserver.script.engine.JavaScriptEngine.CLASSPATH;
import static com.l2jserver.script.engine.JavaScriptEngine.MAINCLASS;
import static javax.script.ScriptContext.ENGINE_SCOPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.mdkt.compiler.InMemoryJavaCompiler;

/**
 * Java Engine Tools.
 * 
 * @author Zoey76
 */
public class JavaEngineTools {

    private static final String CONTEXT = "context";

    private static final String ARGUMENTS = "arguments";

    private static final String SET_SCRIPT_CONTEXT = "setScriptContext";

    private static final String MAIN = "main";

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final Class<?>[] ARG_SCRIPT_CONTEXT = new Class[] { ScriptContext.class };

    private static final Class<?>[] ARG_MAIN = new Class[] { String[].class };

    private static final InMemoryJavaCompiler COMPILER = InMemoryJavaCompiler.newInstance();

    public static Object evalScript(String script, ScriptContext context) throws ScriptException {
	// JSR-223 requirement
	context.setAttribute(CONTEXT, context, ENGINE_SCOPE);

	try {
	    final Class<?> clazz = COMPILER
		    .useOptions("-" + CLASSPATH, context.getAttribute(CLASSPATH, ENGINE_SCOPE).toString()) //
		    .useParentClassLoader((ClassLoader) context.getAttribute(CLASSLOADER, ENGINE_SCOPE)) //
		    .addSource(context.getAttribute(MAINCLASS, ENGINE_SCOPE).toString(), script) //
		    .compile(context.getAttribute(MAINCLASS, ENGINE_SCOPE).toString(), script);
	    final boolean isPublicClazz = Modifier.isPublic(clazz.getModifiers());
	    final Method setCtxMethod = findMethod(clazz, SET_SCRIPT_CONTEXT, ARG_SCRIPT_CONTEXT);
	    if (setCtxMethod != null) {
		if (!isPublicClazz) {
		    setCtxMethod.setAccessible(true);
		}
		setCtxMethod.invoke(null, new Object[] { context });
	    }

	    final Method mainMethod = findMethod(clazz, MAIN, ARG_MAIN);
	    if (mainMethod != null) {
		if (!isPublicClazz) {
		    mainMethod.setAccessible(true);
		}

		final String args[] = getArguments(context);

		mainMethod.invoke(null, new Object[] { args });
	    }
	    return clazz;
	} catch (Exception ex) {
	    throw new ScriptException(ex);
	}
    }

    public static String readerToString(Reader reader) throws ScriptException {
	try (BufferedReader in = new BufferedReader(reader)) {
	    final StringBuilder result = new StringBuilder();
	    String line = null;
	    while ((line = in.readLine()) != null) {
		result.append(line).append(System.lineSeparator());
	    }
	    return result.toString();
	} catch (IOException ex) {
	    throw new ScriptException(ex);
	}
    }

    private static Method findMethod(Class<?> clazz, String methodName, Class<?>[] args) {
	try {
	    final Method mainMethod = clazz.getMethod(methodName, args);
	    final int modifiers = mainMethod.getModifiers();
	    if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
		return mainMethod;
	    }
	} catch (NoSuchMethodException nsme) {
	}
	return null;
    }

    private static String[] getArguments(ScriptContext ctx) {
	int scope = ctx.getAttributesScope(ARGUMENTS);
	if (scope != -1) {
	    Object obj = ctx.getAttribute(ARGUMENTS, scope);
	    if (obj instanceof String[]) {
		return (String[]) obj;
	    }
	}
	return EMPTY_STRING_ARRAY;
    }
}
