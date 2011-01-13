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

import java.util.ArrayList;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;

/**
 * Useful constants for lazy loading rules.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public final class Rules {
    
    private static final ArtifactElement[] EMPTY_AE = new ArtifactElement[0];

    /**
     * Lazy-loading rules: Lazy. Only what is needed is loaded.
     */
    public static final LazyLoadingRulesFactory LAZY_LAZY = new ImmutableFactory( new LazyLoadingRulesFactoryImpl() );
    
    /**
     * Layz-loading rules: Eager. When an artifact element is needed, everything loads.
     */
    public static final LazyLoadingRulesFactory LAZY_EAGER_ALL = createEager( true , true , true );
    
    /**
     * Layz-loading rules: Deep. when an artifact element is needed, it and all its sub artifacts (
     * e.g. annotations, signature ) are loaded.
     */
    public static final LazyLoadingRulesFactory LAZY_EAGER_DEEP = createEager( true , false , false );
    
    /**
     * Lazy-loading rules: Flat eager. When an artifact is needed, all its siblings are loaded, including
     * other artifact elements (e.g. when a method is loaded, all other methods, fields, 
     * and class-annotations are loaded ).
     */
    public static final LazyLoadingRulesFactory LAZY_EAGER_FLAT = createEager( false , true , true );
    
    /**
     * Lazy-loading rules: Flat eager same type. Lazy loading rules with boundaries to same artifact type:
     *  when an artifact is loaded,
     * all its siblings of the same artifact type are loaded. E.g. when a method is loaded
     * all other methods are loaded, but fields and class-annotations are ignored.
     */
    public static final LazyLoadingRulesFactory LAZY_EAGER_FLAT_SINGLE_ARTIFACT = createEager( false , false , true );
    
    /**
     * Lazy-loading rules: Deep eager loading rules with siblings boundaries to same artifact type. When an element is loaded,
     * all its siblings of the same artifact type are loaded and all its sub elements. E.g. when a method is loaded
     * all other methods are loaded, but fields and class-annotations are ignored. Also,
     * method annotations and parameter annotations are also loaded.
     */
    public static final LazyLoadingRulesFactory LAZY_EAGER_DEEP_SINGLE_ARTIFACT = createEager( true , false , true );
    
    /** Loading rules: Lazy. Only class is loaded. */
    public static final MetadataPathRules RULES_LAZY = creareRules( Artifact.CLASS , ArtifactElement.SELF );
    
    /** Loading rules: Quite lazy. Only class and its annotations are loaded. */
    public static final MetadataPathRules RULES_CLASS_ANNOTATIONS = creareRules( Artifact.CLASS , ArtifactElement.ANNOTATIONS );

    /** Loading rules: Quite eager. All elements of a class are loaded (annotations, fields, methods) */
    public static final MetadataPathRules RULES_CLASS_ALL = creareRules( Artifact.CLASS , ArtifactElement.ANNOTATIONS , ArtifactElement.FIELDS, ArtifactElement.METHODS );
    
    /** Loading rules: Eager. All elements are loaded: class, fields, methods , parameters and all their annotations */
    public static final MetadataPathRules RULES_EAGER = creareRules(
    		Artifact.CLASS , ArtifactElement.ANNOTATIONS , 
    		Artifact.FIELD, ArtifactElement.ANNOTATIONS ,
    		Artifact.METHOD , ArtifactElement.ANNOTATIONS , ArtifactElement.PARAMETER_ANNOTATIONS );

    private static LazyLoadingRulesFactory createEager( boolean children , boolean artifactSiblings , boolean siblings ) {
        final LazyLoadingRulesFactoryImpl factory = new LazyLoadingRulesFactoryImpl();
        for ( Artifact a : Artifact.values() ) {
            final ArtifactElement[] array = new ArrayList<ArtifactElement>(a.getSupports()).toArray( EMPTY_AE);
            if ( children ) {
                factory.lazyLoadChildren( a, array );
            }
            if ( artifactSiblings || siblings ) {
                for ( ArtifactElement e : a.getSupports() ) {
                    if ( siblings ) 
                        factory.setLazySiblingsLoadRule(a, e, false );
                    if ( artifactSiblings )
                        factory.lazyLoadSiblingArtifacts( a, e, array );
                }
            }
        }
        return new ImmutableFactory( factory );
    }

    private static MetadataPathRules creareRules(Object ...obj) {
		MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
		for ( Object o : obj ) {
			if ( o instanceof Artifact ) {
				builder.add( (Artifact) o, null );
			}
			else if ( o instanceof ArtifactElement ) {
				builder.load( (ArtifactElement)o );
			}
		}
		return builder.build();
	}

	private static class ImmutableFactory implements LazyLoadingRulesFactory {
        private LazyLoadingRulesFactory delegate;

        public ImmutableFactory(LazyLoadingRulesFactory delegate) {
            super();
            this.delegate = delegate;
        }

        public MetadataPathRules createRules(ArtifactPath path,
                ArtifactElement element) {
            return delegate.createRules(path, element);
        }
    }
    
    private Rules() {}
}
