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
 * A specification of a type parameter, either with a context-bound type parameter or
 * with a (more) concrete type information. 
 * 
 * @author Ron Piterman    
 */
public interface TypeArgument extends TypeReference {
    
    GenericCapture getCapture();
    
    /**
     * The class metadata this generic type refers to as either "is", "super" or "extends" 
     * (as specified by the capture property),
     * <br> 
     * Mutually exclusive with {@link #getContextBoundTypeParameter()}
     * <br>
     * Will return null if this type argument refers to a context bound type argument 
     * ( such as the type argument of superclass List in  
     * <code> class Foo&lt;T> extends List&lt;T> </code> ).<br>
     * Having <code>class Foo implements List&lt;String></code>, the TypeArgument of the super
     * interface List will have a <code>type</code> of String.
     * 
     * @return the type bound to this type argument, or null if none.
     */
    ClassMetadata getType();
    
    /**
     * A context bound type parameter name this TypeArgument references. Mutually exclusive
     * with {@link #getType()}
     * <p>
     * For example, in the class signature 
     * <code>class Foo&lt;X> extends ArrayList&lt;X> ...</code>
     * the TypeArgument of superclass ArrayList will have typeParameterName "X".
     * 
     * @return the context bound type parameter name this TypeArgument is bount to, or null if none.
     */
    String getContextBoundTypeParameter();
    
    /**
     * If the argument represents a context bound parameter array, return
     * the dimensions of the array.
     * <p>
     * If the context bound parameter is not an array, return 0.
     * 
     * @return the array dimensions of this TypeArgument.
     */
    int getContextBoundParameterDimensions();

}
