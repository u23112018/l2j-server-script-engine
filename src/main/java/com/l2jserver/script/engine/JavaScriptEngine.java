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
package com.l2jserver.script.engine;

import static com.l2jserver.script.engine.factory.JavaScriptEngineFactory.JAVA;
import static com.l2jserver.script.engine.factory.JavaScriptEngineFactory.JAVA_LANGUAGE_VERSION;
import static com.l2jserver.script.engine.tools.JavaEngineTools.evalScript;
import static com.l2jserver.script.engine.tools.JavaEngineTools.readerToString;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.l2jserver.script.engine.factory.JavaScriptEngineFactory;
import com.l2jserver.script.engine.script.JavaCompiledScript;

/**
 * Java Script Engine implementation.
 * 
 * @author Zoey76
 */
public class JavaScriptEngine extends AbstractScriptEngine implements Compilable {

    public static final String CLASSLOADER = "ClassLoader";

    public static final String CLASSPATH = "classpath";

    public static final String MAINCLASS = "mainClass";

    public static final String SOURCEPATH = "sourcepath";

    private final ScriptEngineFactory scriptEngineFactory;

    private static final String ENGINE_DESCRIPTION = "Engine " + JAVA + " v" + JAVA_LANGUAGE_VERSION;
    
    public JavaScriptEngine(ScriptEngineFactory scriptEngineFactory) {
	this.scriptEngineFactory = scriptEngineFactory;
    }

    public JavaScriptEngine() {
	this(new JavaScriptEngineFactory());
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
	return evalScript(script, context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
	return eval(readerToString(reader), context);
    }

    @Override
    public Bindings createBindings() {
	return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
	return scriptEngineFactory;
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
	return new JavaCompiledScript(this, script);
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
	return compile(readerToString(script));
    }

    @Override
    public String toString() {
	return ENGINE_DESCRIPTION;
    }
}
