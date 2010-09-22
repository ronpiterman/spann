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

package com.masetta.spann.metadata.util;

import java.util.Arrays;

/**
 * Simple equals builder for internal use.
 *
 * @author Ron Piterman
 * @version $Id: $
 */
public class EqualsBuilder {
    
    private boolean equals = true;
    
    /**
     * <p>eq</p>
     *
     * @param o1 a {@link java.lang.Object} object.
     * @param o2 a {@link java.lang.Object} object.
     * @return a boolean.
     */
    public boolean eq( Object o1 , Object o2 ) {
        return eq( o1 == null ? o2 == null : o1.equals( o2 ) );
    }
    
    /**
     * <p>eq</p>
     *
     * @param o1 an array of {@link java.lang.Object} objects.
     * @param o2 an array of {@link java.lang.Object} objects.
     * @return a boolean.
     */
    public boolean eq( Object[] o1 , Object[] o2 ) {
        return eq( Arrays.equals( o1, o2 ) );
    }
    
    private boolean eq( boolean eq ) {
        this.equals = eq;
        return this.equals;
    }
    
}
