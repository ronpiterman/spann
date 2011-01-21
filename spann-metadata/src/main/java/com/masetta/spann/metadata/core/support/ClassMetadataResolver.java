
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
 *
 * @author rpt
 * @version $Id: $
 */

package com.masetta.spann.metadata.core.support;

import com.masetta.spann.metadata.core.ClassMetadata;

/**
 * Used by ClassReaderAdapter to access class metadata, either already stored
 * or to be created.
 * <br>
 * Decouples ClassReaderAdapter implementations from the MetadataStore and ClassMetadataVisitor.
 * 
 * @author Ron Piterman
 */
public interface ClassMetadataResolver {
    
    /**
     * Retrieve the class metadata representing the given class with the
     * given dimensions.
     *
     * @param className the fq_name of the class.
     * @param dimensions the array dimensions of the class.
     * 
     * @return a ClassMetadata object representing the given class in the given dimensions.
     */
    ClassMetadata resolve( String className , int dimensions );

}
