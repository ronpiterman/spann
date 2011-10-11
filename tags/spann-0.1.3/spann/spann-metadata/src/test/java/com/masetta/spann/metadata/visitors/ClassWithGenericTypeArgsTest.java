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
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.TypeParameter;

public class ClassWithGenericTypeArgsTest extends BaseTestMetadata {

    public ClassWithGenericTypeArgsTest() {
        super("ClassWithGenericTypeArgs");
    }
    
    @Test
    public void testGenericTypeArgs() {
        List<TypeParameter> tp = getMetadata().getTypeParameters();
        Assert.assertEquals( tp.size(), 3 );
        
        String[] names = { "M" , "T" , "V" };
        Class<?>[] types = { Serializable.class , List.class , Map.class };
        
        for ( int i = 0; i < 3 ; i++ ) {
            TypeParameter p = tp.get( i );
            Assert.assertEquals( p.getName() , names[i] );
            Assert.assertEquals( p.getType().getName() , types[i].getCanonicalName() );
        }
        
        TypeParameter p = tp.get( 0 );
        Assert.assertFalse( p.getType() instanceof TypeMetadata , "Serializable is not a TypeMetadata");
        
        p = tp.get( 1 );
        Assert.assertTrue( p.getType() instanceof TypeMetadata , "List<String> is a TypeMetadata");
        TypeMetadata tm = (TypeMetadata)p.getType();
        Assert.assertEquals( tm.getTypeArguments().size() , 1 );
        Assert.assertEquals( tm.getTypeArguments().get( 0 ).getType().getName() , String.class.getCanonicalName() );
        
        p = tp.get( 2 );
        Assert.assertTrue( p.getType() instanceof TypeMetadata , "Map<M,T> is a TypeMetadata");
        tm = (TypeMetadata)p.getType();
        Assert.assertEquals( tm.getTypeArguments().size() , 2 );
        
        TypeArgument p1 = tm.getTypeArguments().get( 0 );
        // TODO
        
    }
    
}
