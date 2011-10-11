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

import com.masetta.spann.metadata.common.Artifact;

/**
 * Base interface for metadata artifacts.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public interface Metadata {
    
    /**
     * The artifact path of this element
     *
     * @return a {@link com.masetta.spann.metadata.core.ArtifactPath} object.
     * @see ArtifactPath
     */
    ArtifactPath getPath();
    
    /**
     * The type of artifact this metadata represents
     *
     * @return a {@link com.masetta.spann.metadata.common.Artifact} object.
     */
    Artifact getArtifact();
    
    /**
     * Retrieve the name of the artifact.
     * For example: full qualified name of class, name of field or method, etc.
     *
     * @return the name of this metadata artifact.
     */
    String getName();
    
    /**
     * Retrieve the parent metadata of this metadata.
     * For exmaple, the metadata of the class to which a method belongs.
     *
     * @return the parent metadata of this metadata or null, if this metadata
     * 	is a top-level class metadata.
     */
    Metadata getParent();
    
}
