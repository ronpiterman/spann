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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.metadata.rules.MetadataPathRulesImpl;

public class MetadataRulesBuilderTest {
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testSupportCheck() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        builder.add( Artifact.CLASS , null).load( ArtifactElement.PARAMETER_ANNOTATIONS );
    }
    
    @Test(expectedExceptions=IllegalStateException.class)
    public void testWrongParent() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        builder.add( Artifact.CLASS, null).add( Artifact.PARAMETER , null );
    }
    
    @Test
    public void testEveryonSupportsAnnotation() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        builder.add( Artifact.CLASS, null).add( Artifact.ANNOTATION , null );
        builder.add( Artifact.METHOD, null).add( Artifact.ANNOTATION , null );
        builder.add( Artifact.FIELD, null).add( Artifact.ANNOTATION , null );
        
        // TODO
    }
    
    @Test
    public void testSinglePath() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        builder.add( Artifact.CLASS, "p").load( ArtifactElement.ANNOTATIONS );
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS , "p" , Artifact.ANNOTATION , "a" );
        assertElements( rules , path.getParent() , ArtifactElement.ANNOTATIONS );
        assertElements( rules , path );
    }
    
    @Test
    public void testTwoPaths() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all annotations scan signature
        builder.add( Artifact.CLASS , "a" ).load( ArtifactElement.SIGNATURE );
        builder.add( Artifact.CLASS , "b" ).load( ArtifactElement.ANNOTATIONS );

        // b annotations scan annotations
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS , "a" );
        assertElements( rules , path , ArtifactElement.SIGNATURE  );
        
        path = createPath( Artifact.CLASS , "b" );
        assertElements( rules , path , ArtifactElement.ANNOTATIONS  );

        path = createPath( Artifact.CLASS , "c" );
        assertElements( rules , path );

    }
    
    @Test
    public void testImplicit() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all annotations scan signature
        builder.add( Artifact.CLASS , "p" ).load( ArtifactElement.SIGNATURE );
        builder.add( Artifact.METHOD, "m" ).load( ArtifactElement.ANNOTATIONS );
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS , "p" );
        assertElements( rules , path , ArtifactElement.METHODS , ArtifactElement.SIGNATURE );
        
        path = createPath( Artifact.CLASS , "other" );
        assertElements( rules , path );
    }
    
    @Test
    public void testFields() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all methods scan signature
        builder.add( Artifact.CLASS , "c" ).
            add( Artifact.FIELD, null ).load( ArtifactElement.SIGNATURE );
        builder.add( Artifact.FIELD, "f" ).load( ArtifactElement.ANNOTATIONS );
        
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "foo" );
        assertElements( rules , path , ArtifactElement.SIGNATURE  );
        
        path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "f" );
        assertElements( rules , path , ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS );
        
    }
    
    @Test
    public void testFieldAnn() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all methods scan signature
        builder.add( Artifact.CLASS , "c" ).
            add( Artifact.FIELD, null ).load( ArtifactElement.SIGNATURE );
        builder.add( Artifact.FIELD, "f" ).load( ArtifactElement.ANNOTATIONS );
        
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "foo" );
        assertElements( rules , path , ArtifactElement.SIGNATURE  );
        
        path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "f" );
        assertElements( rules , path , ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS );
        
        path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "f" , Artifact.ANNOTATION , "b" );
        assertElements( rules , path );
        
    }
    
    @Test
    public void testFieldsAfterMethods() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all methods scan signature
        builder.add( Artifact.CLASS , "c" ).
            add( Artifact.METHOD , null ).load( ArtifactElement.SIGNATURE );
        
        builder.add( Artifact.FIELD, null ).load( ArtifactElement.SIGNATURE );
        builder.add( Artifact.FIELD, "f" ).load( ArtifactElement.ANNOTATIONS );
        
        MetadataPathRulesImpl rules = builder.build();
        
        ArtifactPath path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "foo" );
        assertElements( rules , path , ArtifactElement.SIGNATURE  );
        
        path = createPath( Artifact.CLASS, "c" , Artifact.FIELD , "f" );
        assertElements( rules , path , ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS );
        
    }

    
    @Test
    public void testFallback() {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        // all methods scan signature
        builder.add( Artifact.CLASS , "c" ).
            add( Artifact.METHOD , null ).load( ArtifactElement.SIGNATURE );
        // method m1() scans annotations.
        builder.add( Artifact.METHOD , "m1" ).load( ArtifactElement.ANNOTATIONS );
        
        // field 'f' scans annotations
        builder.add( Artifact.FIELD , "f" ).load( ArtifactElement.ANNOTATIONS );
        // and all fields scan signature
        builder.add( Artifact.FIELD , null ).load( ArtifactElement.SIGNATURE );
        
        MetadataPathRulesImpl rules = builder.build();
            
        // class c will scan methods and fields
        ArtifactPath path = createPath( Artifact.CLASS , "c" );
        assertElements( rules , path , ArtifactElement.METHODS , ArtifactElement.FIELDS );
        
        // same package, but different class name
        path = createPath( Artifact.CLASS , "d" );
        assertElements( rules , path );
        
        // method foo will scan signature
        path = createPath( Artifact.CLASS , "c" , Artifact.METHOD , "foo" );
        assertElements( rules , path , ArtifactElement.SIGNATURE );
        
        // method m1 will scan signature (applied to all methods in class c ) and annotations
        path = createPath( Artifact.CLASS , "c" , Artifact.METHOD , "m1" );
        assertElements( rules , path , ArtifactElement.SIGNATURE, ArtifactElement.ANNOTATIONS );
        
        // m1 arguments have no rules
        path = createPath( Artifact.CLASS , "c" , Artifact.METHOD , "m1" , Artifact.PARAMETER , "");
        assertElements( rules , path );
        
        // m2 will scan nothing
        path = createPath( Artifact.CLASS , "c" , Artifact.METHOD , "m2" );
        assertElements( rules , path , ArtifactElement.SIGNATURE );
        
        // field f - signature and annotations
        path = createPath( Artifact.CLASS , "c" , Artifact.FIELD , "f" );
        assertElements( rules , path , ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS);
        
        // other fields - signature only
        path = createPath( Artifact.CLASS , "c" , Artifact.FIELD , "foo" );
        assertElements( rules , path , ArtifactElement.SIGNATURE );
        
        // field f from other classes - nothing
        path = createPath( Artifact.CLASS , "d" , Artifact.FIELD , "f" );
        assertElements( rules , path );
        
    }
    
    private void assertElements( MetadataPathRulesImpl rules , ArtifactPath path , ArtifactElement ...expectedElements ) {
        int e = rules.getElements( path );
        Assert.assertEquals( e , ArtifactElement.collect( expectedElements ) );
    }

    private ArtifactPath createPath(Object ...artifactNamePairs) {
        ArtifactPath path = ArtifactPath.EMPTY_PATH;
        for ( int i = 0; i < artifactNamePairs.length ; ) {
            path = path.append( createMetadata( artifactNamePairs[i++] , artifactNamePairs[i++] ) );
        }
        return path;
    }

    private Metadata createMetadata(Object object, Object object2) {
        return new MetadataStab( (Artifact) object , (String) object2 );
    }


}
