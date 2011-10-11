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

package com.masetta.spann.spring.core.visitor;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ReplaceOverride;

import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;

public final class DefSupport {
	
	private DefSupport() {}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty( BeanDefinition bd , String property , Class<T> type ) {
		return (T) bd.getPropertyValues().getPropertyValue( property ).getValue();
	}

	public static <T> T getProperty( BeanDefinitionHolder bdh, String property ,
			Class<T> type ) {
		return getProperty( bdh.getBeanDefinition(), property, type );
	}

	/**
	 * Set a bean property in the given bean definition.
	 * 
	 * @param beanDefinition
	 * @param property
	 * @param value
	 */
	public static void setProperty(BeanDefinition beanDefinition, String property, Object value) {
		beanDefinition.getPropertyValues().addPropertyValue(property, value);
	}

	public static void setProperty( BeanDefinition beanDefinition, String classname,
			String property, Object value) {
		if ( ! beanDefinition.getBeanClassName().equals(classname) ) {
			throw new IllegalArgumentException("Expected bean type " + classname + " but was "
					+ beanDefinition.getBeanClassName());
		}
		setProperty(beanDefinition, property, value);
	}

	/**
	 * Same as
	 * {@link DefSupport#setBeanReference(BeanDefinition, String, BeanDefinitionHolder)},
	 * only accepts BeanDefinitionHolder as argument.
	 * 
	 * @param bdh
	 * @param property
	 * @param dependency
	 */
	public static void setBeanReference(BeanDefinitionHolder bdh, String property,
			BeanDefinitionHolder dependency) {
		setBeanReference(bdh.getBeanDefinition(), property, dependency);
	}

	public static void setProperty(BeanDefinitionHolder bdh, String property, Object value) {
		setProperty( bdh.getBeanDefinition() , property , value );
	}

	/**
	 * Set a reference of the given BeanDefinitionHolder to the given property
	 * of the given bean definition.
	 * 
	 * @param beanDefinition
	 *            the bean definition to set the bean into.
	 * @param property
	 *            the property of the bean to set
	 * @param dependency
	 *            the BeanDefinitionHolder which should be set to the given
	 *            property.
	 */
	public static void setBeanReference(BeanDefinition beanDefinition, String property,
			BeanDefinitionHolder dependency) {
		setProperty(beanDefinition, property, dependency );
	}

	/**
	 * Replace a method in the given bean definition using the given
	 * MethodReplacer bean name.
	 * 
	 * @param beanDefinition
	 *            the bean definition who's method should be replaced.
	 * @param metadata
	 *            the metadata of the method being replaced.
	 * @param methodReplacerBeanName
	 *            the MethodReplacer bean name.
	 */
	public static void replaceMethod(BeanDefinition beanDefinition, MethodMetadata metadata,
			String methodReplacerBeanName) {
		ReplaceOverride replace = new ReplaceOverride(metadata.getName(), methodReplacerBeanName);
		for (ParameterMetadata p : metadata.getParameters()) {
			replace.addTypeIdentifier(p.getParameterClass().getName());
		}
		((AbstractBeanDefinition) beanDefinition).getMethodOverrides().addOverride(replace);
	}

}
