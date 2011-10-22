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

import java.io.Serializable;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.TypeParameter;

public class ClassWithGenericSuperTest extends BaseTestMetadata {

    public ClassWithGenericSuperTest() {
        super("ClassWithGenericSuper");
    }
    
    @Test
    public void testGenericTypeArgs() {
        List<TypeParameter> tp = getMetadata().getTypeParameters();
        Assert.assertEquals( tp.size(), 1 );
        
        String[] names = { "T" };
        Class<?>[] types = { Serializable.class };
        
        for ( int i = 0; i < 1 ; i++ ) {
            TypeParameter p = tp.get( i );
            Assert.assertEquals( p.getName() , names[i] );
            Assert.assertEquals( p.getType().getName() , types[i].getCanonicalName() );
        }
        
        ClassMetadata sup = getMetadata().getSuperClass( true );
        Assert.assertTrue( sup instanceof TypeMetadata , "super is typeMetadata");
    }
    
}
