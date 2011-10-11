
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
 * Implementation of {@link Resource} specifying a Resource on the class path by its URL.
 * 
 * @author Ron Piterman
 */
public class UrlResource implements Resource {
	
	private final ClassLoader classLoader;
	
	private final URL url;
	
	/**
	 * <p>Constructor for UrlResource.</p>
	 *
	 * @param url the URL to the resource.
	 * @param classLoader the ClassLoader that should be used to load the given URL.
	 */
	public UrlResource(URL url, ClassLoader classLoader) {
		super();
		this.url = url;
		this.classLoader = classLoader;
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
		return url;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "UrlResource [url=" + url + "]";
	}

}
