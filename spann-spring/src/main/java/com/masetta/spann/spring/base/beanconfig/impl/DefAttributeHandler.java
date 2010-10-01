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

package com.masetta.spann.spring.base.beanconfig.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.StringUtils;

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.beanconfig.AttributeHandler;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class DefAttributeHandler implements AttributeHandler {

	public void handle(AnnotatedElementMetadata metadata, ScanContext context,
			BeanDefinition beanDefinition, AnnotationMetadata annotation, String name) {
		Object value = annotation.getAttribute( name , true );
		String methodName = "set" + StringUtils.capitalize( name );
		Method method = BeanUtils.findMethod( beanDefinition.getClass(), methodName, new Class[] { value.getClass() } );
		try {
			method.invoke( beanDefinition, value );
		} catch (IllegalAccessException e) {
			throw new IllegalConfigurationException( e );
		} catch (InvocationTargetException e) {
			throw new IllegalConfigurationException( e );
		}
	}

}
