
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

package com.masetta.spann.metadata.rules;

import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.util.Matcher;
public class MetadataNameMatcher implements Matcher<Metadata> {
    
    private String name;
    
    /**
     * <p>Constructor for MetadataNameMatcher.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public MetadataNameMatcher(String name) {
        super();
        this.name = name;
    }

    /**
     * <p>matches</p>
     *
     * @param metadata a {@link com.masetta.spann.metadata.core.Metadata} object.
     * @return a boolean.
     */
    public boolean matches(Metadata metadata) {
        return name.equals( metadata.getName() );
    }

    /**
     * <p>isStatic</p>
     *
     * @return a boolean.
     */
    public boolean isStatic() {
        return true;
    }

}
