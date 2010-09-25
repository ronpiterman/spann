
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

package com.masetta.spann.metadata.visitors;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.TypeReference;
public class TypeReferenceImpl implements TypeReference {
    
    private ClassMetadata type;

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getType() {
        return type;
    }

    void setType(ClassMetadata type) {
        this.type = type;
    }

}
