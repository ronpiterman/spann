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
public final class Equals {
	
	private Equals() {}
    
    /**
     * @param o1 Object 1
     * @param o2 Object 2
     * @return true if both are null or equal.
     */
    public static boolean eq( Object o1 , Object o2 ) {
        return  o1 == null ? o2 == null : o1.equals( o2 );
    }
    
    /**
     * @param o1 array 1
     * @param o2 array 2
     * @return true if both arrays are equal.
     */
    public static boolean eq( Object[] o1 , Object[] o2 ) {
        return Arrays.equals( o1, o2 );
    }
    
}
