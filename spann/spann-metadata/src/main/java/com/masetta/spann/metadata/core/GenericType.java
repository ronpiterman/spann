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
 * A generic type, used to represent one of: (1) method return type (2)
 * method parameters, or (3) field's type.
 * <p>
 * A Generic type points to either a context bound type parameter or to a
 * class/type metadata. For example:
 * <ul>
 * <li>
 * A context bound type parameter, as the type of field <code>bar</code> in: <br>
 * <code><pre>
 * public class Foo&lt;T> {
 *   private T bar;
 *   ...
 * }</code></pre></li>
 * <li>A class/type metadata, as the field <code>bars</code> in: <br>
 * <code><pre>
 * public class Foo&lt;T> {
 *   private List&lt;T> bars;
 *      ...
 * }
 * </pre></code>
 * </li></ul>
 * Note that only one of {@link #getContextBoundTypeParameter()} and {@link #getType()}
 * will be set / return a non-null value.
 * 
 * @author Ron Piterman    
 * 
 */
public interface GenericType extends TypeReference {

    /**
     * A context bound type parameter, as the type of field <code>bar</code> in: <br>
     * <code><pre>
     * public class Foo&lt;T> {
     *   private T bar;
     *   ...
     * }</code></pre>
     *
     * Mutually exclusive with {@link #getType()}
     */
    String getContextBoundTypeParameter();

    /**
     * A class/type metadata. <p>
     * E.g., the {@link FieldMetadata#getFieldType() fieldType} of the field <code>bars</code> in 
     * the following example will have a type of <code>List&lt;T></code><br>
     * <code><pre>
     * public class Foo&lt;T> {
     *   private List&lt;T> bars;
     *      ...
     * }
     * </pre></code>
     * 
     * Mutually exclusive with {@link #getContextBoundTypeParameter()}.
     */
    ClassMetadata getType();

}
