/**
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masetta.spann.orm.jpa.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.masetta.spann.spring.util.Factory;

public class DaoMethodInvoker  {
	
	private final Class<?> daoClass;
	
	private final Method daoMethod;
	
	private final Object dao;
	
	private final Object[] arguments;
	
	public DaoMethodInvoker(Class<?> daoClass, Method daoMethod, Object dao, Object[] arguments) {
		super();
		this.daoClass = daoClass;
		this.daoMethod = daoMethod;
		this.dao = dao;
		this.arguments = arguments;
	}

	public void invoke() {
		try {
			Object[] args = process( arguments );
			daoMethod.invoke( dao, arguments );
		} catch (IllegalArgumentException e) {
			throw new RuntimeException( e );
		} catch (IllegalAccessException e) {
			throw new RuntimeException( e );
		} catch (InvocationTargetException e) {
			throw new RuntimeException( e );
		}
	}
	
	private Object[] process(Object[] a) {
		Object[] resolved = new Object[a.length];
		for ( int i = 0; i < a.length; i++ ) {
			resolved[i] = ( a[i] instanceof Factory ) ? ((Factory<?>)a[i]).create() : a[i];
		}
		return resolved;
	}

	public String toString() {
		return "Method invoker of " + daoClass.getSimpleName() + "." + daoMethod.getName() + "(" + 
			Arrays.toString( daoMethod.getParameterTypes() ) + ")";
	}

	public Class<?> getDaoClass() {
		return daoClass;
	}

	public Method getMethod() {
		return daoMethod;
	}

	public Object getDao() {
		return dao;
	}

	public Object[] getArguments() {
		return arguments;
	}

}
