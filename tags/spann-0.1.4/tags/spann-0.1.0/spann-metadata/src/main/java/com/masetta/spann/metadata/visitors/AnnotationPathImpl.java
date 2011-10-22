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

package com.masetta.spann.metadata.visitors;

import java.util.Arrays;

import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;

/**
 * An immutable implementation of AnnotationPath.
 * 
 * @author Ron Piterman    
 */
public final class AnnotationPathImpl implements AnnotationPath {
	
	private static final AnnotationMetadata[] EMPTY_ANNOTATED_ELEMENT_METADATA_ARRAY = {};
    
    static final AnnotationPathImpl EMPTY_ANNOTATION_PATH = new AnnotationPathImpl();
    
    private final AnnotationMetadata[] path;
    
    private AnnotationPathImpl() {
    	this.path = EMPTY_ANNOTATED_ELEMENT_METADATA_ARRAY;
    }
    
    private AnnotationPathImpl( AnnotationPath parent , AnnotationMetadata am ) {
    	this.path = Arrays.copyOf( parent.getPath(), parent.getPath().length + 1 );
    	this.path[ this.path.length - 1 ] = am;
    }
    
    AnnotationPath append( AnnotationMetadata am ) {
    	return new AnnotationPathImpl( this , am );
    }

	public Object getAttribute(int index, String name, boolean nullsafe) {
		return path[index].getAttribute( name, nullsafe);
	}

	public <T> T getAttribute(int index, Class<T> type, String name, boolean nullsafe) {
		return path[index].getAttribute(type , name, nullsafe);
	}

	public AnnotationMetadata[] getPath() {
		return path;
	}
    
}
