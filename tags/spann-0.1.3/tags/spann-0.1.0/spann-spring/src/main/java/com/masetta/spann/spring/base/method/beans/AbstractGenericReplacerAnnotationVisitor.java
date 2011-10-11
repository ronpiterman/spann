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

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.base.method.ReplaceVisitor;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * Base class for Method MetadataVisitors which mutate a GenericMethodReplacer.
 * 
 * @author Ron Piterman
 */
public abstract class AbstractGenericReplacerAnnotationVisitor extends AnnotationPathMetadataVisitor<MethodMetadata> {

	public AbstractGenericReplacerAnnotationVisitor(String annotationType, boolean checkUnique) {
		super( MethodMetadata.class, annotationType, checkUnique);
	}

	@Override
	protected final void visit(MethodMetadata metadata, ScanContext context, AnnotationPath path) {
		// find replacer
		BeanDefinitionHolder replacer = context.getAttachedBean(metadata, Artifact.METHOD, 
				ReplaceVisitor.METHOD_REPLACER_ROLE );
		if ( replacer == null ) {
			throw new IllegalConfigurationException( getAnnotationType() , metadata , "No method replacer found." );
		}
		if ( ! replacer.getBeanDefinition().getBeanClassName().equals( GenericMethodReplacer.class.getName() ) ) {
			throw new IllegalConfigurationException( getAnnotationType() , metadata , "Method replacer bean is not of type GenericMethodReplacer." );
		}
		
		// find CallContextHandlerChainFactoryBean or create one
		BeanDefinitionHolder contextVisitorsFactoryBean = GenericMethodReplacerSupport.findContextVisitorsFactoryBean(metadata,
				context, path);
		
		DefSupport.setBeanReference( replacer, GenericMethodReplacer.CONTEXT_HANDLER_CHAIN_PROPERTY,
				contextVisitorsFactoryBean );
		
		process( metadata , context , path , replacer , contextVisitorsFactoryBean );
		
	}

	protected abstract void process( MethodMetadata metadata, ScanContext context, 
			AnnotationPath path, BeanDefinitionHolder methodReplacer, 
			BeanDefinitionHolder callContextVisitorsFactoryBean);
}
