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

/**
 * Common super interrface for metadata elements which support annotaions:
 * {@link ClassMetadata}, {@link MethodMetadata}, {@link FieldMetadata} and {@link ParameterMetadata}.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public interface AnnotatedElementMetadata extends Metadata {

    /**
     * <p>getAnnotation</p>
     *
     * @param cannonicalClassName a {@link java.lang.String} object.
     * @return a {@link com.masetta.spann.metadata.core.AnnotationMetadata} object.
     */
    AnnotationMetadata getAnnotation( String cannonicalClassName );
    
    /**
     * Retrieve all paths to the given (meta-) annotation ordered by path length (smaller first).
     *
     * @param annotationCannonicalClassName annotation to find the paths to.
     * @return all paths to the given (meta-) annotation.
     * @see AnnotationPath
     */
    List<AnnotationPath> findAnnotationPaths( String annotationCannonicalClassName );
    
}
