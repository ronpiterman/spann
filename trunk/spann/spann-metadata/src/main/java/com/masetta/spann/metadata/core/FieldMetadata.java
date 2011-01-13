
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

package com.masetta.spann.metadata.core;

import com.masetta.spann.metadata.core.modifier.FieldModifier;

/**
 * Represents the metadata of a field.
 * 
 * @author Ron Piterman
 */
public interface FieldMetadata extends Metadata , AnnotatedElementMetadata {
    
    /**
     * Retrieve the type of the field.
     *
     * @return the field's type metadata. 
     * 
     * @see #getFieldType() for generic signature information
     */
    ClassMetadata getFieldClass();
    
    /**
     * Retrieve the generic type of the field.
     *
     * @return the generic signature of the field represented by this metadata object.
     */
    GenericType getFieldType();
    
    /**
     * Retrieve the field's modifier.
     *
     * @return the modifier of the field represented by this metadata object.
     */
    FieldModifier getModifier();

}
