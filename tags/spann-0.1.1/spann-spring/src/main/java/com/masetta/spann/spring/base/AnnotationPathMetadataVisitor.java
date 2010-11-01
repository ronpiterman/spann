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

package com.masetta.spann.spring.base;

import java.util.List;


import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * Base class for Visitors (both class and method visitors) 
 * which process a single annotation type.
 * 
 * @author Ron Piterman    
 */
public abstract class AnnotationPathMetadataVisitor<T extends AnnotatedElementMetadata> 
	implements MetadataVisitor<T> {
	
	private final String annotationType;
	
	private final boolean unique;
	
	private final boolean firstOnly;
	
	private Class<T> supported;
	
    /**
     * Construct a new visitor
     * 
     * @param annotationType The annotation type this visitor processes.
     * @param checkUnique If the visitor should check that only one annotation path
     *     to the given annotation type exists, and otherwise, throw a RuntimeException.
     */
    public AnnotationPathMetadataVisitor( Class<T> supported, String annotationType,
            boolean checkUnique) {
        this( supported, annotationType , checkUnique , false );
    }

    /**
     * Construct a new visitor
     * 
     * @param annotationType The annotation type this visitor processes.
     * @param checkUnique If the visitor should check that only one annotation path
     *     to the given annotation type exists, and otherwise, throw a RuntimeException.
     * @param firstOnly If the visitor should only call 
     * {@link #visit(AnnotatedElementMetadata, ScanContext, AnnotationPath) } for the first, shortest
     * path to the given annotation.
     */
    public AnnotationPathMetadataVisitor(Class<T> supported, 
    		String annotationType, boolean checkUnique, boolean firstOnly ) {
        this.annotationType = annotationType;
        this.unique = checkUnique;
        this.firstOnly = firstOnly;
        this.supported = supported;
    }
    
    public void visit( T metadata, ScanContext context) {
        List<AnnotationPath> paths = metadata.findAnnotationPaths( this.annotationType);
        if ( unique && paths.size() > 1 ) {
        	throw new IllegalConfigurationException( annotationType , metadata , 
        			"More than one path to annotation, unique path required.");
        }
        if ( paths.size() > 0 )
        	visit(metadata, context, paths);
    }

    /**
     * Visit all paths from the given metadata to this visitor's annotation.
     * <p>
     * This method iterates over the given paths and delegates to 
     * {@link #visit(AnnotatedElementMetadata, ScanContext, AnnotationPath)}.
     * <p>
     * If a subclasses should handle all paths together it can override this method.
     * <p>
     * If <code>checkUnique</code> is true (see constructor) , the <code>paths</code> argument 
     * will always contain exactly one member. 
     * If <code>checkUnique</code> is false, the <code>paths</code> argument may 
     * contain one or more <code>AnnotationPath</code>s, but is never empty.
     * 
     * @param metadata the visited metadata
     * @param context the scan context
     * @param paths all paths from the given metadata to this visitor's annotation.
     */
	protected void visit( T metadata, ScanContext context, List<AnnotationPath> paths ) {
		for ( AnnotationPath path : paths ) {
        	visit( metadata , context , path );
        	if ( firstOnly ) {
        		return;
        	}
        }
	}

    /**
     * Process the annotation. Normally, this method is called for each annotation path found
     * to the requested annotation.
     * <p>
     * This method is called from {@link #visit(AnnotatedElementMetadata, ScanContext, List)},
     * so subclasses can override it to handle all paths together.
     * <p>
     * 
     * @param metadata the metadata of the class annotated by the annotation.
     * @param context current scan context.
     * @param path AnnotationPaths from the given metadata to this visitor's annotation.
     */
    protected abstract void visit(T metadata, ScanContext context, AnnotationPath path );

	public String getAnnotationType() {
		return annotationType;
	}

	public <M extends Metadata> boolean supports(M metadata) {
		return supported.isAssignableFrom( metadata.getClass() );
	}

}
