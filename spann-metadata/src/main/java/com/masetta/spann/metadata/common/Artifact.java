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

package com.masetta.spann.metadata.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents different metadata artifacts, and specifies the different elements
 * each metadata artifacts supports.
 * 
 * @author Ron Piterman    
 */
public enum Artifact {
    
    /** Class metadata */
    CLASS( ArtifactElement.SELF, ArtifactElement.SIGNATURE, 
            ArtifactElement.FIELDS , ArtifactElement.METHODS , ArtifactElement.ANNOTATIONS ),
        
    /** Field metadata */
    FIELD( ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS ),
    
    /** Method metadata */
    METHOD( ArtifactElement.SIGNATURE , ArtifactElement.ANNOTATIONS , ArtifactElement.PARAMETER_ANNOTATIONS , 
            ArtifactElement.DEFAULT_VALUE ),
    
    PARAMETER(),
    
    ANNOTATION(),
    
    /**
     * Is used by spann-spring, not used by spann-metadata
     */
    UNDEFINED(),
    
    /**
     * Is used by spann-spring, not used by spann-metadata
     */
    UNKNOWN();
    
    private final Set<ArtifactElement> supports;
    
    private Artifact( ArtifactElement ...supportedElements ) {
        this.supports = Collections.unmodifiableSet( new HashSet<ArtifactElement>( 
                Arrays.asList( supportedElements ) ) );
    }

    public Set<ArtifactElement> getSupports() {
        return supports;
    }

    public boolean isSupports(ArtifactElement element) {
        return supports.contains( element );
    }
    
}
