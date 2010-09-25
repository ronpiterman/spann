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

import java.text.ParseException;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.util.StringUtils;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.orm.jpa.beans.factories.JpqlSimpleQueryFactory;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * Visits methods annotated with {@link ByMethodName}.
 * 
 * @author Ron Piterman
 */
public class ByMethodNameVisitor extends AbstractGenericReplacerAnnotationVisitor {
	
	private static final String BY_METHOD_NAME = ByMethodName.class.getCanonicalName();

	public ByMethodNameVisitor() {
		super( BY_METHOD_NAME, false );
	}

	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		ClassMetadata entity = ClassMetadataSupport.findTypeParameterCapture( (ClassMetadata) metadata.getParent(),
				BaseDao.class.getCanonicalName(), 0 );
		
		MethodNameJpqlGenerator g = new MethodNameJpqlGenerator( metadata.getName(), 
				StringUtils.unqualify( entity.getName() ) , null );
		try {
			
			String jpql = g.getEql();
			
			BeanDefinitionHolder factory = context.builder(metadata, 
					JpqlSimpleQueryFactory.class.getCanonicalName() , path )
					.set( "query", jpql )
					.addFinal();
				
			GenericMethodReplacerSupport.setCallContextFactory(methodReplacer, 
					factory );
			
			SpannLogFactory.getLog( metadata.getParent().getName()).info( "JPQL created from method name: " + jpql );
			
		}
		catch (ParseException p) {
			throw new IllegalConfigurationException( BY_METHOD_NAME, metadata , p );
		}
	}

}
