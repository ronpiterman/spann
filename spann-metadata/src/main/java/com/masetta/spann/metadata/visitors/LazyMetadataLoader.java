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

package com.masetta.spann.metadata.visitors;

import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.core.ArtifactPath;

interface LazyMetadataLoader {
    
    /**
     * <p>lazyload</p>
     *
     * @param classLoader a {@link java.lang.ClassLoader} object.
     * @param className a {@link java.lang.String} object.
     * @param path a {@link com.masetta.spann.metadata.core.ArtifactPath} object.
     * @param element a {@link com.masetta.spann.metadata.common.ArtifactElement} object.
     */
    void lazyload( ClassLoader classLoader, String className, ArtifactPath path , ArtifactElement element );

}
