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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.support.AnnotationPathByLengthComparator;
import com.masetta.spann.metadata.util.Pair;

class AnnotatedElementMetadataImpl extends AbstractMetadataImpl implements AnnotatedElementMetadata {
    
    private static final Pair<List<AnnotationPath>, Boolean> EMPTY_PAIR =  
    	new Pair<List<AnnotationPath>, Boolean>( 
    			Collections.unmodifiableList( new ArrayList<AnnotationPath>()) , false );
    
	private Map<String,AnnotationMetadata> annotations = new HashMap<String, AnnotationMetadata>();
	
	private Map<String,List<AnnotationPath>> paths;
    
    AnnotatedElementMetadataImpl(ArtifactPath parent, Artifact type,
            String name ) {
        super(parent, type, name );
    }
    
    AnnotationMetadata getOrCreateAnnotation( AbstractMetadataImpl parent, ClassMetadata cls ) {
        AnnotationMetadata am = this.annotations.get( cls.getName() );
        if ( am == null ) {
            am = new AnnotationMetadataImpl( parent, cls );
            this.annotations.put( cls.getName() , am );
        }
        return am;
    }

    public AnnotationMetadata getAnnotation(String cannonicalClassName) {
        loadAnnotations();
        return annotations.get( cannonicalClassName );
    }

    protected void loadAnnotations() {
        load( ArtifactElement.ANNOTATIONS );
    }

	public List<AnnotationPath> findAnnotationPaths(String annotationCannonicalClassName ) {
		List<AnnotationPath> path = findAnnotationPathInternal( annotationCannonicalClassName );
		if ( path == null ) {
			return Collections.emptyList();
		}
		return path;
	}

	private List<AnnotationPath> findAnnotationPathInternal( String pathTargetAnnotation ) {
		if ( paths != null ) {
			List<AnnotationPath>  cached = paths.get( pathTargetAnnotation );
			if ( cached != null )
				return cached;
		}
		
		Pair<List<AnnotationPath>,Boolean> pathAndStore =
			findAnnotationPathInternal( new HashSet<String>() , pathTargetAnnotation );
			
		final List<AnnotationPath> path = pathAndStore.getLeft();
		if ( pathAndStore.getRight() ) {
			if ( paths == null ) {
				paths = new HashMap<String, List<AnnotationPath>>();
			}
			Collections.sort( path, AnnotationPathByLengthComparator.INSTANCE );
			paths.put( pathTargetAnnotation, path );
		}
		return path;
	}
	
	private Pair<List<AnnotationPath>,Boolean> findAnnotationPathInternal( Set<String> visited , 
			String pathTargetAnnotation ) {
		
		loadAnnotations();
		if ( annotations.isEmpty() )
			return EMPTY_PAIR;
		
		List<AnnotationPath> all = new ArrayList<AnnotationPath>();
		boolean store = true;
		for (AnnotationMetadata ann : annotations.values() ) {
			final String annFqName = ann.getName();
			
			if ( annFqName.equals( pathTargetAnnotation ) ) {
				all.add( AnnotationPathImpl.EMPTY_ANNOTATION_PATH.append( ann ) );
				continue;
			}
			
			if ( ! visited.add( annFqName ) ) {
				if ( ! annFqName.startsWith("java.lang") ) {
					store = false;
				}
				continue;
			}
			
			Pair<List<AnnotationPath>,Boolean> childPaths = ((AnnotatedElementMetadataImpl)ann.getType())
				.findAnnotationPathInternal(visited, pathTargetAnnotation);
			
			store = store & childPaths.getRight();
			
			for ( AnnotationPath childPath : childPaths.getLeft() ) {
				all.add( ((AnnotationPathImpl)childPath).append( ann ) );
			}
			
			visited.remove( annFqName );
		}
		return new Pair<List<AnnotationPath>, Boolean>(all,store);
	}

}
