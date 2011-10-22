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

package com.masetta.spann.orm.jpa.annotations;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.MethodMetadataSupport;
import com.masetta.spann.orm.jpa.beans.CountMethodReplacer;
import com.masetta.spann.orm.jpa.beans.callbacks.ConsumeHandler;
import com.masetta.spann.orm.jpa.beans.factories.AbstractQueryCallContextFactory;
import com.masetta.spann.orm.jpa.support.QueryCount;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacer;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.core.visitor.DefSupport;
import com.masetta.spann.spring.util.InstanceCache;
import com.masetta.spann.spring.util.Resolver;

public class CountVisitor extends AbstractGenericReplacerAnnotationVisitor {

	private static final String COUNT = Count.class.getCanonicalName();
	
	private static final InstanceCache instanceCache = new InstanceCache();

	public CountVisitor() {
		super( COUNT , true );
	}
	
	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		
		methodReplacer.getBeanDefinition().setBeanClassName( CountMethodReplacer.class.getCanonicalName() );
		
		BeanDefinitionHolder callContextFactory = DefSupport.getProperty( methodReplacer, 
				GenericMethodReplacer.CONTEXT_FACTORY_PROPERTY, BeanDefinitionHolder.class );
		
		BeanDefinition countCallContextFactory = new GenericBeanDefinition( callContextFactory.getBeanDefinition() );
		
		Resolver<String,String> selectCountGenerator = getGenerator( path );
		String originalJqpl = DefSupport.getProperty( countCallContextFactory, 
				AbstractQueryCallContextFactory.QUERY_PROPERTY, String.class );
		String countQuery = selectCountGenerator.resolve( originalJqpl );
		
		DefSupport.setProperty( countCallContextFactory , AbstractQueryCallContextFactory.QUERY_PROPERTY,
				countQuery );
		
		DefSupport.setProperty( methodReplacer , CountMethodReplacer.COUNT_CONTEXT_FACTORY_PROPERTY,
				countCallContextFactory );
		
		int countPositionIndex = MethodMetadataSupport.findParameterByType( metadata, QueryCount.class.getCanonicalName() , 0 );
		DefSupport.setProperty(methodReplacer , CountMethodReplacer.COUNT_ARGUMENT_INDEX_PROPERTY,
				countPositionIndex );
		
		GenericMethodReplacerSupport.addCallContextChainFactoryCallback( callContextVisitorsFactoryBean, 
				0 , new ConsumeHandler( countPositionIndex ) );
		
	}

	@SuppressWarnings("unchecked")
	private Resolver<String, String> getGenerator(AnnotationPath path) {
		ClassMetadata cm = path.getAttribute( 0, ClassMetadata.class, Count.GENERATOR_ATTRIBUTE, true );
		return instanceCache.get( cm.getName() , Resolver.class );
	}

}
