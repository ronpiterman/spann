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
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.core.TypeArgument;

public class ClassWithGenericMethodsTest extends BaseTestMetadata {

    public ClassWithGenericMethodsTest() {
        super("ClassWithGenericMethods");
    }
    
    @Test
    public void testSimple() {
        MethodMetadata mm = getMethodMetadata( "simple");
        Assert.assertNull( mm.getReturnType());
        
        for ( ParameterMetadata pm : mm.getParameters() )
            Assert.assertNull( pm.getParameterType() );
    }
    
    @Test
    public void testT() {
        MethodMetadata mm = getMethodMetadata( "t");
        Check.check( mm.getReturnType() , "T" );
    }
    
    @Test
    public void testT2() {
        MethodMetadata mm = getMethodMetadata( "t2");
        Assert.assertNull( mm.getReturnType() );
        Assert.assertNull( mm.getReturnClass() );
        
        Check.check( mm , 0 , null , "T" );
    }
    
    @Test
    public void testT3() {
        MethodMetadata mm = getMethodMetadata( "t3");
        Check.check( mm.getReturnType() ,  "T" );
        Check.check( mm , 0 , null , "T" );
    }

    // T t4( T t , int i , String string );
    @Test
    public void testT4() {
        MethodMetadata mm = getMethodMetadata( "t4");
        Check.check( mm.getReturnType() , "T" );
        Check.check( mm , 0 , null , "T" );
        Check.check( mm , 1 , null , null );
        Check.check( mm , 2 , null , null );
    }
    
    // T t5( T t , X x , int i , String s );
    @Test
    public void testT5() {
        MethodMetadata mm = getMethodMetadata( "t5");
        checkParamsCount( mm , 4 );
        Check.check( mm.getReturnType() , "T" );
        Check.check( mm , 0 , null , "T" );
        Check.check( mm , 1 , null , "X" );
        Check.check( mm , 2 , null , null );
        Check.check( mm , 3 , null , null );
    }
    
    //T t6( T t , List<X> x , String s );
    @Test
    public void testT6() {
        MethodMetadata mm = getMethodMetadata( "t6");
        checkParamsCount( mm , 3 );
        Check.check( mm.getReturnType() , "T" );
        Check.check( mm , 0 , null , "T" );
        Check.check( Check.check( mm , 1 , List.class , null  ), 0 , null , GenericCapture.IS, "X" );
        Check.check( mm , 2 , null , null );
    }
    
    // T t7( T t , Set<String> x , List<X> l , int i, String s );
    public void testT7() {
        MethodMetadata mm = getMethodMetadata( "t7");
        checkParamsCount( mm , 5 );
        Check.check( mm.getReturnType() , "T" );
        Check.check( mm , 0 , null , "T" );
        Check.check( Check.check( mm , 1 , Set.class , null  ), 0 , String.class , GenericCapture.IS, null );
        Check.check( Check.check( mm , 2 , List.class , null  ), 0 , null , GenericCapture.IS, "X" );
        Check.check( mm , 3 , null , null );
        Check.check( mm , 4 , null , null );
    }
    
    // void t8( float a , Object o , T t , Map<?,String> x , List<Map<? extends X,T>> l , int i, String s );
    @Test
    public void testT8() {
        MethodMetadata mm = getMethodMetadata( "t8");
        checkParamsCount( mm , 7 );
        Check.check( mm.getReturnType() , null );
        Check.check( mm , 0 , null , null );
        Check.check( mm , 1 , null , null );
        Check.check( mm , 2 , null , "T" );
        GenericType p = Check.check( mm , 3 , Map.class , null );
        Check.check( p , 0 , null , null, null );
        Check.check( p , 1 , String.class , GenericCapture.IS, null );
        
        p = Check.check( mm , 4 , List.class , null );
        TypeArgument a = Check.check( p , 0 , Map.class , GenericCapture.IS , null );
        Check.check( a , 0 , null , GenericCapture.EXTENDS , "X" );
        Check.check( a , 1 , null , GenericCapture.IS, "T" );
        
        Check.check( mm , 5 , null , null );
        Check.check( mm , 6 , null , null );
    }
    
    // Class<X> clsX();
    @Test
    public void testClsX() {
        MethodMetadata mm = getMethodMetadata( "clsX");
        checkParamsCount( mm , 0 );
        Check.check( Check.check( mm.getReturnType() , Class.class , null ) , 0 , null , GenericCapture.IS , "X" );
    }
    
    // Class<X> clsX1( Class<T> cls );
    @Test
    public void testClsX1() {
        MethodMetadata mm = getMethodMetadata( "clsX1");
        checkParamsCount( mm , 1 );
        Check.check( Check.check( mm.getReturnType() , Class.class , null ) , 0 , null , GenericCapture.IS , "X" );
        
        Check.check( Check.check( mm , 0 , Class.class , null ) , 0 , null , GenericCapture.IS , "T" );
    }
    
    // Class<X> clsX2( Class<T> cls , X x );
    @Test
    public void testClsX2() {
        MethodMetadata mm = getMethodMetadata( "clsX2");
        checkParamsCount( mm , 2 );
        Check.check( Check.check( mm.getReturnType() , Class.class , null ) , 0 , null , GenericCapture.IS , "X" );
        
        Check.check( Check.check( mm , 0 , Class.class , null ) , 0 , null , GenericCapture.IS , "T" );
        Check.check( mm , 1 , null , "X" );
    }

    // Class<X> clsX3( Class<T> cls , X x , String s );
    @Test
    public void testClsX3() {
        MethodMetadata mm = getMethodMetadata( "clsX3");
        checkParamsCount( mm , 3 );
        Check.check( Check.check( 
                mm.getReturnType() , Class.class , null ) , 
                0 , null , GenericCapture.IS , "X" );
        
        Check.check( Check.check( 
                mm , 0 , Class.class , null ) , 
                0 , null , GenericCapture.IS , "T" );
        
        Check.check( mm , 1 , null , "X" );
        Check.check( mm , 2 , null , null );
    }
    
    // Class<X> clsX4( String s1 , Class<T> cls , X x , String s );
    @Test
    public void testClsX4() {
        MethodMetadata mm = getMethodMetadata( "clsX4");
        checkParamsCount( mm , 4 );
        Check.check( Check.check( 
                mm.getReturnType() , Class.class , null ) , 
                0 , null , GenericCapture.IS , "X" );
        
        Check.check( mm , 0 , null , null );
        Check.check( Check.check( 
                mm , 1 , Class.class , null ) , 
                0 , null , GenericCapture.IS , "T" );
        
        Check.check( mm , 2 , null , "X" );
        Check.check( mm , 3 , null , null );
    }
    
    // List<Class<X>> listClassX( Set<Class<T>> set );
    @Test
    public void testClassX() {
        MethodMetadata mm = getMethodMetadata( "listClassX");
        checkParamsCount( mm , 1 );
        Check.check( Check.check ( Check.check( 
                mm.getReturnType() , List.class , null ), 
                0 , Class.class , GenericCapture.IS , null ),
                0 , null , GenericCapture.IS , "X" );
        
        Check.check( Check.check( Check.check( 
                mm , 0 , Set.class , null ), 
                0 , Class.class , GenericCapture.IS , null ), 
                0 , null , GenericCapture.IS , "T" ); 
    }
    
    
    //----------------------------------------------------------------------------------------------
    
    private void checkParamsCount(MethodMetadata mm, int arguments) {
        Assert.assertEquals( mm.getParameters().size(), arguments, "parameters count" );
    }
    
}
