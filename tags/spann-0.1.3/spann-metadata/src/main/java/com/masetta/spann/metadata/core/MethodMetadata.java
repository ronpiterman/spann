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
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public interface MethodMetadata extends Metadata , AnnotatedElementMetadata {
    
    /**
     * Retrieve the modifier of the method.
     *
     * @return a {@link MethodModifier} object.
     */
    MethodModifier getModifier();
    
    /**
     * Retrieve the return type of the method or null for void methods.
     *
     * @return a {@link ClassMetadata} representing the return
     * 		class of the method, or null if the method is void.
     */
    ClassMetadata getReturnClass();
    
    /**
     * Generic return type of the method.
     * Note that this never indicates array dimensions. For array dimensions
     * use methodMetadata.{@link #getReturnClass() getReturnType() }.{@link ClassMetadata#getDimensions() getDimensions()}
     *
     * @return the generic return type of the method, if generic information is available, otherwise null.
     */
    GenericType getReturnType();

    /**
     * Retrieve the method's parameters metadata.
     *
     * @return a list of ParameterMetadata representing the parameters of this method.
     */
    List<ParameterMetadata> getParameters();

    /**
     * Return the type parameters of this methods.
     * For exmaple, method <code>&lt;X> X get( String key , Class&lt;X> type);</code>
     * will have one type parameter with name 'X'.
     *
     * @return a List of TypeParameters.
     */
    List<TypeParameter> getTypeParameters();
    
    /**
     * For annotation interface methods only (attributes), retrieve the default value of the annotation
     * attribute represented by this method metadata.
     * <p>
     * For attributes of type Class, a ClassMetadata object is returned.<br>
     * For attributes of type Enum, an EnumValue object is returned.<br>
     * For attributes of any annotation type, an AnnotationMetadata object is returned.<br>
     * For attributes of a primitive or String type, the actual value is returned.<br>
     * For attributes of any array type, an array of any of the above is returned.<br>
     * 
     *
     * @return the annotation attribute's default value, or null if this method metadata does
     * not represent an annotation attribute method or there is not default value for it.
     */
    Object getDefault();
    
    /**
     * @return the ClassMetadata of the class declaring this method.
     */
    ClassMetadata getParent();
    
}
