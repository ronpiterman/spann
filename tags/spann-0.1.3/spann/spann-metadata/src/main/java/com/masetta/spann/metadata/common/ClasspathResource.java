
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

package com.masetta.spann.metadata.common;

import java.net.URL;

/**
 * Implementation of Resource of resources specified by a path on the classpath.
 * @author Ron Piterman
 *
 */
public class ClasspathResource implements Resource {
    
    private final String path;
    
    private final ClassLoader classLoader;

    /**
     * <p>Constructor for ClasspathResource.</p>
     *
     * @param path the path to the resource.
     * @param classLoader the ClassLoader that should be used to access the resource and to load
     * 		the underlying class.
     */
    public ClasspathResource(String path, ClassLoader classLoader) {
        super();
        this.path = path;
        this.classLoader = classLoader;
    }

    /**
     * Retrieve the path to the resource.
     */
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * {@inheritDoc}
     */
    public URL toUrl() {
		return this.classLoader.getResource( path );
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return getClass().getSimpleName() + " [path=" + path + "]";
	}

	/**
	 * <p>forClassname</p>
	 *
	 * @param classname a {@link java.lang.String} object.
	 * @param clsLoader a {@link java.lang.ClassLoader} object.
	 * @return a {@link com.masetta.spann.metadata.common.Resource} object.
	 */
	public static Resource forClassname(String classname, ClassLoader clsLoader) {
		return new ClasspathResource( 
				ResourceUtil.convertClassNameToPath( classname ) + ".class" , clsLoader );
	}
	
}
