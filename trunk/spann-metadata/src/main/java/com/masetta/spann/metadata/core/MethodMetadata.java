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

import java.util.List;

import com.masetta.spann.metadata.core.modifier.MethodModifier;

/**
 * Metadata of a method
 * @author Ron Piterman    
 */
public interface MethodMetadata extends Metadata , AnnotatedElementMetadata {
    
    /** Modifier */
    MethodModifier getModifier();
    
    /** Return type of the method. */
    ClassMetadata getReturnClass();
    
    /** 
     * Generic return type of the method. 
     * Note that this never indicates array dimensions. For array dimensions
     * use {@link #getReturnClass() getReturnType() }.{@link ClassMetadata#getDimensions() getDimensions()}
     * @return
     */
    GenericType getReturnType();

    /**
     * @return Metadata of the parameters of the method.
     */
    List<ParameterMetadata> getParameters();

    /**
     * Return the type parameters of this methods.
     * For exmaple, method <code>&lt;X> X get( String key , Class&lt;X> type);</code>
     * will have one type parameter with name 'X'.
     * @return
     */
    List<TypeParameter> getTypeParameters();
    
    Object getDefault();
    
    ClassMetadata getParent();
    
}
