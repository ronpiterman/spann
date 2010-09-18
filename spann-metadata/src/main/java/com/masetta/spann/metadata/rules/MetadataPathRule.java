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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.util.Matcher;


public class MetadataPathRule {
    
    private final Map<Artifact,List<Matcher<Metadata>>> path;
    
    private final int elements;
    
    public MetadataPathRule(Map<Artifact, List<Matcher<Metadata>>> path,
            Collection<ArtifactElement> elements) {
        super();
        this.path = path;
        int e = 0;
        for ( ArtifactElement ae : elements  )
            e = e | ae.getBit();
        this.elements = e;
    }

    public int collect( int current, ArtifactPath artifactPath ) {
        if ( path.size() != artifactPath.size() )
            return 0;
        if ( (current & elements) == elements )
            return 0;
        
        for ( Metadata m : artifactPath ) {
            List<Matcher<Metadata>>  matcher = path.get( m.getArtifact() );
            if ( matcher == null )
                return 0;
            if ( matcher.isEmpty() ) 
                continue;
            if ( ! matches( matcher , m ) )
                return 0;
        }
        return elements;
    }

    private boolean matches(List<Matcher<Metadata>> matcher, Metadata metadata) {
        for ( Matcher<Metadata> m : matcher ) {
            if ( m.matches( metadata ) )
                return true;
        }
        return false;
    }
    
    
}
