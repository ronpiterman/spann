
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

package com.masetta.spann.metadata.core.modifier;
public enum ClassType {
    
    CLASS,
    
    INTERFACE,
    
    ENUM,
    
    ANNOTATION;
    
    /**
     * <p>isInterface</p>
     *
     * @return a boolean.
     */
    public boolean isInterface() {
        return equals( INTERFACE );
    }
    
    /**
     * <p>isEnum</p>
     *
     * @return a boolean.
     */
    public boolean isEnum() {
        return equals( ENUM );
    }
    
    /**
     * <p>isAnnotation</p>
     *
     * @return a boolean.
     */
    public boolean isAnnotation() {
        return equals( ANNOTATION );
    }
    
    /**
     * <p>isClass</p>
     *
     * @return a boolean.
     */
    public boolean isClass() {
        return equals( CLASS );
    }

}
