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
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport.ResolutionPolicy;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.core.visitor.DefSupport;

/**
 * The default AttributeHandler; sets the bean property named as the annotation
 * attribute to the current attribute value.
 * 
 * @author Ron Piterman
 */
public class DefaultAttributeHandler implements AttributeHandler {
	
	private final SpannLog log = SpannLogFactory.getLog( DefaultAttributeHandler.class );

	public void handle(AnnotatedElementMetadata metadata, ScanContext context,
			BeanDefinition beanDefinition, AnnotationMetadata annotation, String name) {
		log.debug("handle attribute " + name );
		DefSupport.setProperty( beanDefinition, name, 
				getAttributeValue(annotation, name) );
	}

	private Object getAttributeValue(AnnotationMetadata annotation, String name) {
		Object o = annotation.getAttribute( name, true);
		return AnnotationMetadataSupport.resolveAttributeValue( o,  ResolutionPolicy.STRING, 
				ResolutionPolicy.STRING );
	}

}
