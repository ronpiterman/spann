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
 * Represents metadata of an annotation proxy.
 * <p>
 * A Main difference of AnnotationMetadata from the java API is that it can differentiate between
 * annotation attributes which are explicitly set and those that are implicitly set by the
 * annotation interface's default value.
 * 
 * @author Ron Piterman
 */
public interface AnnotationMetadata extends Metadata {
    
    /**
     * Retrieve an annotation attribute value.
     * 
     * The returned object is one of 
     * <ul>
     *  <li>any primitive type (int, float, booelan etc.)</li>
     *  <li>a String</li>
     *  <li>a {@link ClassMetadata}</li>
     *  <li>an {@link AnnotationMetadata}</li>
     *  <li>an {@link EnumValue} </li>
     *  <li>a one dimensional array of any of the above types</li>
     *  <li>null, if 'nullsafe' is false or lazy loading is not active</li>
     * </ul>
     * 
     * <b>Note about empty arrays:</b> if the value of the attribute is an
     * empty array, an empty Object array may be returned for certain attribute
     * types (annotations), regardless of the type of the annotation attribute. 
     * If the array is not empty, it can be safely cast to the respective
     * attribute type (for example <code>(EnumValue[])value</code> ).
     * <p>
     * For type safety handling use 
     * {@link #getAttribute(Class, String, boolean)}.
     * <p>
     * <b>Note about default values:</b> default attribute values may be null (also if given 
     * nullsafe is 'true') if lazy loading is not active.
     * 
     * @param name The name of the attribute to retrieve
     * @param nullsafe If false, only explicitly set attributes values are
     *      returned. If true, the attribute's default value is retrieved for
     *   any attribute which is not explicitly set. Default attribute values
     *   may be falsely null if lazy loading is not active.
     * @return the attribute value.
     * @throws IllegalArgumentException if nullsafe is true but the attribute
     *         does not exist, an IllegalArgumentException is thrown.
     */
    Object getAttribute( String name , boolean nullsafe );
    
    /**
     * Same as {@link #getAttribute(String, boolean)}, but casts the result 
     * to the given type.
     * 
     * If the result is an empty array it will be converted to the given array type.
     * 
     * @param <T> one of the supported types. see {@link #getAttribute(String, boolean)}.
     * @param type class of one of the supported types. see {@link #getAttribute(String, boolean)}
     * @param name attribute name.
     * @param nullsafe if to retrieve default value for attributes which are not explicitly set.
     * 
     * @return The value of the attribute cast to the given type.
     */
    <T> T getAttribute( Class<T> type, String name , boolean nullsafe );
    
    /**
     * Retrieve the metadata of the element which is annotated by this annotation.
     * This can be a ClassMetadata, FieldMetadata, MethodMetadata or ParameterMetadata.
     * <p>
     * May return null if this annotation represents an annotation attribute value.
     */
    AnnotatedElementMetadata getAnnotatedElement();
    
    /** Retrieve the type of the annotation */
    ClassMetadata getType();
    
}
