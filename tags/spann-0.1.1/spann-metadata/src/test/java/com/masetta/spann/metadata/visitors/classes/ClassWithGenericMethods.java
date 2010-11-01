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

package com.masetta.spann.metadata.visitors.classes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClassWithGenericMethods<T,X extends Serializable> {
    
    int i();
    
    boolean b();
    
    float f();
    
    String simple();
    
    String simple1( String string , int i );
    
    T t();
    
    void t2( T t );
    
    T t3( T t );
    
    T t4( T t , int i , String string );
    
    T t5( T t , X x , int i , String s );
    
    T t6( T t , List<X> x , String s );
    
    T t7( T t , Set<String> x , List<X> l , int i, String s );
    
    void t8( float a , Object o , T t , Map<?,String> x , List<Map<? extends X,T>> l , int i, String s );
    
    Class<X> clsX();
    
    Class<X> clsX1( Class<T> cls );
    
    Class<X> clsX2( Class<T> cls , X x );
    
    Class<X> clsX3( Class<T> cls , X x , String s );
    
    Class<X> clsX4( String s1 , Class<T> cls , X x , String s );
    
    List<Class<X>> listClassX( Set<Class<T>> set );

}
