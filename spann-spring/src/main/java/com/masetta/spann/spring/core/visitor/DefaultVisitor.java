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

package com.masetta.spann.spring.core.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.core.annotations.VisitFields;
import com.masetta.spann.spring.core.annotations.VisitMethods;
import com.masetta.spann.spring.core.annotations.Visitor;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;
import com.masetta.spann.spring.util.Handler;

/**
 * Default visitor. Delegates to visitors defined by the {@link Visitor} Meta-Annotation.
 * <p>
 * Method scan is triggered by the class-level {@link VisitMethods} annotation, which can be used both as
 * meta annotation or directly on the class who's methods should be searched for Visitor 
 * annotations.
 * <p>
 * The same applies to the class-level {@link VisitFields} annotation.
 * 
 * @author Ron Piterman    
 */
public class DefaultVisitor implements MetadataVisitor<ClassMetadata> {
    
	private static final String VISIT = Visit.class.getCanonicalName();

	private static final String VISITOR = Visitor.class.getCanonicalName();
    
    private static final String VISIT_METHODS = VisitMethods.class.getCanonicalName();
    
    private static final String VISIT_FIELDS = VisitFields.class.getCanonicalName();
    
    /** 
     * Compares AnnotationPaths whos target annotation is Visitor, according to the Visitor annotation's 
     * precedence attribute.
     */
    private static final Comparator<AnnotationPath> ANNOTATION_PATH_COMPARATOR_BY_PRECEDENCE = new Comparator<AnnotationPath>() {

        public int compare(AnnotationPath o1, AnnotationPath o2) {
            return precedence( o1 ).compareTo( precedence( o2 ) );
        }

        public Integer precedence(AnnotationPath ap) {
            return ap.getAttribute(0 , Integer.class, Visitor.ORDER_ATTRIBUTE , true );
        }
        
    };

    private final SpannLog log = SpannLogFactory.getLog( DefaultVisitor.class );
    
    /** Map of delegates by classname. */
    private Map<String,MetadataVisitor> delegates = new HashMap<String, MetadataVisitor>();
    
    /** Map of callbacks by classname. */
    private Map<String,VisitCallback> callbacks = new HashMap<String, VisitCallback>();
    
    
    /**
     * @see com.masetta.spann.spring.MetadataVisitor#visit(com.masetta.spann.spring.core.asm.metadata.ExtendedClassMetadata, com.masetta.spann.spring.ScanContext)
     */
    public void visit( ClassMetadata metadata, ScanContext context) {
    	
    	switch ( metadata.getModifier().getClassType() ) {
    		case ANNOTATION:
    		case ENUM:
    			return;
    	}
    	
    	Set<Metadata> visited = new HashSet<Metadata>();
    	
    	visit( metadata , context , visited );
    }
    
    private void visit( AnnotatedElementMetadata metadata , final ScanContext context , 
    		Set<Metadata> visited ) {
    	
    	if ( ! visited.add( metadata ) )
    		return;
    	
    	visitAnnotatedElement( metadata, context );
    	
    	visitCallbacks( metadata , context , visited );
    	
    }
    
    private void visitCallbacks( AnnotatedElementMetadata metadata , final ScanContext context , 
    		final Set<Metadata> visited ) {
    	
    	List<AnnotationPath> callbacks = metadata.findAnnotationPaths( VISIT );
    	
    	Handler<AnnotatedElementMetadata> handler = new Handler<AnnotatedElementMetadata>() {
			public void handle(AnnotatedElementMetadata t) {
				visit( t, context , visited );
			}
    	};
    	
    	for (AnnotationPath callbackPath : callbacks ) {
    		VisitCallback callback = getInstanceOfValueAttribute(callbackPath, VisitCallback.class, this.callbacks );
    		callback.perform( handler, metadata );
    	}
	}
    	
	@SuppressWarnings("unchecked")
	private <T extends AnnotatedElementMetadata> void visitAnnotatedElement( T metadata, ScanContext context) {
		List<AnnotationPath> paths = getPathsByPrecedence( metadata );
        if ( paths == null )
            return;
        Set<MetadataVisitor<T>> visitors = new LinkedHashSet<MetadataVisitor<T>>();
        for ( AnnotationPath p : paths ) {
            final MetadataVisitor<T> visitor = (MetadataVisitor<T>) getInstanceOfValueAttribute( p , MetadataVisitor.class , this.delegates );
			if ( visitor.supports( metadata ) ) {
				visitors.add( visitor );
			}
        }
        for ( MetadataVisitor<T> visitor : visitors ) {
        	try {
				visitor.visit( metadata , context );
			}
			catch ( Exception ex ) {
				throw new IllegalConfigurationException( "Exception in visitor " + visitor + " while processing " + metadata , ex );
			}
        }
	}

	private <T> T getInstanceOfValueAttribute( AnnotationPath p, Class<T> type,
			Map<String, T> cache) {
		ClassMetadata delegate = p.getAttribute( 0 , ClassMetadata.class , "value" , false );
        final String canonicalName = delegate.getName();
		if ( cache.containsKey( canonicalName ) )
            return cache.get( canonicalName );
		T v = create( delegate , type );
        cache.put( canonicalName , v );
        return v;
	}

    @SuppressWarnings("unchecked")
	private <T> T create( ClassMetadata visitor , Class<T> ifc ) {
        T cmv = ClassMetadataSupport.newInstance( ifc, visitor );
        cmv = (T)SpannLogFactory.createTraceProxy( cmv, log, ifc );
        return cmv;
    }
    
    private List<AnnotationPath> getPathsByPrecedence( AnnotatedElementMetadata metadata ) {
        List<AnnotationPath> paths = metadata.findAnnotationPaths( VISITOR );
        if ( paths.size() == 0 ) {
            return null;
        }
        paths = sort( paths );
        return paths;
    }

    private List<AnnotationPath> sort(List<AnnotationPath> paths) {
    	List<AnnotationPath> sorted = new ArrayList<AnnotationPath>( paths );
        Collections.sort( sorted , ANNOTATION_PATH_COMPARATOR_BY_PRECEDENCE );
        return sorted;
    }


	public <M extends Metadata> boolean supports(M metadata) {
		return true;
	}

}
