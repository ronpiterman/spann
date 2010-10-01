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

package com.masetta.spann.spring.base;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.core.visitor.SpringAnnotationsSupport;
import com.masetta.spann.spring.core.visitor.VisitorSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class ImplementVisitor extends AnnotationPathMetadataVisitor<ClassMetadata> {
	
	private SpannLog log = SpannLogFactory.getLog( ImplementVisitor.class );

	private static final String SPANN_BEAN = Implement.class.getCanonicalName();

	public ImplementVisitor() {
		super(ClassMetadata.class,SPANN_BEAN, false);
	}

	@Override
	protected void visit(ClassMetadata metadata, ScanContext context, AnnotationPath path) {
	}

	@Override
	protected void visit(ClassMetadata metadata, ScanContext context, List<AnnotationPath> paths) {
		ClassModifier mod = metadata.getModifier();
		if ( mod.getClassType().isAnnotation() || mod.getClassType().isEnum() ) {
			log.info( "Skipping Enum or Annotation " + metadata.getName() );
			return;
		}
		BeanDefinition beanDefinition;
		if  ( ! mod.isAbstract() && ! mod.getClassType().isInterface() ) {
			beanDefinition = context.createDefaultBeanDefinition( metadata, metadata.getName() );
		}
		else {
			beanDefinition = VisitorSupport.implement(metadata, context);
		}
		if ( beanDefinition == null ) {
			throw new IllegalConfigurationException( SPANN_BEAN , metadata , 
  		  		"Implement must only be used with abstract classes or interfaces." );
		}
		String name = SpringAnnotationsSupport.getOrCreateBeanName( context , paths , beanDefinition );
		BeanDefinitionHolder bdh = new BeanDefinitionHolder( beanDefinition , name );
		context.attach( bdh, metadata, Artifact.CLASS, "main" );
	}

}
