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

package com.masetta.spann.spring;

import com.masetta.spann.metadata.core.Metadata;


/**
 * Visitor for ClassMetadata.
 * 
 * @author Ron Piterman    
 */
public interface MetadataVisitor<T extends Metadata> {
    
    /**
     * Visit the given metadata.
     * <p>
     * Optionally create bean definitions or manipulate existing definitions.
     * 
     * @param metadata
     * @param context
     */
    void visit( T metadata , ScanContext context );
    
    /**
     * Retrun true if this visitor supports the given metadata's type;
     * 
     * @param metadata
     * @return true if this visitor supports the given metadata (by type).
     */
    <M extends Metadata> boolean supports( M metadata );
    

}
