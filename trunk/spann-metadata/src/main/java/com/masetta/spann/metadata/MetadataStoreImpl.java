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

package com.masetta.spann.metadata;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.masetta.spann.metadata.common.Resource;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.metadata.core.support.PrimitiveClassMetadata;

/**
 * Default implementation of MetadataSource.
 * 
 * @author Ron Piterman
 */
public class MetadataStoreImpl implements MetadataStore {
	
	private final static ClassMetadata[] PRIMITIVE_METADATA = {
		new PrimitiveClassMetadata( Boolean.TYPE ),
		new PrimitiveClassMetadata( Long.TYPE ),
		new PrimitiveClassMetadata( Integer.TYPE ),
		new PrimitiveClassMetadata( Short.TYPE ),
		new PrimitiveClassMetadata( Byte.TYPE ),
		new PrimitiveClassMetadata( Character.TYPE ),
		new PrimitiveClassMetadata(	Float.TYPE ),
		new PrimitiveClassMetadata( Double.TYPE ) };
	
	private final Map<String,ClassMetadata> classMetadataByClassName =
		new HashMap<String,ClassMetadata>();
	
	private final Map<String,ClassMetadata> classMetadataByUrl =
		new HashMap<String,ClassMetadata>();
	
	public MetadataStoreImpl() {
		for ( ClassMetadata cm : PRIMITIVE_METADATA ) {
			classMetadataByClassName.put( cm.getName(), cm );
		}
	}

	public void store( Resource resource , ClassMetadata clsMetadata ) {
		final String classname = ClassMetadataSupport.getNiceClassnameFor( clsMetadata.getName(), clsMetadata.getDimensions() );
		if ( classMetadataByClassName.containsKey( classname ) ) {
			if ( classMetadataByClassName.get( classname ) != clsMetadata ) {
				throw new IllegalArgumentException("ClassMetadata of " + classname + " already in store." );
			}
		} else {
			this.classMetadataByClassName.put( classname, clsMetadata );
		}
		if ( resource != null ) {
			final URL url = resource.toUrl();
			this.classMetadataByUrl.put( url == null ? clsMetadata.getName() + ".class" : 
				url.toExternalForm(), clsMetadata );
		}
	}
	
	public ClassMetadata getByClassname( String classname, int dimensions ) {
		return classMetadataByClassName.get( ClassMetadataSupport.getNiceClassnameFor( classname , dimensions ) );
	}
	
	public ClassMetadata getByResource( Resource r ) {
		return classMetadataByUrl.get( r.toUrl().toExternalForm() );
	}

}
