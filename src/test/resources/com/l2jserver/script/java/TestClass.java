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
package com.l2jserver.script.java;

import java.lang.reflect.Method;

import com.l2jserver.script.java.child.ChildTestClass;

/**
 * Test class example.
 * 
 * @author Zoey76
 */
public class TestClass {

    public static void main(String[] args) throws Exception {
	final Method m = ChildTestClass.class.getMethod("getCountries");
	final ChildTestClass child = ChildTestClass.class.getDeclaredConstructor().newInstance();
	System.out.println(m.invoke(child));
    }
}
