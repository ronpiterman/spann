
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
 *
 * @author rpt
 * @version $Id: $
 */

package com.masetta.spann.metadata.visitors;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.masetta.spann.metadata.MetadataStore;
import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.common.ClasspathResource;
import com.masetta.spann.metadata.common.Resource;
import com.masetta.spann.metadata.common.ResourceUtil;
import com.masetta.spann.metadata.common.RuntimeIOException;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.reader.ClassMetadataResolver;
import com.masetta.spann.metadata.reader.ClassReaderAdapter;
import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactory;
import com.masetta.spann.metadata.rules.MetadataPathRules;
import com.masetta.spann.metadata.util.Provider;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
public class VisitorControllerImpl implements VisitorController , Provider<ClassLoader> {
    
    private SpannLog log = SpannLogFactory.getLog( VisitorControllerImpl.class );
    
    private static final String SPACES = "                       ";

    private final Stack<Metadata> metadataStack = new Stack<Metadata>();
    
    private final Stack<Integer> metadataGranularity = new Stack<Integer>();
    
    private final MetadataStore metadataStore;
    
    private final ClassReaderAdapter classReaderAdapter;
    
    private final LazyLoadingRulesFactory lazyLoadingRulesFactory;

    private final Map<Artifact,Object> visitors;
    
    private final Map<Artifact,VisitorAdapter<?>> emptyVisitors;
    
    private final VisitorAdapter<SignatureVisitorImpl> signatureVisitor;
    
    private final ClassMetadataResolver classMetadataResolver;

    private final VisitorDelegate delegate;
    
    private final VisitorAdapter<ClassVisitorImpl> classVisitorAdapter;

    private MetadataPathRules rules;
    
    private Resource resource;
    
    /**
     * <p>Constructor for VisitorControllerImpl.</p>
     *
     * @param store a {@link com.masetta.spann.metadata.MetadataStore} object.
     * @param rules a {@link com.masetta.spann.metadata.rules.MetadataPathRules} object.
     * @param lazyLoadingRulesFactory a {@link com.masetta.spann.metadata.rules.LazyLoadingRulesFactory} object.
     * @param adapter a {@link com.masetta.spann.metadata.reader.ClassReaderAdapter} object.
     */
    public VisitorControllerImpl( MetadataStore store, MetadataPathRules rules , 
            LazyLoadingRulesFactory lazyLoadingRulesFactory , ClassReaderAdapter adapter ) {
        this.metadataStore = store;
        this.rules = rules;
        this.lazyLoadingRulesFactory = lazyLoadingRulesFactory;
        metadataGranularity.push( ArtifactElement.SIGNATURE.getBit() );
        this.classMetadataResolver = new ClassMetadataResolver() {
            public ClassMetadata resolve(String className, int dimensions) {
                return getClassMetadata(className, dimensions);
            }
        };
        this.classReaderAdapter = adapter;
        this.delegate = new VisitorDelegate( this.classReaderAdapter , this.classMetadataResolver );
        this.emptyVisitors = createEmptyVisitors( this.classReaderAdapter );
        this.visitors = createVisitors( this.classReaderAdapter );
        
        this.signatureVisitor = classReaderAdapter.createVisitorAdapter( new SignatureVisitorImpl( this ) );
        this.signatureVisitor.getDelegate().init( this.signatureVisitor );
        this.classVisitorAdapter = getVisitor( Artifact.CLASS , ClassVisitorImpl.class );
    }
    
    /** {@inheritDoc} */
    public boolean isVisit(ArtifactElement e) {
        boolean b = (this.metadataGranularity.peek().intValue() & e.getBit()) != 0 ;
        if ( log.isTraceEnabled() ) {
            String el = metadataStack.isEmpty() ? e.name() : e.name() + " of " + metadataStack.peek();
            log.trace( stackLevelIndent() + ( b ? "Visit " : "Skip " ) + el );
        }
        return b;
    }
    
    /** {@inheritDoc} */
    public VisitorAdapter<?> getEmptyVisitorAdapter( Artifact artifact ) {
    	return (VisitorAdapter<?>) this.emptyVisitors.get( artifact );
    }
    
    /** {@inheritDoc} */
    public <T> VisitorAdapter<T> visit( Class<T> visitorType, Metadata metadata, ArtifactElement element ) {
        int granularity = rules.getElements( metadata.getPath() );
        if ( ! metadataStack.isEmpty() ) {
            ((AbstractMetadataImpl)metadataStack.peek()).loaded( element );
        }
        metadataGranularity.push( granularity );
        metadataStack.push( metadata );
        return getVisitor(metadata.getArtifact(),visitorType);
    }

    private String stackLevelIndent() {
        return SPACES.substring( 0 , metadataStack.size() * 2 );
    }

    private void visitSignature(Metadata md) {
        if ( log.isTraceEnabled() ) {
            log.trace( stackLevelIndent() + "Visiting signature of " + md );
        }
        ((AbstractMetadataImpl)md).loaded( ArtifactElement.SIGNATURE );
        final String signature = ((SignatureOwner)md).getSignature();
        if ( signature != null ) {
            signatureVisitor.getDelegate().setTarget( md );
            this.classReaderAdapter.readSignature( signature , signatureVisitor );
            signatureVisitor.getDelegate().reset();
        }
    }

    /** {@inheritDoc} */
    public void lazyload( ClassLoader classLoader, String className,
            ArtifactPath path, ArtifactElement element) {
        if ( log.isTraceEnabled() ) {
            log.trace( "Lazy loading " + element + " of " + path );
        }
        if ( ArtifactElement.SIGNATURE.equals( element ) ) {
            ArtifactPath pathToSig = path;
            while ( ! (pathToSig.getMetadata() instanceof SignatureOwner)) {
                pathToSig = pathToSig.getParent();
            }
            visitSignature( pathToSig.getMetadata() );
            return;
        }
        
        if ( this.lazyLoadingRulesFactory == null )
            return;
        // store current rules temporarily and load lazy loading rules.
        MetadataPathRules temp = this.rules;
        this.rules = this.lazyLoadingRulesFactory.createRules(path, element);
        visit( ClasspathResource.forClassname( className , classLoader ) );
        // restore "normal scan" rules.
        this.rules = temp;
    }

    /**
     * <p>visit</p>
     *
     * @param resource a {@link com.masetta.spann.metadata.common.Resource} object.
     */
    public void visit( Resource resource ) {
    	this.resource = resource;
    	try {
    		URL url = resource.toUrl();
			final InputStream inputStream = url == null ? null : url.openStream();
	        if ( inputStream == null )
	        	throw new IllegalArgumentException("Resource not found: " + resource );
	        
	        try {
	            this.delegate.readClass( inputStream, classVisitorAdapter );
	        } catch (IOException e) {
	            throw new RuntimeException( e );
	        } finally {
	            try {
	                inputStream.close();
	            } catch (IOException e) {
	            }
	        }
    	} catch (IOException e) {
			throw new RuntimeIOException( e );
		}
    	finally {
    		this.resource = null;
    	}
    }

    private Map<Artifact, Object> createVisitors( ClassReaderAdapter classReaderAdapter ) {
        Map<Artifact,Object> map = new HashMap<Artifact, Object>();
        //map.put( Artifact.PACKAGE, new PackageVisitorImpl( this , this.metadataStore ) );
        map.put( Artifact.CLASS, classReaderAdapter.createVisitorAdapter( new ClassVisitorImpl( this ) ) );
        map.put( Artifact.FIELD, classReaderAdapter.createVisitorAdapter( new FieldVisitorImpl( this ) ) );
        VisitorAdapter<AnnotationVisitorImpl> annVisitorAdapter = classReaderAdapter.createVisitorAdapter( 
                new AnnotationVisitorImpl( this ) );
        annVisitorAdapter.getDelegate().init( annVisitorAdapter );
        map.put( Artifact.METHOD, classReaderAdapter.createVisitorAdapter( new MethodVisitorImpl( this , annVisitorAdapter ) ) );
        map.put( Artifact.ANNOTATION, annVisitorAdapter );
        return Collections.unmodifiableMap( map );
    }
    
    private Map<Artifact, VisitorAdapter<?>> createEmptyVisitors( ClassReaderAdapter classReaderAdapter ) {
    	Map<Artifact,VisitorAdapter<?>> map = new HashMap<Artifact, VisitorAdapter<?>>();
    	for ( Artifact a : Artifact.values() ) {
    		map.put( a , classReaderAdapter.createEmptyVisitor( a ) );
    	}
    	return map;
    }

    @SuppressWarnings("unchecked")
    private <T> VisitorAdapter<T> getVisitor( Artifact artifact , Class<T> visitorType ) {
        return (VisitorAdapter<T>) visitors.get( artifact );
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <T> T getCurrentMetadata(Class<T> type) {
        return (T) metadataStack.peek();
    }

    /**
     * <p>visitEnd</p>
     */
    public void visitEnd() {
        Object metadata = this.metadataStack.pop();
        this.metadataGranularity.pop();
        if ( this.metadataStack.isEmpty() && this.resource != null ) {
        	this.metadataStore.store( this.resource, (ClassMetadata)metadata );
        }
    }
    
    /**
     * <p>getFactories</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.ClassReaderAdapter} object.
     */
    public ClassReaderAdapter getFactories() {
        return this.classReaderAdapter;
    }
    
    /** {@inheritDoc} */
    public ClassMetadata getClassMetadata( String className , int dimensions ) {
        return getClassMetadata( className , dimensions , this );
    }
    
    /** {@inheritDoc} */
    public ClassMetadata getClassMetadata( String className , int dimensions , Provider<ClassLoader> classLoaderProvider ) {
        if ( className == null || "void".equals(className))
            return null;
        
        String actual = className; 
        if ( actual.contains("/"))
            actual = ResourceUtil.convertResourcePathToClassName( actual );
        
        ClassMetadata cm = metadataStore.getByClassname( actual , dimensions );
        if ( cm != null ) {
            return cm;
        }

        // if is array, search the comoponent class
        if ( dimensions > 0 )
            cm = metadataStore.getByClassname( actual , 0 );
        
        // create component class.
        if ( cm == null ) {
            final ClassLoader classloader = classLoaderProvider.get();
			cm = createClassMetadata( actual , classloader );
            if ( cm != null ) {
				Resource res = ClasspathResource.forClassname( actual , classloader );
				metadataStore.store( res, cm );
			}
        }
        
        // if is array, create array metadata and add to cache.
        if ( dimensions > 0 ) {
            cm = new ArrayMetadataImpl( cm , dimensions );
            metadataStore.store( null , cm );
        }

        return cm;
    }
    
    private ClassMetadata createClassMetadata( String className, ClassLoader classLoader ) {
        if ( classLoader == null )
            return null;
        return new ClassMetadataImpl( this, className , classLoader );
    }

    /**
     * <p>Getter for the field <code>delegate</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.visitors.VisitorDelegate} object.
     */
    public VisitorDelegate getDelegate() {
        return delegate;
    }

    /**
     * Implementation of Provider&lt;ClassLoader>.
     *
     * @return a {@link java.lang.ClassLoader} object.
     */
    public ClassLoader get() {
        if ( this.metadataStack.isEmpty() )
            return this.resource.getClassLoader();
        return ((ClassMetadata)this.metadataStack.get( 0 )).getClassLoader();
    }

}
