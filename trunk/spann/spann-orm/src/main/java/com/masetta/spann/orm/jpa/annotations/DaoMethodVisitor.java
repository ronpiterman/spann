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

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.MethodMetadataSupport;
import com.masetta.spann.orm.jpa.annotations.DaoMethod.Op;
import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.ResultStrategies;
import com.masetta.spann.orm.jpa.beans.callbacks.QueryArgumentsHandlers;
import com.masetta.spann.orm.jpa.beans.handler.QueryPositionSetter;
import com.masetta.spann.orm.jpa.support.QueryPosition;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.base.method.beans.CallContextChainBuilderHandler;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;
import com.masetta.spann.spring.util.Resolver;

public class DaoMethodVisitor extends AnnotationPathMetadataVisitor<MethodMetadata> {

	private static final String JPA = DaoMethod.class.getCanonicalName();

	public static final String QUERY_VISITOR_LIST_ROLE = "queryVisitorList";

	private static final String VOID = "void";

	private static final Pattern DETECT_GET_PATTERN = Pattern.compile("^get[A-Z]");

	private static final Pattern DETECT_UPDATE_PATTERN = Pattern.compile("^update[A-Z]");

	private static final Resolver<Object, Object> VOID_RESOLVER = new Resolver<Object, Object>() {
		public Object resolve(Object result) {
			return null;
		}
	};

	public DaoMethodVisitor() {
		super( MethodMetadata.class, JPA, false, true );
	}
	
	@Override
	protected void visit(MethodMetadata metadata, ScanContext context, AnnotationPath path) {

		// create result resolver
		Resolver<Object, Object> resultTransformer = null;
		if ( metadata.getReturnClass().getName().equals(VOID) )
			resultTransformer = VOID_RESOLVER;

		// create query factory
		Resolver<? extends Object, QueryCallContext> queryExecutor = createQueryExecutor(metadata, path);
		
		BeanDefinitionHolder dao = context.getAttachedBean(metadata, Artifact.CLASS, "main" );

		BeanDefinitionHolder replacer = GenericMethodReplacerSupport.replaceMethod(
				dao.getBeanDefinition(), metadata, context, path );
		
		GenericMethodReplacerSupport.setResultFactory( replacer, queryExecutor );
		GenericMethodReplacerSupport.setResultTransformer( replacer, resultTransformer );
		
		BeanDefinitionHolder contextVisitorsFactoryBean = GenericMethodReplacerSupport.findContextVisitorsFactoryBean(metadata,
				context, path);
		
		int queryPositionIndex = MethodMetadataSupport.findParameterByType( metadata, QueryPosition.class.getCanonicalName() , 0 );
		if ( queryPositionIndex > -1 ) {
			GenericMethodReplacerSupport.addCallContextVisitorsBuilderCallback( contextVisitorsFactoryBean, 0, 
					new CallContextChainBuilderHandler<QueryCallContext>( 
							new QueryPositionSetter( queryPositionIndex ) , queryPositionIndex ) );
		}
		
		// per default use positional parameters
		GenericMethodReplacerSupport.addCallContextVisitorsBuilderCallback( contextVisitorsFactoryBean, -1, 
				QueryArgumentsHandlers.Positional.INSTANCE );
		
	}

	private Resolver<? extends Object, QueryCallContext> createQueryExecutor(MethodMetadata metadata,
			AnnotationPath path) {
		Op op = path.getAttribute(0, EnumValue.class, DaoMethod.OP_ATTRIBUTE, true).resolve(
				Op.class);
		switch ( op ) {
			case DETECT:
				String name = metadata.getName();
				if ( DETECT_GET_PATTERN.matcher(name).find() ) {
					return ResultStrategies.GET;
				}
				if ( DETECT_UPDATE_PATTERN.matcher(name).find() ) {
					return ResultStrategies.UPDATE;
				}
				// else return find (so no switch break here...)
			case FIND:
				if ( isCollection(metadata.getReturnClass()) ) {
					return ResultStrategies.FIND;
				} else {
					return ResultStrategies.FIND_ONE;
				}
			case GET:
				return ResultStrategies.GET;
			case UPDATE:
				return ResultStrategies.UPDATE;
		}
		throw new IllegalArgumentException("Unknown op value: " + op);
	}

	private boolean isCollection(ClassMetadata returnClass) {
		return returnClass.getName().equals(Collection.class.getCanonicalName())
				|| returnClass.getName().equals(List.class.getCanonicalName());
	}

}
