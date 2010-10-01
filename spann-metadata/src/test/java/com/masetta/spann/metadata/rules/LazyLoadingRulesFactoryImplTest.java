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

import java.util.Set;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactoryImpl;
import com.masetta.spann.metadata.rules.MetadataPathRules;

public class LazyLoadingRulesFactoryImplTest {
    
    @Test
    public void testEmpty1() {
        // empty factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // path to class
        ArtifactPath path = createPath( getClass() );
        
        // signal: class is lazy loading methods
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.METHODS );
        checkRules( rules, path, ArtifactElement.METHODS );
        
        // load class -> someMethod() -> ?
        // check : method should not load anything.
        path = append( path , Artifact.METHOD , "someMethod" );
        checkRules( rules , path );
    }

    /** 
     * @param rules
     * @param path
     * @param elements
     */
    private void checkRules(MetadataPathRules rules, ArtifactPath path,
            final ArtifactElement ...elements) {
        int set = rules.getElements( path );
        Assert.assertEquals( set, ArtifactElement.collect( elements ) );
    }
    
    @Test
    public void testEmpty2() {
        // empty factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // path class -> foo()
        ArtifactPath path = createPath( getClass() , Artifact.METHOD , "foo" );
        
        // signal: method foo() is lazy loading annotations
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.ANNOTATIONS );
        checkRules( rules , path , ArtifactElement.ANNOTATIONS );
        // what to load in class -> ?
        checkRules( rules , path.getParent() , ArtifactElement.METHODS );
        
        // check : fields of parent should not load anything.
        rules = impl.createRules( append( path.getParent(), Artifact.METHOD , "bar" ) , ArtifactElement.ANNOTATIONS );
        checkRules( rules , path.getParent() , ArtifactElement.METHODS );
    }
    
    @Test
    public void testSiblingArtifacts() {
        // create factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // when loading methods of a class, also fields and annotations (of the class ) should load.
        impl.lazyLoadSiblingArtifacts( Artifact.CLASS, ArtifactElement.METHODS, ArtifactElement.FIELDS , ArtifactElement.ANNOTATIONS );
        // path to class
        ArtifactPath path = createPath( getClass() );
        
        // signal: class is loading methods
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.METHODS );
        // rule: class should load methods fields and annotations.
        checkRules( rules, path, ArtifactElement.METHODS , ArtifactElement.FIELDS , ArtifactElement.ANNOTATIONS );
        // method foo should not load anything
        checkRules( rules, append( path , Artifact.METHOD , "foo" ) );
        // field foo should not load anything
        checkRules( rules, append( path , Artifact.FIELD , "foo" ) );
        
        
        // BUT, if a field is loading, we do not load methods...
        
        // singal: class is lazy loading fields 
        rules = impl.createRules( path, ArtifactElement.FIELDS);
        // loading method should load methods fields and annotations.
        checkRules( rules, path, ArtifactElement.FIELDS );
        // method foo should not load anything
        checkRules( rules, append( path , Artifact.METHOD , "foo" ) );
        // field foo should not load anything
        checkRules( rules, append( path , Artifact.FIELD , "foo" ) );
        
        // AND same with annotations: loading annotations should NOT load fields and methods.
        // signal: class is loading annotations
        rules = impl.createRules( path, ArtifactElement.ANNOTATIONS);
        // loading method should load methods fields and annotations.
        checkRules( rules, path, ArtifactElement.ANNOTATIONS );
        // method foo should not load anything
        checkRules( rules, append( path , Artifact.METHOD , "foo" ) );
        // field foo should not load anything
        checkRules( rules, append( path , Artifact.FIELD , "foo" ) );
    }
    
    @Test
    public void testSiblingArtifacts2() {
        // create factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // when loading signature of a method, also annotations (of the method) should load, but not the other way around.
        impl.lazyLoadSiblingArtifacts( Artifact.METHOD, ArtifactElement.SIGNATURE, ArtifactElement.ANNOTATIONS );
        // path to method
        ArtifactPath path = createPath( getClass() , Artifact.METHOD , "foo" );
        
        // signal: method foo is lazy loading signature
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.SIGNATURE );
        // rule: method should load methods fields and annotations.
        checkRules( rules, path, ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS );
        // rule: method bar should not load anything
        checkRules( rules, append( path.getParent() , Artifact.METHOD , "bar" ) );
        // rule: field foo should not load anything
        checkRules( rules, append( path.getParent() , Artifact.FIELD , "foo" ) );
        
        
        // BUT, if annotations are loading, do not load signature...
        
        // signal: method "foo" is lazy loading annotations
        rules = impl.createRules( path, ArtifactElement.ANNOTATIONS );
        // rule: method "foo" should load annotations
        checkRules( rules, path, ArtifactElement.ANNOTATIONS );
        // rule: method "bar" should not load anything
        checkRules( rules, append( path.getParent() , Artifact.METHOD , "bar" ) );
        // rule: field "foo" should not load anything
        checkRules( rules, append( path.getParent() , Artifact.FIELD , "foo" ) );
        
    }
    
    @Test
    public void testChildren() {
        // create factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // when lazy loading a method, load also its signature (but not annotations).
        impl.lazyLoadChildren( Artifact.METHOD, ArtifactElement.SIGNATURE );
        // path to class
        ArtifactPath path = createPath( getClass() );
        
        // signal: class is lazy loading methods
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.METHODS );
        
        // rule: when scanning the class, methods should load...
        checkRules( rules, path, ArtifactElement.METHODS );
        // rule: when loading the method, its signature should load...
        checkRules( rules, append( path , Artifact.METHOD , "foo ") , ArtifactElement.SIGNATURE );
    }
    
    @Test
    public void testStrict() {
        // 1. in strict mode, method bar will not load annotations
        //    when method foo does.
        
        // create factory
        LazyLoadingRulesFactoryImpl impl = new LazyLoadingRulesFactoryImpl();
        // path to method
        ArtifactPath path = createPath( getClass() , Artifact.METHOD , "foo");
        
        // singal: method foo is loading annotations
        MetadataPathRules rules = impl.createRules( path, ArtifactElement.ANNOTATIONS );
        
        // rule: method foo loads annotations
        checkRules( rules, path, ArtifactElement.ANNOTATIONS );
        // rule: method bar does not load anything. 
        checkRules( rules , append( path.getParent() , Artifact.METHOD , "bar" ) );
        
        // 2. in non strict mode, method bar WILL load annotations
        //    when method foo does.
        impl.setLazySiblingsLoadRule( Artifact.CLASS, ArtifactElement.METHODS, false );
        // signal: method 'foo' is loading annotations.
        rules = impl.createRules( path , ArtifactElement.ANNOTATIONS );
        // rule: method foo loads annotations
        checkRules( rules, path, ArtifactElement.ANNOTATIONS );
        // rule: method bar also load anything. 
        checkRules( rules , append( path.getParent() , Artifact.METHOD , "bar" ) , ArtifactElement.ANNOTATIONS );

    }

    private ArtifactPath createPath( Class<?> cls , Object ...artifactAndNamePairs ) {
        ArtifactPath path = ArtifactPath.EMPTY_PATH;
        path = append( path , Artifact.CLASS , cls.getCanonicalName() );
        for ( int i = 0; i < artifactAndNamePairs.length ; ) {
            path = append( path , (Artifact)artifactAndNamePairs[i++] , (String)artifactAndNamePairs[i++] );
        }
        return path;
    }

    private ArtifactPath append( ArtifactPath path, Artifact artifact , String name ) {
        Metadata m = EasyMock.createNiceMock( Metadata.class );
        EasyMock.expect( m.getArtifact() ).andReturn( artifact ).anyTimes();
        EasyMock.expect( m.getName() ).andReturn( name ).anyTimes();
        EasyMock.expect( m.getPath() ).andReturn( path ).anyTimes();
        EasyMock.replay( m );
        return path.append( m );
    }

}
