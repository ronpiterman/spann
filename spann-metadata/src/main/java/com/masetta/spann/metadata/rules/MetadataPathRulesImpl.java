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

import java.util.ArrayList;
import java.util.List;

import com.masetta.spann.metadata.core.ArtifactPath;

public class MetadataPathRulesImpl implements MetadataPathRules {
    
    private final List<MetadataPathRule> rules;
    
    public MetadataPathRulesImpl(List<MetadataPathRule> rules) {
        super();
        this.rules = new ArrayList<MetadataPathRule>(rules);
    }

    public int getElements( ArtifactPath path ) {
        int elements = 0;
        for ( MetadataPathRule rule : rules ) {
            elements = elements | rule.collect( elements, path );
        }
        return elements;
    }

}
