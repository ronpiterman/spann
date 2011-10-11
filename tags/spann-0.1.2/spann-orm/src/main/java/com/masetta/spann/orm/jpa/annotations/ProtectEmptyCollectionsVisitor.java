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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.util.EmptyArrays;
import com.masetta.spann.orm.jpa.annotations.DaoMethod.Op;
import com.masetta.spann.orm.jpa.beans.handler.ProtectEmptyCollectionsHandler;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.AnnotationPathMetadataVisitor;
import com.masetta.spann.spring.base.method.beans.GenericMethodReplacerSupport;

public class ProtectEmptyCollectionsVisitor extends AnnotationPathMetadataVisitor<MethodMetadata> {
	
	private static final Set<String> COLLECTION_CLASS_NAMES = createSupportedCollections();

	private static final String DAO_METHOD = DaoMethod.class.getCanonicalName();
	private static final String PROTECT_EMPTY_COLLECTIONS = ProtectEmptyCollections.class.getCanonicalName();

	public ProtectEmptyCollectionsVisitor() {
		super(MethodMetadata.class, PROTECT_EMPTY_COLLECTIONS, false , true );
	}

	private static Set<String> createSupportedCollections() {
		Set<String> set = new HashSet<String>();
		set.add( Collection.class.getCanonicalName() );
		set.add( List.class.getCanonicalName() );
		set.add( Set.class.getCanonicalName() );
		return set;
	}

	@Override
	protected void visit(MethodMetadata metadata, ScanContext context, AnnotationPath path) {
		List<AnnotationPath> daoMethodPaths = metadata.findAnnotationPaths( DAO_METHOD );
		if ( daoMethodPaths.isEmpty() )
			return;
		
		Integer[] collectionArgumentIndexes = getCollectionArgumentIndexes( metadata );
		if ( collectionArgumentIndexes.length == 0 ) {
			return;
		}
		
		Op op = DaoMethodVisitor.getOp( metadata, daoMethodPaths.get( 0 ) );
		Object emptyValue = getEmptyValue( op );
		
		
		BeanDefinitionHolder callContextChainFactoryBean = GenericMethodReplacerSupport.findCallContextChainFactoryBean(
				metadata, context, path );
		ProtectEmptyCollectionsHandler callback = new ProtectEmptyCollectionsHandler(emptyValue, collectionArgumentIndexes );
		GenericMethodReplacerSupport.addCallContextChainFactoryCallback( callContextChainFactoryBean, 0, callback );
	}

	private Object getEmptyValue(Op op) {
		switch ( op ) {
			case FIND:
				return Collections.EMPTY_LIST;
			case GET:
				return null;
			case UPDATE:
				return 0;
		}
		throw new IllegalArgumentException("Unknown op : " + op );
	}

	private Integer[] getCollectionArgumentIndexes(MethodMetadata metadata) {
		List<Integer> list = null;
		int i = 0;
		for ( ParameterMetadata m : metadata.getParameters() ) {
			ClassMetadata cm = m.getParameterClass();
			if ( COLLECTION_CLASS_NAMES.contains( cm.getName() ) ) {
				if ( list == null ) {
					list = new ArrayList<Integer>();
				}
				list.add( i );
			}
			i++;
		}
		if ( list == null )
			return EmptyArrays.INTEGER;
		
		return list.toArray( EmptyArrays.INTEGER );
	}

}
