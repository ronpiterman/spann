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

package com.masetta.spann.metadata.rules;

import com.masetta.spann.metadata.core.ArtifactPath;

/**
 * Metadata path based rules engine to determine which artifact elements
 * are to be visited (and which metadata objects should be created).
 * 
 * @see MetadataPathRulesBuilder
 * 
 * @author Ron Piterman    
 */
public interface MetadataPathRules {
    
    /**
     * Retrieve the set of elements which need to be visited for the given path.
     * 
     * The elements are sub elements of the last path artifact.
     * 
     * @param path the current path
     * 
     * @return the 'ored' bits of all elements of the last artifact in the path
     *     which should be visited according to this rules object. 
     */
    int getElements( ArtifactPath path );

}
