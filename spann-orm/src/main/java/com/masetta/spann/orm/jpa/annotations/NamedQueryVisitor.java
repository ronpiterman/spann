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

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.util.StringUtils;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.orm.jpa.beans.factories.NamedQueryFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class NamedQueryVisitor extends AbstractGenericReplacerAnnotationVisitor {

	private static final String NAMED_QUERY = NamedQuery.class.getCanonicalName();

	public NamedQueryVisitor() {
		super(NAMED_QUERY, true);
	}

	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		String name = path.getAttribute( 0, String.class, "value", false );
		if ( ! StringUtils.hasLength( name ) ) {
			name = getDefaultName( metadata );
		}
		
		BeanDefinitionHolder dao = DaoVisitor.getDao( context , metadata );
		
		BeanDefinitionHolder h = 
			context.builder( metadata, NamedQueryFactory.class.getCanonicalName() , path )
			.set( NamedQueryFactory.QUERY_PROPERTY, name )
			.set( NamedQueryFactory.ENTITY_MANAGER_SUPPORT_PROPERTY , dao )
			.addFinal();
		
		GenericMethodReplacerSupport.setCallContextFactory( methodReplacer, h );
	}

	private String getDefaultName(MethodMetadata metadata) {
		return getEntityName( metadata ) + "." + metadata.getName();
	}

	private String getEntityName(MethodMetadata metadata) {
		ClassMetadata entityType = ClassMetadataSupport.findTypeParameterCapture( (ClassMetadata)metadata.getParent(), 
				BaseDao.class.getCanonicalName() , 0 );
		
		if ( entityType != null ) {
			return StringUtils.unqualify( entityType.getName() );
		}
		
		throw new IllegalConfigurationException( NAMED_QUERY , metadata , 
				"Class does not extends BaseDao or specify generic entity type");
	}
	
}
