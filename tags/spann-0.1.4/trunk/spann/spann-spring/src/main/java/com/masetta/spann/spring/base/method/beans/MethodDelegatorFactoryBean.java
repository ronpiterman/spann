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

package com.masetta.spann.spring.base.method.beans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.MethodReplacer;

import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.MethodMetadataSupport;
import com.masetta.spann.metadata.util.EmptyArrays;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * FactoryBean for a MethodReplacer which delegates a method call to a bean's method.
 * 
 * @author Ron Piterman
 */
public class MethodDelegatorFactoryBean implements FactoryBean, InitializingBean {
	
	public static final String TARGET_BEAN_PROPERTY = "targetBean";

	public static final String TARGET_METHOD_NAME_PROPERTY = "targetMethodName";

	public static final String TARGET_PARAMETER_CLASSES_PROPERTY = "targetParameterClasses";

	private Object targetBean;
	
	private String targetMethodName;
	
	private Method targetMethod;
	
	private Class<?>[] targetParameterClasses;
	
	private int[] parameterMapping;
	
	public Object getObject() throws Exception {
		if ( parameterMapping == null ) {
			return new SimpleMethodDelegator( targetBean , targetMethod );
		}
		else {
			return new MappingMethodDelegator( targetBean , targetMethod , parameterMapping );
		}
	}

	public Class<?> getObjectType() {
		return MethodReplacer.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	public void setTargetBean(Object target) {
		this.targetBean = target;
	}
	
	public void setTargetMethodName( String name ) {
		this.targetMethodName = name;
	}
	
	public void setTargetParameterClasses(Class<?>[] targetParameterClasses) {
		this.targetParameterClasses = targetParameterClasses;
	}

	public void setParameterMapping(int[] parameterMapping) {
		this.parameterMapping = parameterMapping;
	}

	public void afterPropertiesSet() throws Exception {
		this.targetMethod = findMethod();
	}

	private Method findMethod() {
		int expectedArguments = targetParameterClasses.length;
		
		List<Method> candidates = new ArrayList<Method>();
		for ( Method method : targetBean.getClass().getMethods() ) {
			if ( method.getName().equals( this.targetMethodName ) 
					&& method.getParameterTypes().length == expectedArguments ) {
				candidates.add( method );
			}
		}
		
		for ( Method m : candidates.toArray( EmptyArrays.METHOD ) ) {
			if ( ! matches( m ) ) {
				candidates.remove( m );
			}
		}
		if ( candidates.size() == 1 )
			return candidates.get( 0 );
		
		if ( candidates.size() == 0 ) {
			throw new IllegalConfigurationException( 
					String.format( "No method '%s' found with parameter types '%s' in %s" ,
							targetMethodName , Arrays.toString( targetParameterClasses ),
							targetBean.getClass() ) );
		}
		
		Map<Method,Integer> distances = calculateDistances( candidates );
		List<Map.Entry<Method,Integer>> sorted = sort( distances );
		if ( sorted.get( 0 ).getValue().equals( sorted.get( 1 ).getValue() ) ) {
			throw new IllegalConfigurationException( 
					String.format( "Ambiguous method '%s' found with arguments '%s'" ,
							targetMethodName , Arrays.toString( targetParameterClasses ) ) );
		}
		return sorted.get( 0 ).getKey();
	}

	private List<Entry<Method, Integer>> sort(Map<Method, Integer> distances) {
		List<Entry<Method, Integer>> list = new ArrayList<Entry<Method,Integer>>( distances.entrySet() );
		Collections.sort( list , MapEntryValueComparator.INSTANCE );
		return list;
	}

	private Map<Method, Integer> calculateDistances(List<Method> candidates) {
		Map<Method,Integer> map = new HashMap<Method, Integer>( candidates.size() );
		for ( Method method : candidates ) {
			map.put( method, getDistance( method ) );
		}
		return map;
	}

	private Integer getDistance(Method method) {
		int distance = 0;
		for ( int i = 0; i < targetParameterClasses.length; i++ ) {
			distance += getDistance( method.getParameterTypes()[i] , targetParameterClasses[i] );
		}
		return distance;
	}

	private int getDistance(Class<?> superclass, Class<?> subclass ) {
		if ( subclass == null )
			return 0;
		if ( superclass.equals( subclass ) )
			return 1;
		int sup = getDistance( superclass , subclass.getSuperclass() );
		for ( Class<?> ifc : subclass.getInterfaces() ) {
			int temp = getDistance( superclass , ifc );
			if ( temp > 0 && temp < sup )
				sup = temp;
		}
		return sup + 1;
	}

	private boolean matches(Method m) {
		final Class<?>[] p = m.getParameterTypes();
		for ( int i = 0; i < p.length; i++ ) {
			if ( targetParameterClasses[i] == null )
				continue;
			if ( p[i].isAssignableFrom( targetParameterClasses[i] ) )
				continue;
			return false;
		}
		return true;
	}
	
	private static class SimpleMethodDelegator implements MethodReplacer {
		
		private final Object bean;
		
		private final Method method;
		
		public SimpleMethodDelegator(Object bean, Method method) {
			super();
			this.bean = bean;
			this.method = method;
		}

		public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
			return this.method.invoke( bean, args );
		}
		
	}
	
	private static class MappingMethodDelegator implements MethodReplacer {
		
		private final Object bean;
		
		private final Method method;
		
		private final int[] mapping;

		public MappingMethodDelegator(Object bean, Method method, int[] mapping) {
			super();
			this.bean = bean;
			this.method = method;
			this.mapping = mapping;
		}

		public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
			Object[] invokeArgs = new Object[mapping.length];
			for ( int i = 0; i < mapping.length; i++ ) {
				invokeArgs[i] = mapping[i] == -1 ? null : args[mapping[i]];
			}
			return this.method.invoke( bean, invokeArgs );
		}
		
	}
	
	public static void configureSimple( BeanDefinition beanDefinition , String delegateBeanName, MethodMetadata methodMetadata ) {
		if ( ! MethodDelegatorFactoryBean.class.getName().equals( beanDefinition.getBeanClassName() ) ) {
			throw new IllegalArgumentException( "BeanDefinition's beanClassName is not " + MethodDelegatorFactoryBean.class.getCanonicalName() );
		}
		
		DefSupport.setProperty(beanDefinition, TARGET_BEAN_PROPERTY, new RuntimeBeanReference( delegateBeanName ) );
		DefSupport.setProperty(beanDefinition, TARGET_METHOD_NAME_PROPERTY , methodMetadata.getName() );
		DefSupport.setProperty(beanDefinition, TARGET_PARAMETER_CLASSES_PROPERTY,
				MethodMetadataSupport.getMethodParameterClassnames( methodMetadata ) );
	}
	
	private static class MapEntryValueComparator implements Comparator<Map.Entry<?,Integer>> {
		private static final MapEntryValueComparator INSTANCE = new MapEntryValueComparator();
		
		public int compare(Entry<?, Integer> o1, Entry<?, Integer> o2) {
			return o1.getValue().compareTo( o2.getValue() );
		}
	}

}
