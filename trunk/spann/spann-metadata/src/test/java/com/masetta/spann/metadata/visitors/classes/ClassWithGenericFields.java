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

public class ClassWithGenericFields<X,T extends Serializable> {
    
    X x;
    
    T t;
    
    Class<X> classX;
    
    Class<? super T> classT;
    
    List<Set<String>> listSetString;
    
    List<Set<X>> listSetX;
    
    Map<Set<X>,List<Map<X,String>>> many;
    
    Map<Set<? super X>,List<Map<? extends X,String>>> many2;
    
    List<?> list;
    
    Map<?,T> map;
    
    int i;
    
    boolean b;
    
    float f;
    
    int[] is;
    
    boolean[] bs;
    
    String[] s;
    
    List<String[]> listOfStrings;
    
    List<String[][]> listOfStrings2;
    
    List<X[]> listOfXs;
    
    List<X[][]> listOfXs2;
    
    List<String>[] listsOfString;
    
    List<InnerClass<String>> innerClasses;
    
    private class InnerClass<P> {}
    
}
