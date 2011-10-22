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

package com.masetta.spann.metadata.rules;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;


/**
 * Implementation of LazyLoadingRulesFactory. Provides an API to define rules for lazy loading.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public class LazyLoadingRulesFactoryImpl implements LazyLoadingRulesFactory {
    
    private static final ArtifactElement[] EMPTY_ARTIFACT_ELEMENT_ARRAY = {};
    
    private final Map<String,Collection<ArtifactElement>> lazyLoadSiblings =
        new HashMap<String,Collection<ArtifactElement>>();
    
    private final Map<Artifact,Collection<ArtifactElement>> lazyLoadChildren =
        new HashMap<Artifact, Collection<ArtifactElement>>();
    
    private final Map<String,Boolean> strictNamingPolicy = new HashMap<String, Boolean>();
    
    
    /* (non-Javadoc)
     * @see org.masetta.spann.asm.rules.LazyLoadingRulesFactory#createRules(org.masetta.spann.asm.metadata.ArtifactPath, org.masetta.spann.asm.common.ArtifactElement)
     */
    /** {@inheritDoc} */
    public MetadataPathRules createRules(ArtifactPath path,
            ArtifactElement element) {
        Artifact lastInPath = path.getMetadata().getArtifact();
        if (  ! lastInPath.isSupports( element ) ) {
            throw new IllegalArgumentException( "Element " + element + " is not a child of " + 
                    lastInPath );
        }
        
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        Metadata metadata = addPath( builder , path );
        add( builder , metadata.getArtifact() , element );
        
        return builder.build();
    }

    private Metadata addPath(MetadataPathRulesBuilder builder,
            ArtifactPath path) {
        if ( path == null )
            return null;
        addPath( builder , path.getParent() );
//        if ( path.size() > 1 ) {
//            ArtifactElement e = getArtifactElement( path.getMetadata().getArtifact() );
//            if ( e != null )
//                builder.scan( e );
//        }
        Metadata m = path.getMetadata();
        if ( m != null ) {
            builder.add( m.getArtifact() , isStrict( path ) ? m.getName() : null );
        }
        return m;
    }

    private boolean isStrict(ArtifactPath path) {
        if ( path.size() < 2 ) {
            return false;
        }
        String key = key( path.getParent().getMetadata().getArtifact() , 
            getArtifactElement( path.getMetadata().getArtifact() ) );
        Boolean strict = this.strictNamingPolicy.get( key );
        // default is true.
        return strict == null || strict.booleanValue();
    }

    private void add( MetadataPathRulesBuilder builder, Artifact artifact, ArtifactElement element) {
        Collection<ArtifactElement> siblings = getMutableSiblings( artifact , element );
        siblings.add( element );
        
        builder.load( siblings.toArray( EMPTY_ARTIFACT_ELEMENT_ARRAY ) );
        
        for ( ArtifactElement ae : siblings ) {
            Artifact a = getArtifact( ae );
            
            if ( a != null ) {
                Collection<ArtifactElement> children = lazyLoadChildren.get( a );
                
                if ( children == null || children.isEmpty() )
                    continue;
                    
                builder.add( a , null );
                for ( ArtifactElement child : children )
                add( builder , a , child );
            }
        }
    }

    private Collection<ArtifactElement> getMutableSiblings(Artifact artifact,
            ArtifactElement element) {
        Collection<ArtifactElement> siblings = lazyLoadSiblings.get( key (artifact,element ) );
        if ( siblings == null )
            return new HashSet<ArtifactElement>();
        else
            return new HashSet<ArtifactElement>( siblings );
        
    }

    private String key(Artifact artifact, ArtifactElement element) {
        return artifact.name() + '#' + element.name();
    }

    private Artifact getArtifact(ArtifactElement ae) {
        switch ( ae ) {
            case CLASSES : return Artifact.CLASS;
            case METHODS : return Artifact.METHOD;
            case FIELDS : return Artifact.FIELD;
        }
        return null;
    }
    
    private ArtifactElement getArtifactElement(Artifact artifact) {
        switch ( artifact ) {
            case ANNOTATION: return ArtifactElement.ANNOTATIONS;
            case CLASS: return ArtifactElement.CLASSES;
            case FIELD: return ArtifactElement.FIELDS;
            case METHOD: return ArtifactElement.METHODS;
            case PARAMETER: 
        }
        throw new IllegalArgumentException("Unknown artifact:" + artifact );
    }
    
    /**
     * Defines which sibling-elements should be lazy loaded when lazy-loading the given element
     * for the given artifact.
     * <p>
     * For example, the call
     * <code>lazyLoadSiblingArtifact( Artifact.CLASS, ArtifactElement.ANNOTATIONS,
     * 	ArtifactElement.METHODS ); </code> adds a rule to indicate that
     * when element ANNOTATIONS of artifact CLASS is lazy loaded, also elements METHODS should
     * be loaded.
     * <p>
     * Note that this rule is not mutual: taking the example above, the rule does not
     * indicate that when METHODS will lazy load, ANNOTATIONS will.
     *
     * @param artifact the artifact which is lazy loading.
     * @param element the element of the artifact which is lazy loaded
     * @param elements other elements of the artifact which are requested to lazy load.
     */
    public void lazyLoadSiblingArtifacts( Artifact artifact, ArtifactElement element, ArtifactElement ...elements ) {
        this.lazyLoadSiblings.put( key( artifact , element ), Arrays.asList( elements ) );
    }
    
    /**
     * Defines which child elements should be loaded when an artifact is lazy-loaded.
     * <p>
     * For example, when a <code>Artifact.METHOD</code>s are loaded,
     * also elements ANNOTATIONS and PARAMETER_ANNOTATIONS (of the methods!)
     * can be lazy loaded.
     * <p>
     *
     * @param artifact artifact type
     * @param elements elements to load when the given artifact type loads.
     */
    public void lazyLoadChildren( Artifact artifact , ArtifactElement ...elements ) {
        this.lazyLoadChildren.put( artifact , Arrays.asList( elements ) );
    }
    
    /**
     * Set if siblings of same Artifact type should also be loaded.
     * For example, when METHOD 'foo()' is lazy loading ANNOTATIONS, if <b>all other methods</b> in
     * the same class should also load annotations.
     * <p>
     * Note that this cascades also to the defined
     * {@link #lazyLoadChildren(Artifact, ArtifactElement...) lazyLoadChildren(...)} .
     * <br>
     * For example: if Artifact METHOD loads child-element PARAMETER_ANNOTATIONS when it loads child-element ANNOTATIONS,
     * and the strict policy is set to false, also all other methods in the same class will load
     * both PARAMETER_ANNOTATIONS and ANNOTATIONS.
     * <p>
     * The default policy is strict, which means (for example): when METHOD 'foo()' is loading ANNOTATIONS,
     * other methods will not.
     * <p>
     * Also note that strict policy is only applicable for Artifacts below the CLASS level.
     * Setting a non-strict policy to CLASS or PACKAGE artifacts will have no effect.
     * (lazy-loading methods of class Foo will never preload methods of other classes).
     *
     * @param artifact the artifact which lazy-loads an element
     * @param element the element which is lazy loading.
     * @param strict the strict policy to set for the given artifact and element.
     */
    public void setLazySiblingsLoadRule( Artifact artifact , ArtifactElement element , boolean strict ) {
        this.strictNamingPolicy.put( key( artifact, element ), strict );
    }

}
