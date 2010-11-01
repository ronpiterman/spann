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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.TypeParameter;

public class ClassWithManyGenTest extends BaseTestMetadata {
    
    public ClassWithManyGenTest() {
        super("ClassWithManyGen");
    }
    
    @Test
    public void testGenericTypeArgs() {
        List<TypeParameter> tparams = getMetadata().getTypeParameters();
        Assert.assertEquals( tparams.size(), 2 );
        
        // 1.
        // T extends List<Set<Comparable<Comparator<Map<String,Long>>>>
        
        // T extends List<...>
        TypeParameter tp = checkParam( getMetadata() , 0 , List.class , "T" );
        // ... extends List<Set<...>>
        TypeArgument ta = checkArg( tp , 0 , Set.class , GenericCapture.IS , null );
        // ...<Set<Comparable<...>>
        ta = checkArg( ta , 0 , Comparable.class , GenericCapture.IS , null );
        // ...<Comparable<Comparator<...>>
        ta = checkArg( ta , 0 , Comparator.class , GenericCapture.IS , null );
        // ...<Comparator<Map<...>>
        ta = checkArg( ta , 0 , Map.class , GenericCapture.IS , null );
        
        // Map<String,Long>
        last( checkArg( ta , 0 , String.class , GenericCapture.IS , null ) );
        last( checkArg( ta , 1 , Long.class , GenericCapture.IS , null ) );
        
        // 2. 
        // X extends Map<Set<? extends Comparable<Comparator<? super Map<String,Long>>>>,Class<T>>>
        
        // X extends Map
        tp = checkParam( getMetadata() , 1 , Map.class , "X" );
        // ... extends Map<Set<...>,...>
        ta = checkArg( tp , 0 , Set.class , GenericCapture.IS , null );
        // <Set<? extends Comparable
        ta = checkArg( ta , 0 , Comparable.class , GenericCapture.EXTENDS , null );
        // ... extends Comparable<Comparator<...
        ta = checkArg( ta , 0 , Comparator.class , GenericCapture.IS , null );
        // ...<Comparator<? super Map...
        ta = checkArg( ta , 0 , Map.class , GenericCapture.SUPER_OF , null );
        // Map<String,Long>
        last( checkArg( ta, 0, String.class, GenericCapture.IS, null ) );
        last( checkArg( ta, 1, Long.class, GenericCapture.IS, null ) );
        
        // , Class<T>
        ta = checkArg( tp , 1 , Class.class , GenericCapture.IS, null );
        last( checkArg( ta , 0 , null , GenericCapture.IS, "T" ) );
        
        // 3.
        // extends ArrayList<List<Set<Comparable<Comparator<Map<String,X>>>>>>
        
        // extends ArrayList<List
        ta = checkArg( getMetadata().getSuperClass( true ) , 0 , List.class , GenericCapture.IS, null );
        // List<Set
        ta = checkArg( ta , 0 , Set.class , GenericCapture.IS, null );
        // Set<Comparable
        ta = checkArg( ta , 0 , Comparable.class , GenericCapture.IS, null );
        // Comparable<Comparator
        ta = checkArg( ta , 0 , Comparator.class , GenericCapture.IS, null );
        // Comparator<Map
        ta = checkArg( ta, 0 , Map.class , GenericCapture.IS, null );
        // Map<String,X>
        last( checkArg( ta , 0 , String.class , GenericCapture.IS, null ) );
        last( checkArg( ta , 1 , null , GenericCapture.IS, "X" ) );
        
        Set<ClassMetadata> ifcs = getMetadata().getInterfaces(true);
        Iterator<ClassMetadata> i = ifcs.iterator();
        Assert.assertTrue( i.hasNext() );
        ClassMetadata ifc = i.next();
        
        // implements Comparator<List<Set<Comparable<Comparator<Map<T,Long>>>>>>,
        ta = checkArg( ifc, 0, List.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Set.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Comparable.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Comparator.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Map.class, GenericCapture.IS, null );
        last( checkArg( ta, 0, null, GenericCapture.IS, "T" ) );
        last( checkArg( ta, 1, Long.class, GenericCapture.IS, null ) );
        
        // implements ... , 
        // GenericInterface<Comparable<Map<String,X>>,Set<Comparable<Comparator<String>>>>
        
        Assert.assertTrue( i.hasNext() );
        ifc = i.next();
        ta = checkArg( ifc, 0, Comparable.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Map.class, GenericCapture.IS, null );
        last( checkArg( ta, 0, String.class, GenericCapture.IS, null ) );
        last( checkArg( ta, 1, null, GenericCapture.IS, "X" ) );
        
        ta = checkArg( ifc, 1, Set.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Comparable.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, Comparator.class, GenericCapture.IS, null );
        ta = checkArg( ta, 0, String.class, GenericCapture.IS, null );
        last( ta );
        }
    
    private void last(TypeArgument ta) {
        if ( ta.getType() == null ) {
            Assert.assertNotNull( ta.getContextBoundTypeParameter() );
        }
        else {
            Assert.assertNull( ta.getContextBoundTypeParameter() );
            Assert.assertFalse( ta.getType() instanceof TypeMetadata );
        }
    }

    private TypeParameter checkParam(ClassMetadata metadata, int index,
            Class<?> type, String name ) {
        TypeParameter tp = metadata.getTypeParameters().get( index );
        return check(tp, type, name);
    }
    
    private TypeArgument checkArg(ClassMetadata p, int index,
            Class<?> type, GenericCapture capture, String name ) {
        return checkArg( (TypeMetadata)p , index , type , capture , name );
    }
    
    private TypeArgument checkArg(TypeParameter p, int index,
            Class<?> type, GenericCapture capture, String name ) {
        TypeArgument tp = ((TypeMetadata)p.getType()).getTypeArguments().get( index );
        return check(tp, type, capture, name);
    }
    
    private TypeArgument checkArg(TypeArgument p, int index,
            Class<?> type, GenericCapture capture, String name ) {
        TypeArgument tp = ((TypeMetadata)p.getType()).getTypeArguments().get( index );
        return check(tp, type, capture, name);
    }
    
    private TypeArgument checkArg(TypeMetadata metadata, int index,
            Class<?> type, GenericCapture capture, String name ) {
        TypeArgument tp = metadata.getTypeArguments().get( index );
        return check(tp, type, capture, name);
    }

    private TypeArgument check(TypeArgument ta, Class<?> type,
            GenericCapture capture, String name) {
        if ( type != null )
            Assert.assertEquals( ta.getType().getName(), type.getCanonicalName() , "type");
        else
            Assert.assertNull( ta.getType() , "type" );
    
        Assert.assertEquals( ta.getCapture(), capture , "capture" );
        Assert.assertEquals( ta.getContextBoundTypeParameter(), name , "name" );
        
        return ta;
    }
    
    private TypeParameter check(TypeParameter tp, Class<?> type, String name) {
        if ( type != null )
            Assert.assertEquals( tp.getType().getName(), type.getCanonicalName() , "type");
        else
            Assert.assertNull( tp.getType() , "type" );
    
        Assert.assertEquals( tp.getName(), name , "name" );
        
        return tp;
    }

}
