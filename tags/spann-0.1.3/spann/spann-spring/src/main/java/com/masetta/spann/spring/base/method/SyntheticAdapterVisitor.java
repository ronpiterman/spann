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

package com.masetta.spann.spring.base.method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.core.support.MethodMetadataSupport;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.base.method.beans.MethodDelegatorFactoryBean;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.core.visitor.VisitorSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class SyntheticAdapterVisitor extends AnnotationPathMetadataVisitor<MethodMetadata> {
	
	private final SpannLog log = SpannLogFactory.getLog( SyntheticAdapterVisitor.class );
	
	private final static String ADAPTER = SyntheticAdapter.class.getCanonicalName();

	public SyntheticAdapterVisitor() {
		super(MethodMetadata.class,ADAPTER, true);
	}

	@Override
	protected void visit(MethodMetadata metadata, ScanContext context, AnnotationPath path) {
		
		String methodOwnerBeanName = VisitorSupport.findBeanName( context, metadata.getParent() );
		if ( methodOwnerBeanName == null ) {
			log.info("Ignoring Adapter annotation: could not find bean of parent class " + metadata.getParent().getName() + 
					" for method " + metadata.getName() );
			return;
		}
		
		// 1. create an implementation of the adapter
		
		ClassMetadata toImplement = path.getAttribute(0, ClassMetadata.class, 
				SyntheticAdapter.IMPLEMENT_ATTRIBUTE , false );
		
		MethodMetadata abstractMethod = findAbstractMethod( toImplement );
		if ( abstractMethod == null ) {
			throw new IllegalConfigurationException( ADAPTER , metadata , toImplement.getName() + 
					" should have one abstract method." );
		}
		
		String adapterName = AnnotationMetadataSupport.findAttribute( 
				Collections.singletonList( path ), 
				String.class, SyntheticAdapter.NAME_ATTRIBUTE, 
				null, false, false );
		if ( adapterName == null ) {
			adapterName = metadata.getName() + "_" + toImplement.getSimpleName();
			adapterName = context.generateBeanName( adapterName ); 
		}
		
		BeanDefinition impl = VisitorSupport.implement( toImplement, context);
		BeanDefinitionHolder implHolder =
			new BeanDefinitionHolder( impl, adapterName );
		context.attach( implHolder, metadata, Artifact.METHOD, "adapter" );
		
		// apply adapter properties
		AnnotationMetadata[] props = path.getAttribute( 0, AnnotationMetadata[].class , 
				SyntheticAdapter.CLASS_PROPERTIES_ATTRIBUTE, true );
		for ( AnnotationMetadata p : props ) {
			int index = p.getAttribute( Integer.class, ClassTypeProperty.INDEX_ATTRIBUTE, false );
			String name = p.getAttribute( String.class, ClassTypeProperty.NAME_ATTRIBUTE, false );
			DefSupport.setProperty( impl, name, 
					getParameterType(metadata, index) );
		}
		
		// 2. create a delegating method replacer which delegates the abstract method
		//    to the annotated method
		
		List<String> list = new ArrayList<String>();
		for ( ParameterMetadata pm : metadata.getParameters() ) {
			list.add( pm.getParameterClass().getName() );
		}
		
		// create a method replacer
		BeanDefinitionHolder replacer = context.builder(metadata, MethodDelegatorFactoryBean.class.getCanonicalName() , metadata )
			.setReference( MethodDelegatorFactoryBean.TARGET_BEAN_PROPERTY, methodOwnerBeanName )
			.set( MethodDelegatorFactoryBean.TARGET_METHOD_NAME_PROPERTY, metadata.getName() )
			.set( MethodDelegatorFactoryBean.TARGET_PARAMETER_CLASSES_PROPERTY, list )
			.generateBeanName().addFinal();
		String replacerName = replacer.getBeanName(); 
		
		// replace impl abstract method with replacer implementation
		DefSupport.replaceMethod( impl, abstractMethod, replacerName );
		
	}

	private String getParameterType(MethodMetadata metadata, int index) {
		ClassMetadata cm = index == -1 ? metadata.getReturnClass() 
			: metadata.getParameters().get( index ).getParameterClass();
		return cm.getName();
	}

	private MethodMetadata findAbstractMethod(ClassMetadata toImplement) {
		Set<MethodMetadata> methods = MethodMetadataSupport.findAbstractMethods( toImplement );
		if ( methods.size() != 1 )
			return null;
		for ( MethodMetadata mm : methods ) {
			return mm;
		}
		throw new IllegalStateException("Unreachable");
	}

}
