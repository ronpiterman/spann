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

package com.masetta.spann.metadata.visitors.lazy;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.metadata.visitors.BaseTestMetadata;

public class TestLazyOff extends BaseTestMetadata {

    public TestLazyOff() {
        super("SimpleClass" );
        MetadataPathRulesBuilder b = new MetadataPathRulesBuilder();
        b.add( Artifact.CLASS, null ).load( ArtifactElement.SELF );
        setRules( b.build(), "lazy/off");
    }
    
    @Test
    public void testLazyOff() {
        ClassMetadata cm = getMetadata();
        Assert.assertNotNull( cm );
        Assert.assertEquals( cm.getFields().size() , 0 );
        Assert.assertEquals( cm.getMethods().size() , 0 );
        Assert.assertEquals( cm.getTypeParameters().size() , 0 );
    }

}
