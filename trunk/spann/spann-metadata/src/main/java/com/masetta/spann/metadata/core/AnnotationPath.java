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
 * A path of annotations from an annotated element to a (meta-)annotation.
 * <p>
 * Meta-Annotation is an annotation of an annotation interface, see example in {@link #getPath()}.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public interface AnnotationPath {
    
    /**
     * Returns the path to the annotation.
     *
     * <p>
     * For example:
     * </p>
     * <pre>
     * &#64;interface AnnA { String value(); }
     *
     * &#64;AnnA("foo")
     * &#64;interface AnnB {}
     *
     * &#64;AnnB
     * &#64;interface AnnC {}
     *
     * &#64;AnnC()
     * class Foo {...}
     * </pre>
     * <p>
     * The path from Foo to AnnA will be: <br/>
     *
     * [AnnA, AnnB, AnnC]
     * </p>
     *
     * @return array of AnnotationMetadata, first element is the target annotation,
     * last is an annotation on the source AnnotatedElement.
     */
    AnnotationMetadata[] getPath();
    
    /**
     * Convenience method to retrieve the annotation attribute of the n'th element with in the path.
     *
     * @param index 0 based index of the annotation in the path. Note that the first element (0)
     *         is the target annotation of the path, the last element is the
     * @param name attribute name
     * @param nullsafe if to retrieve default value if attribute is not explicitly set.
     * @return Attribute value.
     * @see AnnotationMetadata#getAttribute(String, boolean)
     */
    Object getAttribute( int index , String name , boolean nullsafe );
    
    /**
     * Convenience method to retrieve the annotation attribute of the n'th element with in the path.
     *
     * @param index 0 based index of the annotation in the path. Note that the first element (0)
     *         is the target annotation of the path, the last element is the
     * @param name attribute name
     * @param nullsafe if to retrieve default value if attribute is not explicitly set.
     * @return Attribute value.
     * @see AnnotationMetadata#getAttribute(String, boolean)
     * @param type a {@link java.lang.Class} object.
     * @param <T> a T object.
     */
    <T> T getAttribute( int index , Class<T> type , String name , boolean nullsafe );
    
}
