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
package com.l2jserver.script.engine.factory;

import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.l2jserver.script.engine.JavaScriptEngine;

/**
 * Java Script Engine Factory implementation.
 * 
 * @author Zoey76
 */
public class JavaScriptEngineFactory implements ScriptEngineFactory {

    public static final String JAVA = "Java";

    public static final String JAVA_LANGUAGE_VERSION = "10.0.2";

    private static final String THREADING = "THREADING";

    private static final String MULTITHREADED = "MULTITHREADED";

    private static final String EXTENSION = "java";

    private static final String VERSION = "2.0";

    private static final List<String> NAMES = List.of(JAVA);

    private static final List<String> EXTENSIONS = List.of(EXTENSION);

    private static final List<String> MIME_TYPES = Collections.emptyList();

    private static long nextClassNum = 0L;

    @Override
    public String getEngineName() {
	return JAVA;
    }

    @Override
    public String getEngineVersion() {
	return VERSION;
    }

    @Override
    public List<String> getExtensions() {
	return EXTENSIONS;
    }

    @Override
    public String getLanguageName() {
	return JAVA;
    }

    @Override
    public String getLanguageVersion() {
	return JAVA_LANGUAGE_VERSION;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
	final StringBuilder buf = new StringBuilder();
	buf.append(obj);
	buf.append('.');
	buf.append(m);
	buf.append('(');
	if (args.length != 0) {
	    int i = 0;
	    for (; i < (args.length - 1); i++) {
		buf.append(args[i] + ", ");
	    }
	    buf.append(args[i]);
	}
	buf.append(')');
	return buf.toString();
    }

    @Override
    public List<String> getMimeTypes() {
	return MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
	return NAMES;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
	final StringBuilder buf = new StringBuilder();
	buf.append("System.out.print(\"");
	int len = toDisplay.length();
	for (int i = 0; i < len; i++) {
	    char ch = toDisplay.charAt(i);
	    switch (ch) {
	    case '"': {
		buf.append("\\\"");
		break;
	    }
	    case '\\': {
		buf.append("\\\\");
		break;
	    }
	    default: {
		buf.append(ch);
		break;
	    }
	    }
	}
	buf.append("\");");
	return buf.toString();
    }

    @Override
    public String getParameter(String key) {
	if (ScriptEngine.ENGINE.equals(key)) {
	    return getEngineName();
	}
	if (ScriptEngine.ENGINE_VERSION.equals(key)) {
	    return getEngineVersion();
	}
	if (ScriptEngine.NAME.equals(key)) {
	    return getEngineName();
	}
	if (ScriptEngine.LANGUAGE.equals(key)) {
	    return getLanguageName();
	}
	if (ScriptEngine.LANGUAGE_VERSION.equals(key)) {
	    return getLanguageVersion();
	}
	if (THREADING.equals(key)) {
	    return MULTITHREADED;
	}
	return null;
    }

    @Override
    public String getProgram(String... statements) {
	final StringBuilder buf = new StringBuilder();
	buf.append("class ");
	buf.append(getClassName());
	buf.append(" {\n");
	buf.append("    public static void main(String[] args) {\n");
	if (statements.length != 0) {
	    for (String statement : statements) {
		buf.append("        ");
		buf.append(statement);
		buf.append(";\n");
	    }
	}
	buf.append("    }\n");
	buf.append("}\n");
	return buf.toString();
    }

    @Override
    public ScriptEngine getScriptEngine() {
	return new JavaScriptEngine();
    }

    private String getClassName() {
	return "com_sun_script_java_Main$" + getNextClassNumber();
    }

    private static synchronized long getNextClassNumber() {
	return nextClassNum++;
    }
}
