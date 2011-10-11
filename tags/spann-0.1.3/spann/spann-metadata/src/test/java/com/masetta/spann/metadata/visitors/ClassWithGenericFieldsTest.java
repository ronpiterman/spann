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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.support.FieldMetadataSupport;

public class ClassWithGenericFieldsTest extends BaseTestMetadata {
    
    public ClassWithGenericFieldsTest() {
        super("ClassWithGenericFields" );
    }
    
    @Test
    public void testX() {
        GenericType p = getFieldType("x");
        Check.check( p , "X" );
    }
    
    @Test
    public void testT() {
        GenericType p = getFieldType("t");
        Check.check( p , "T" );
    }
    
    @Test
    public void testClassX() {
        GenericType p = getFieldType("classX");
        Check.check( p , Class.class  , null );
        Check.check( p , 0 , null , GenericCapture.IS, "X" );
    }
    
    @Test
    public void testClassT() {
        GenericType p = getFieldType("classT");
        Check.check( p , Class.class  , null );
        Check.check( p , 0 , null , GenericCapture.SUPER_OF, "T" );
    }

    
    @Test
    public void testListSetString() {
        GenericType p = getFieldType("listSetString");
        Check.check( p , List.class  , null );
        TypeArgument a = Check.check( p , 0 , Set.class, GenericCapture.IS, null );
        Check.check( a , 0 , String.class , GenericCapture.IS , null );
    }
    
    @Test
    public void testListSetX() {
        GenericType p = getFieldType("listSetX");
        Check.check( p , List.class  , null );
        TypeArgument a = Check.check( p , 0 , Set.class, GenericCapture.IS, null );
        Check.check( a , 0 , null , GenericCapture.IS , "X" );
    }
    
    @Test
    public void testMany() {
        GenericType p = getFieldType("many");
        Check.check( p , Map.class  , null );
        TypeArgument a = Check.check( p , 0 , Set.class, GenericCapture.IS, null );
        Check.check( a , 0 , null , GenericCapture.IS , "X" );
        a = Check.check( p , 1 , List.class , GenericCapture.IS , null );
        a = Check.check( a , 0 , Map.class , GenericCapture.IS , null );
        Check.check( a , 0 , null , GenericCapture.IS , "X" );
        Check.check( a , 1 , String.class , GenericCapture.IS , null );
    }
    
    @Test
    public void testMany2() {
        GenericType p = getFieldType("many2");
        Check.check( p , Map.class  , null );
        TypeArgument a = Check.check( p , 0 , Set.class, GenericCapture.IS, null );
        Check.check( a , 0 , null , GenericCapture.SUPER_OF , "X" );
        a = Check.check( p , 1 , List.class , GenericCapture.IS , null );
        a = Check.check( a , 0 , Map.class , GenericCapture.IS , null );
        Check.check( a , 0 , null , GenericCapture.EXTENDS , "X" );
        Check.check( a , 1 , String.class , GenericCapture.IS , null );
    }
    
    @Test
    public void testList() {
        GenericType p = getFieldType("list");
        Check.check( p , List.class , null );
        Check.check( p , 0 , null, null, null );
    }
    
    @Test
    public void testMap() {
        GenericType p = getFieldType("map");
        Check.check( p , Map.class , null );
        Check.check( p , 0 , null, null, null );
        Check.check( p , 1 , null, GenericCapture.IS, "T" );
    }
    
    @Test
    public void testListOfStringArray() {
        GenericType p = getFieldType("listOfStrings");
        Check.check( p , List.class , null );
        TypeArgument ta = Check.check( p , 0 , String.class, GenericCapture.IS, null );
        Assert.assertEquals( ta.getType().getDimensions(), 1 );
    }
    
    @Test
    public void testListOfStringArray2() {
        GenericType p = getFieldType("listOfStrings2");
        Check.check( p , List.class , null );
        TypeArgument ta = Check.check( p , 0 , String.class, GenericCapture.IS, null );
        Assert.assertEquals( ta.getType().getDimensions(), 2 );
    }
    
    @Test
    public void testListOfXArray() {
        GenericType p = getFieldType("listOfXs");
        Check.check( p , List.class , null );
        TypeArgument ta = Check.check( p , 0 , null, GenericCapture.IS, "X" );
        Assert.assertEquals( ta.getContextBoundParameterDimensions(), 1 );
    }
    
    @Test
    public void testListOfXs2() {
        GenericType p = getFieldType("listOfXs2");
        Check.check( p , List.class , null );
        TypeArgument ta = Check.check( p , 0 , null, GenericCapture.IS, "X" );
        Assert.assertEquals( ta.getContextBoundParameterDimensions(), 2 );
    }
    
    @Test
    public void testListsOfStrings() {
        final String name = "listsOfString";
        GenericType p = getFieldType(name);
        Check.check( p , List.class , null );
        Check.check( p , 0 , String.class, GenericCapture.IS, null);
        Assert.assertEquals( FieldMetadataSupport.findField( getMetadata(), name ).getFieldClass().getDimensions() , 1 );
    }

}
