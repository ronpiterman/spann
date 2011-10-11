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

public class ClassWithInnerType<T> {
    
    T t;
    
    InnerClass<String> innerClassString;
    
    List<InnerClass<String>> innerClassesString;
    
    InnerClass<T> innerClassT;
    
    List<InnerClass<T>> innerClassesT;
    
    InnerClass<?> innerClass;
    
    List<InnerClass<?>> innerClasses;
    
    NonGeneric nonGeneric;
    
    List<NonGeneric> nonGeerics;
    
    <X> InnerClass<X> innerClassX( Class<X> x ) { return null; }
    
    <X> List<InnerClass<X>> innerClassesX( List<Class<X>> x ) { return null; }
    
    <X> InnerClass<T> innerClassT( InnerClass<X> x ) { return null; }
    
    <X> List<InnerClass<T>> innerClassesT( List<InnerClass<X>> x , boolean b ) { return null; }
    
    InnerClass<String> innerClassString( String a, InnerClass<String> s ) { return null; }
    
    List<InnerClass<String>> innerClassesString( InnerClass<String> s , List<T> list ) { return null; }
    
    private class InnerClass<V> {
        
        T t;
        
        V v;
        
    }
    
    private class NonGeneric {
    }
    
    private static class InnerStaticClass<V> {}
    
    private static interface InnerInterface {
        String interfaceMethod();
    }
    
    public void someMethod() {
    	new Serializable() {
		};
    }
    
    public static void someStaticMethod() {
    	new Serializable() {
		};
    }


}
