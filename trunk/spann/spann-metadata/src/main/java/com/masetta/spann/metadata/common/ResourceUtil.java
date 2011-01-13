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

/**
 * Resource-path to class-name conversion and vice versa.
 * 
 * @author Ron Piterman
 */
public final class ResourceUtil {
    
    private ResourceUtil() {}
    
    /**
     * Convert the given path to a class name.
     * 
     * @param path a resource path
     * @return
     */
    public static String convertResourcePathToClassName(String path) {
        return path.replaceAll( "/" , "." );
    }
    
    public static String convertClassNameToPath(String className) {
        return className.replaceAll( "\\.", "/" );
    }

}
