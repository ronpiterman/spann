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

import java.util.Collection;

/** 
 * Represents different elements of metadata types.
 * 
 * The exact semantics of each element changes according to the artifact it is applies to.
 *  
 * 
 */
public enum ArtifactElement {
    
    
    /** Classes in a package */
    CLASSES,
    
    /** Basic class information */
    SELF,
    
    /** Full generic type information of class, methods and fields */
    SIGNATURE,

    /** Annotations metadata of class, methods and fields */
    ANNOTATIONS,
    
    /** Annotations metadata of method parameters */
    PARAMETER_ANNOTATIONS,
    
    /** Fields of a class */
    FIELDS,
    
    /** Methods of a class */
    METHODS,
    
    /** Method default value (annotation interfaces only) */
    DEFAULT_VALUE;
    
    private final int bit;
    
    private ArtifactElement() {
        this.bit = 1 << ordinal();
    }

    public int getBit() {
        return bit;
    }
    
    public static int collect( ArtifactElement ...elements ) {
        int bit = 0;
        for ( ArtifactElement ae : elements ) {
            bit = bit | ae.getBit();
        }
        return bit;
    }
    
    public static int collect( Collection<ArtifactElement> elements ) {
        int bit = 0;
        for ( ArtifactElement ae : elements ) {
            bit = bit | ae.getBit();
        }
        return bit;
    }

}
