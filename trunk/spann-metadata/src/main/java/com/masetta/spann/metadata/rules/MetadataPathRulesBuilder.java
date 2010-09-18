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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.util.Matcher;
import com.masetta.spann.metadata.util.Pair;

/**
 * Builder for {@link MetadataPathRules}.
 * <p>
 * The Builder provides an API to construct rules which define which elements
 * should load for which artifacts.
 * <p>
 * It uses only 'include' semantics, thus one can only define which elements should be included
 * under which conditions, but one <b> can not </b> define overriding <i>exclude</i> rules.
 * <p>
 * <h2>How to use the builder</h2>
 * <p>
 * To add rules, multiple series of one or more calls to {@link #add(Artifact, String, Matcher...) add} 
 * should be called which together define a matchable "path"; 
 * each series ending with a single call to {@link #load(ArtifactElement...)}.
 * 
 * <p>
 * Every call to {@link #add(Artifact, String, Matcher...)} must be made after the <i>parent</i>
 * of the given Artifact is already in the path. So, the first call (after creating the builder)
 * must define a class rule (<code>add( Artifact.CLASS, ...) </code>)
 * and be followed by a call to <code>add( Artifact.CLASS, ...)</code> etc.
 * 
 * <p>
 * The {@link #add(Artifact, String, Matcher...) add(...)} calls define a match pattern for an <i>artifact path</i>,
 * the {@link #load(ArtifactElement...)} call defines which elements should be loaded for any path which
 * matches.
 * 
 * <p>
 * After calling {@link #load(ArtifactElement...)}, a new series of 
 * {@link #add(Artifact, String, Matcher...) add(...)} calls can be made, 
 * again ending with a call to {@link #load(ArtifactElement...) load(...)} etc.
 * <p>
 * The new series does not start with <code>Artifact.PACKAGE</code>. The builder
 * will trim the to the parent of the given Artifact and then add the given rules.
 * <p>
 * <h3>For example:</h3>
 * <code><pre>
 * MetadataPathRulesBuilder b = new MetadataPathRulesBuilder();
 * b.add( Artifact.PACKAGE , null )                  // all packages
 *      .add( Artifact.CLASS , null )                // ...all classes
 *      .load( ArtifactElement.ANNOTATIONS );        // ...load annotations
 * b.add( Artifact.CLASS , "com.my.pakage.Foo" )     // class Foo
 *      .add( Artifact.METHOD , null )               // ...all methods
 *      ,load( ArtifactElement.ANNOTATIONS ,         // ...load annotations
 *          ArtifactElement.PARAMETER_ANNOTATIONS ); // ...and parameter annotations.
 *         
 * </pre></code>
 * <p>
 * <h3>Implicit rules</h3>
 * <p>
 * In the example above, we did not explicitly set a rule to load any methods of class Foo.
 * However, as soon as we call add( Artifact.METHOD, ...), the builder adds
 * a load rule for ArtifactElement.METHODS in the current path, thus
 * loading all methods of class Foo in the example above.
 * <p> 
 * 
 * @author Ron Piterman    
 *
 */
public class MetadataPathRulesBuilder {
    
    private static final Map<Artifact, Artifact> PARENTS = createParents();
    
    private static final Map<Artifact,ArtifactElement[]> IMPLIES = createImplies();
    
    private final Stack<Pair<Artifact,List<Matcher<Metadata>>>> currentPath = new Stack<Pair<Artifact,List<Matcher<Metadata>>>>();
    
    private final List<MetadataPathRule> rules = new ArrayList<MetadataPathRule>();
    
    private static Map<Artifact, Artifact> createParents() {
        Map<Artifact, Artifact> map = new HashMap<Artifact, Artifact>();
        map.put( Artifact.CLASS , null );
        map.put( Artifact.FIELD , Artifact.CLASS );
        map.put( Artifact.METHOD , Artifact.CLASS );
        map.put( Artifact.PARAMETER, Artifact.METHOD );
        return map;
    }
    
    private static Map<Artifact, ArtifactElement[]> createImplies() {
        Map<Artifact, ArtifactElement[]> map = new HashMap<Artifact, ArtifactElement[]>();
        put( map, Artifact.METHOD, ArtifactElement.METHODS );
        put( map, Artifact.FIELD, ArtifactElement.FIELDS );
        put( map, Artifact.ANNOTATION, ArtifactElement.ANNOTATIONS );
        return map;
    }
    
    private static void put( Map<Artifact,ArtifactElement[]> map , Artifact a , ArtifactElement ...ae ) {
        map.put( a , ae );
    }

    /**
     * Add matcher for the given artifact. The rule will apply for any artifact which
     * matches any of the given rules.
     * 
     * @param artifact the artifact to add matcher for. The artifact's parent must be in the path 
     *         (except for PACKAGE)
     * @param name shortcut for creating a name rule. If null will be ignored. 
     * @param matcher other matcher to apply.
     * @return this builder
     * @throws IllegalStateException if the parent of the given artifact is not in the path
     *         (for example, adding an ARGUMENT is only allowed if METHOD is in the path )
     */
    public MetadataPathRulesBuilder add( Artifact artifact , String name , Matcher<Metadata> ...matcher ) {
        if ( PARENTS.containsKey( artifact ) ) {
            fallbackToParent(artifact);
        }
        // only if artifact is ANNOTATION
        else {
            // double annotations definition need to apply to parent, and not to last added
            // annotation definition.
            // For example :
            // annotation Foo of class C should scan Attributes.
            // annotation Bar of class C should scan Attributes also.
            // Syntax is : 
            // builder...add( CLASS , "C" ).add( ANNOTATION , "Foo" ).scan( ATTRIBUTES );
            // builder.add( ANNOTATION , "Bar").scan( ATTRIBUTES );
            // in the second call to add(ANNOTATION...) the top element is Annotation itself.
            // still, we need to pop down to the class.
            if ( currentPath.peek().getLeft().equals( Artifact.ANNOTATION ) )
                currentPath.pop();
        }
        ArtifactElement[] implicit = IMPLIES.get( artifact );
        if ( implicit != null )
            load( implicit );
        
        List<Matcher<Metadata>> list = new ArrayList<Matcher<Metadata>>();
        if ( name != null ) {
            list.add( new MetadataNameMatcher( name ) );
        }
        if ( matcher.length > 0 ) {
            list.addAll( Arrays.asList( matcher ) );
        }
        currentPath.push( new Pair<Artifact, List<Matcher<Metadata>>>( artifact , list ) );
        return this;
    }

    private void fallbackToParent(Artifact artifact) {
        Artifact parent = PARENTS.get( artifact );
        if ( parent == null )
            currentPath.clear();
        else {
            while ( ! currentPath.peek().getLeft().equals( parent ) ) {
                currentPath.pop();
                if ( currentPath.isEmpty() ) {
                    throw new IllegalStateException( "Parent of " + artifact + " is " + parent + " but no instacne in current path." );
                }
            }
        }
    }
    
    public void load( ArtifactElement ...elements ) {
        if ( currentPath.isEmpty() )
            throw new IllegalStateException("Need to add at least a class rule before adding scan elements." );
        
        Artifact lastInPath = currentPath.peek().getLeft();
        for ( ArtifactElement element : elements ) {
            if ( ! lastInPath.isSupports( element ) )
                throw new IllegalArgumentException( lastInPath + " does not support element " + element );
        }
        Map<Artifact,List<Matcher<Metadata>>> pathAsMap = new HashMap<Artifact, List<Matcher<Metadata>>>();
        for ( Pair<Artifact,List<Matcher<Metadata>>> pathElement : currentPath ) {
            pathAsMap.put( pathElement.getLeft(), pathElement.getRight() );
        }
        rules.add( new MetadataPathRule( pathAsMap , Arrays.asList( elements ) ) );
    }
    
    /**
     * Builds rules for the added rules and resets this builder.
     */
    public MetadataPathRulesImpl build() {
        MetadataPathRulesImpl result = new MetadataPathRulesImpl( this.rules );
        this.rules.clear();
        this.currentPath.clear();
        return result;
    }

}
