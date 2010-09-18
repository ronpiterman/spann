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

import java.net.URL;


public class ClasspathResource implements Resource {
    
    private final String path;
    
    private final ClassLoader classLoader;

    public ClasspathResource(String path, ClassLoader classLoader) {
        super();
        this.path = path;
        this.classLoader = classLoader;
    }

    public String getPath() {
        return path;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public URL toUrl() {
		return this.classLoader.getResource( path );
	}

	public String toString() {
		return getClass().getSimpleName() + " [path=" + path + "]";
	}

	public static Resource forClassname(String classname, ClassLoader clsLoader) {
		return new ClasspathResource( 
				ResourceUtil.convertClassNameToPath( classname ) + ".class" , clsLoader );
	}
	
}
