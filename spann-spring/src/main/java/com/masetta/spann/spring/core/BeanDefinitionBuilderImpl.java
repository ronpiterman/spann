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

package com.masetta.spann.spring.core;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.BeanDefinitionBuilder;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.core.visitor.VisitorSupport;

public class BeanDefinitionBuilderImpl implements BeanDefinitionBuilder {
	
	private final ScanContext scanContext;
	
	private final Metadata metadata;
	
	private final BeanDefinition beanDefinition;
	
	private final Object source;
	
	private boolean mutable = true;
	
	private BeanDefinitionHolder beanDefinitionHolder; 
	
	public BeanDefinitionBuilderImpl( ScanContext context , Metadata metadata, BeanDefinition def ) {
		this.scanContext = context;
		this.metadata = metadata;
		this.source = def.getSource();
		this.beanDefinition = def;
	}

	public BeanDefinitionHolder addFinal() {
		checkMutable();
		scanContext.addFinal( getBeanDefinitionHolder() );
		mutable = false;
		return getBeanDefinitionHolder();
	}

	public BeanDefinitionHolder attach(Artifact scope, String role) {
		checkMutable();
		scanContext.attach( getBeanDefinitionHolder(), metadata, scope, role);
		mutable = false;
		return getBeanDefinitionHolder();
	}

	private BeanDefinitionHolder getBeanDefinitionHolder() {
		if ( this.beanDefinitionHolder == null ) {
			generateBeanName();
		}
		return this.beanDefinitionHolder;
	}

	public BeanDefinitionBuilder generateBeanName() {
		checkCreateBdh();
		this.beanDefinitionHolder = new BeanDefinitionHolder( beanDefinition, 
				scanContext.generateBeanName(beanDefinition));
		return this;
	}

	private void checkCreateBdh() {
		checkMutable();
		if ( this.beanDefinitionHolder != null ) {
			throw new IllegalStateException("BeanDefinitionHolder already created / bean name already set.");
		}
	}

	public Object get(String property) {
		return beanDefinition.getPropertyValues().getPropertyValue( property );
	}

	public BeanDefinitionBuilder set(String property, Object value) {
		checkMutable();
		beanDefinition.getPropertyValues().addPropertyValue( property , value );
		return this;
	}

	public BeanDefinitionBuilder setBeanName(String name) {
		checkCreateBdh();
		if ( name == null ) {
			generateBeanName();
		}
		else {
			this.beanDefinitionHolder = new BeanDefinitionHolder( beanDefinition, name );
		}
		return this;
	}

	public BeanDefinitionBuilder setReference( String property, Artifact scope, String classname, String role ) {
		checkMutable();
		VisitorSupport.getOrCreateAndAttach( scanContext, metadata, scope, role, source, classname );
		return this;
	}

	public BeanDefinitionBuilder setReference(String property, String beanname) {
		checkMutable();
		set( property , new RuntimeBeanReference( beanname ) );
		return this;
	}
	
	public BeanDefinitionBuilder setReference(String property, BeanDefinitionHolder bdh) {
		checkMutable();
		set( property , bdh );
		return this;
	}
	
	public BeanDefinitionBuilder setReference(String property, BeanDefinition bd) {
		checkMutable();
		set( property , bd );
		return this;
	}

	public BeanDefinitionBuilder addConstructorArgument( Object value ) {
		checkMutable();
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue( value );
		return this;
	}
	
	private void checkMutable() {
		if ( ! mutable ) {
			throw new IllegalStateException("BeanDefinitionBuilder is finalized.");
		}
	}

}
