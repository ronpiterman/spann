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

package com.masetta.spann.spring.base.beanconfig;

import org.springframework.beans.factory.config.BeanDefinition;

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.spring.ScanContext;

/**
 * Handles a bean-config annotation attribute. BeanConfigVisitor delegates handling
 * of annotation attribute to instances of AttributeHandler.
 * <p>
 * Implementations should have a public no-arguments constructor.
 * 
 * @author Ron Piterman
 */
public interface AttributeHandler {

	/**
	 * Handle an annotation attribute value. 
	 * 
	 * @param metadata the annotated metadata element
	 * @param context the scan context
	 * @param beanDefinition the bean definition to optionaly mutate
	 * @param annotation the annotation information
	 * @param name the annotation attribute name being handled
	 */
	void handle(AnnotatedElementMetadata metadata, ScanContext context,
			BeanDefinition beanDefinition, AnnotationMetadata annotation, String name);

}
