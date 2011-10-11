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

package com.masetta.spann.metadata.core;



/**
 * A type parameter, such as <code>T</code> in the <code>class Foo&lt;T extends Bar> </code> 
 * or <code>V</code> in <code>&lt;V> void getValue( ... ) {...}</code> 
 */
public interface TypeParameter extends TypeReference {
    
    /**
     * The class metadata this type parameter should extend. May be null.
     */
    ClassMetadata getType();
    
    /**
     * The type parameter name.
     */
    String getName();
    
}
