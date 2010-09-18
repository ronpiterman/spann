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

import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;

public interface LazyLoadingRulesFactory {
    
    /**
     * Create rules for lazy loading the given element for the given path.
     * 
     * @param path the path of the metadata which is requesting to lazy load the given element.
     * @param element the element of the last artifact in the path which is requested to load.
     * @return
     */
    MetadataPathRules createRules( ArtifactPath path , ArtifactElement element );

}
