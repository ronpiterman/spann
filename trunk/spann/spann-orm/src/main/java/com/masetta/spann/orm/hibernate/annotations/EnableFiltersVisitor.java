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

package com.masetta.spann.orm.hibernate.annotations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.MethodMetadataSupport;
import com.masetta.spann.orm.hibernate.callbacks.DynamicFilterEnablerChainBuilderHandler;
import com.masetta.spann.orm.hibernate.callbacks.FilterListEnablerChainBuilderHandler;
import com.masetta.spann.orm.hibernate.support.FilterActivation;
import com.masetta.spann.orm.hibernate.support.FilterActivations;
import com.masetta.spann.spring.BeanDefinitionBuilder;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;

public class EnableFiltersVisitor implements MetadataVisitor<MethodMetadata> {

	private static final String FILTER_ACTIVATIONS = FilterActivations.class.getCanonicalName();
	private static final String FILTER_LIST_HANDLER = FilterListEnablerChainBuilderHandler.class.getCanonicalName();
	private static final String ENABLE_FILTERS = EnableFilters.class.getCanonicalName();
	private static final String DYNAMIC_FILTER_HANDLER = DynamicFilterEnablerChainBuilderHandler.class.getCanonicalName();

	public void visit(MethodMetadata metadata, ScanContext context) {
		int filterActvIndex = MethodMetadataSupport.findParameterByType( metadata, FILTER_ACTIVATIONS , 0 );
		
		BeanDefinitionHolder dao = context.getAttachedBean(metadata, Artifact.CLASS, "main" );
		
		if ( filterActvIndex >= 0 ) {
			addHandler( metadata, context , FILTER_LIST_HANDLER , dao, filterActvIndex );
		}
		
		List<AnnotationPath> allFilters = metadata.findAnnotationPaths( ENABLE_FILTERS );
		for ( AnnotationPath path : allFilters ) {
			AnnotationMetadata[] enableFilters = path.getAttribute( 0, AnnotationMetadata[].class, EnableFilters.FILTERS_ATTRIBUTE, true );
			List<FilterActivation> activations = new ArrayList<FilterActivation>();
			for ( AnnotationMetadata enableFilter : enableFilters ) {
				activations.add( createFilterActivation( enableFilter ) );
			}
			if ( ! activations.isEmpty() )
				addHandler(metadata, context, DYNAMIC_FILTER_HANDLER, dao, activations);
		}
	}

	private FilterActivation createFilterActivation(AnnotationMetadata enableFilter) {
		FilterActivation fa = new FilterActivation( enableFilter.getAttribute( String.class, 
				EnableFilter.NAME_ATTRIBUTE , true ) );
		for ( AnnotationMetadata am : enableFilter.getAttribute( AnnotationMetadata[].class, 
				EnableFilter.PARAMETERS_ATTRIBUTE, true ) ) {
			fa.setParameter( 
					am.getAttribute( String.class , FilterParam.NAME_ATTRIBUTE , true ),
					am.getAttribute( Integer.class , FilterParam.INDEX_ATTRIBUTE , true )
				);
		}
		return fa;
	}

	private void addHandler(MethodMetadata metadata, ScanContext context, String classname,
			Object... ctorArguments) {
		BeanDefinitionHolder builder = GenericMethodReplacerSupport.findContextVisitorsFactoryBean( 
				metadata, context , null ); 
		BeanDefinitionBuilder bdb = context.builder( metadata, classname, this );
		for ( Object ctorArg : ctorArguments )
			bdb.addConstructorArgument( ctorArg );
		
		BeanDefinitionHolder callback = bdb.addFinal();
		GenericMethodReplacerSupport.addCallContextVisitorsBuilderCallback( builder, 0 , 
				new RuntimeBeanReference( callback.getBeanName() ) );
	}

	public <M extends Metadata> boolean supports(M metadata) {
		return metadata instanceof MethodMetadata;
	}

}
