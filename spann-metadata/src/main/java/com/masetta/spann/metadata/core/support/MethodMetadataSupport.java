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

package com.masetta.spann.metadata.core.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;

public abstract class MethodMetadataSupport {
	
	private MethodMetadataSupport() {}

	/**
	 * Find the first method parameter which is the given class or a subclass of it.
	 * @param metadata
	 * @param canonicalName
	 * @param dimension array dimension
	 * @return
	 */
	public static int findParameterByType(MethodMetadata metadata, String canonicalName , int dimension ) {
		int i = 0;
		for (ParameterMetadata pm : metadata.getParameters() ) {
			if ( ClassMetadataSupport.instanceOf( pm.getParameterClass() , canonicalName , dimension ) )
				return i;
			i++;
		}
		return -1;
	}

	public static List<String> getMethodParameterClassnames(MethodMetadata methodMetadata) {
		List<String> list = new ArrayList<String>( methodMetadata.getParameters().size() );
		for ( ParameterMetadata pm : methodMetadata.getParameters() ) {
			list.add( ClassMetadataSupport.getReflectionClassnameFor( pm.getParameterClass() ) );
		}
		return list;
	}

	public static Set<MethodMetadata> findAbstractMethods(ClassMetadata classMetadata ) {
		return collectAbstractMethods( classMetadata , new HashSet<MethodMetadata>() );
	}

	private static Set<MethodMetadata> collectAbstractMethods(ClassMetadata classMetadata,
			Set<MethodMetadata> set) {
		if ( classMetadata == null ) {
			return set;
		}
		for ( ClassMetadata ifc : classMetadata.getInterfaces( false ) ) {
			collectAbstractMethods( ifc , set );
		}
		collectAbstractMethods( classMetadata.getSuperClass( false ) , set );
		
		for ( MethodMetadata mm : classMetadata.getMethods() ) {
			if ( mm.getModifier().isAbstract() ) {
				set.add( mm );
			}
			else {
				set.remove( mm );
			}
		}
		return set;
	}

}
