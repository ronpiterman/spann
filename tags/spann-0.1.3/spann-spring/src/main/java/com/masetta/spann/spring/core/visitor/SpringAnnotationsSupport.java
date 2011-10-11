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

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.StringUtils;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.util.Predicate;
import com.masetta.spann.spring.ScanContext;

public final class SpringAnnotationsSupport {
	
	private static final Predicate<String> IS_EMPTY_PREDICATE = new Predicate<String>() {
		public boolean evaluate(String value) {
			return ! StringUtils.hasLength( value );
		}
	};
	
	private SpringAnnotationsSupport() {}

	public static String getOrCreateBeanName(ScanContext context, List<AnnotationPath> paths,
			BeanDefinition beanDefinition) {
		String name = AnnotationMetadataSupport.findAttribute( paths, String.class, "value" , 
				IS_EMPTY_PREDICATE, false , false );
	    if ( name == null ) {
	    	name = context.generateBeanName( beanDefinition );
	    }
		return name;
	}

}
