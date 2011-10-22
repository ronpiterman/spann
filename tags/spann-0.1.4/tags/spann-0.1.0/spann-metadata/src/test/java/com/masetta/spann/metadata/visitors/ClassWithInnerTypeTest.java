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

import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;


public class ClassWithInnerTypeTest extends BaseTestMetadata {

    private static final String CLS = "ClassWithInnerType";
    private static final String FQ = getTestClassName( CLS );
    private static final String INNER = FQ + "$InnerClass";
    private static final String NON_GEN = FQ + "$NonGeneric";

    public ClassWithInnerTypeTest() {
        super(CLS);
    }
    
    //     InnerClass<String> innerClassString;
    @Test
    public void testInnerClassString() {
        GenericType gt = getFieldType( "innerClassString" );
        Check.check( gt, INNER, null );
        Check.check( gt , 0 , String.class , GenericCapture.IS, null );
        TypeMetadata tm = (TypeMetadata) Check.innerOf( gt, FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // List<InnerClass<String>> innerClassesString;
    @Test
    public void testInnerClassesString() {
        GenericType gt = getFieldType( "innerClassesString" );
        Check.check( gt, List.class , null );
        TypeArgument ta = Check.check( gt , 0 , INNER , GenericCapture.IS);
        Check.check( ta , 0 , String.class , GenericCapture.IS , null );
        
        TypeMetadata tm = (TypeMetadata) Check.innerOf( ta.getType(), FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // InnerClass<T> innerClassT;
    @Test
    public void testInnerClassT() {
        GenericType gt = getFieldType( "innerClassT" );
        Check.check( gt, INNER, null );
        Check.check( gt , 0 , null , GenericCapture.IS, "T" );
        TypeMetadata tm = (TypeMetadata) Check.innerOf( gt, FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // List<InnerClass<T>> innerClassesT;
    @Test
    public void testInnerClassesT() {
        GenericType gt = getFieldType( "innerClassesT" );
        Check.check( gt, List.class , null );
        TypeArgument ta = Check.check( gt , 0 , INNER , GenericCapture.IS);
        Check.check( ta , 0 , null, GenericCapture.IS , "T" );
        
        TypeMetadata tm = (TypeMetadata) Check.innerOf( ta.getType(), FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // InnerClass<?> innerClass;
    @Test
    public void testInnerClass() {
        GenericType gt = getFieldType( "innerClass" );
        Check.check( gt, INNER, null );
        Check.check( gt , 0 , null , null, null );
        
        TypeMetadata tm = (TypeMetadata) Check.innerOf( gt, FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // List<InnerClass<?>> innerClasses;
    @Test
    public void testInnerClasses() {
        GenericType gt = getFieldType( "innerClasses" );
        Check.check( gt, List.class , null );
        TypeArgument ta = Check.check( gt , 0 , INNER , GenericCapture.IS);
        Check.check( ta , 0 , null, null , null );
        
        TypeMetadata tm = (TypeMetadata) Check.innerOf( ta.getType(), FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // NonGeneric nonGeneric;
    @Test
    public void testNonGeneric() {
        GenericType gt = getFieldType( "nonGeneric" );
        Check.check( gt , NON_GEN , null );
        Check.check( gt.getType(), 0 );
        
        TypeMetadata tm = (TypeMetadata) Check.innerOf( gt, FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
    }
    
    // <X> InnerClass<X> innerClassX( Class<X> x )
    @Test
    public void innerClassX() {
        MethodMetadata mm = getMethodMetadata( "innerClassX" );
        
        GenericType gt = mm.getReturnType();
        Check.check( gt, INNER, null );
        
        Check.check( gt , 0 , null , GenericCapture.IS, "X" );
        TypeMetadata tm = (TypeMetadata) Check.innerOf( gt, FQ );
        Check.check( tm , 0 , GenericCapture.IS , "T" );
        
        Check.checkTypeParameter( mm.getTypeParameters() , 0 , Object.class, "X" );
        
        Check.check( Check.check( mm , 0 , Class.class , null ) , 0, null , GenericCapture.IS , "X" );
    }
    
    // <X> List<InnerClass<X>> innerClassesX( List<Class<X>> x ) {
    public void innerClassesX() {
        MethodMetadata mm = getMethodMetadata( "innerClassesX" );
        
        GenericType gt = mm.getReturnType();
        Check.check( gt, List.class, null );
        
        TypeArgument ta = Check.check( gt , 0 , INNER , GenericCapture.IS);
        Check.check( ta , 0 , null, GenericCapture.IS , "X" );
        TypeMetadata tm = (TypeMetadata) Check.innerOf( ta.getType(), FQ );
        
    }
    

}
