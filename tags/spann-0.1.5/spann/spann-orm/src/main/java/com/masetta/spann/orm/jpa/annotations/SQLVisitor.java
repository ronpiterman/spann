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

import java.text.MessageFormat;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.orm.jpa.beans.callbacks.ConsumeHandler;
import com.masetta.spann.orm.jpa.beans.factories.JpqlSimpleQueryFactory;
import com.masetta.spann.orm.jpa.beans.factories.NativeDynamicQueryFactory;
import com.masetta.spann.orm.jpa.beans.factories.NativeQueryFactory;
import com.masetta.spann.orm.jpa.support.MessageFormatSupport;
import com.masetta.spann.spring.BeanDefinitionBuilder;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.method.beans.AbstractGenericReplacerAnnotationVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class SQLVisitor extends AbstractGenericReplacerAnnotationVisitor {

	private static final String SQL_ANNOTATION = SQL.class.getCanonicalName();

	public SQLVisitor() {
		super(SQL_ANNOTATION, true);
	}

	@Override
	protected void process(MethodMetadata metadata, ScanContext context, AnnotationPath path,
			BeanDefinitionHolder methodReplacer, BeanDefinitionHolder callContextVisitorsFactoryBean) {
		String query = path.getAttribute( 0, String.class, "value", false );
		ClassMetadata resultSetType = path.getAttribute( 0, ClassMetadata.class, SQL.RESULT_SET_CLASS , false );
		String resultSetMapping = path.getAttribute( 0, String.class, SQL.RESULT_SET_MAPPING , false );
		boolean useMessageFormat = path.getAttribute( 0, Boolean.class, SQL.USE_MESSAGE_FORMAT , true );
		
		MessageFormat format = useMessageFormat ? MessageFormatSupport.createMessageFormat( query ) : null;
		
		BeanDefinitionHolder dao = DaoVisitor.getDao( context , metadata );
		
		BeanDefinitionBuilder b = null;
		if ( format != null ) {
			int[] argsToConsume = MessageFormatSupport.getMessageArguments( format );
			b = context.builder( metadata, 
					NativeDynamicQueryFactory.class.getCanonicalName() , path );
			
			GenericMethodReplacerSupport.addCallContextChainFactoryCallback( 
					callContextVisitorsFactoryBean, 0, new ConsumeHandler( argsToConsume ) );
		} else {
			b = context.builder( metadata, 
					NativeQueryFactory.class.getCanonicalName() , path );
		}
		
		b.set( NativeDynamicQueryFactory.QUERY_PROPERTY, query )
			.set( NativeDynamicQueryFactory.ENTITY_MANAGER_SUPPORT_PROPERTY , dao );
		
		if ( resultSetMapping != null && resultSetType != null ) {
			throw new IllegalConfigurationException( SQL_ANNOTATION, metadata, "resultSetMapping and resultSetType are mutualy exclusive. Specify only one of both.");
		}
		
		if ( resultSetMapping != null ) {
			b.set( NativeQueryFactory.RESULT_SET_MAPPING_PROPERTY, resultSetMapping );
		}
		
		if ( resultSetType != null ) {
			b.set( NativeQueryFactory.RESULT_SET_CLASS_PROPERTY, resultSetType.getName() );
		}
		
		BeanDefinitionHolder h = b.addFinal();
		
		GenericMethodReplacerSupport.setCallContextFactory( methodReplacer, h );
	}

}
