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
package com.l2jserver.script.engine.script;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.l2jserver.script.engine.JavaScriptEngine;

/**
 * Java Compiled Script implementation.
 * 
 * @author Zoey76
 */
public class JavaCompiledScript extends CompiledScript {

    private final JavaScriptEngine scriptEngine;

    private final String script;

    public JavaCompiledScript(JavaScriptEngine scriptEngine, String script) {
	this.scriptEngine = scriptEngine;
	this.script = script;
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
	return scriptEngine.eval(script, context);
    }

    @Override
    public ScriptEngine getEngine() {
	return scriptEngine;
    }
}