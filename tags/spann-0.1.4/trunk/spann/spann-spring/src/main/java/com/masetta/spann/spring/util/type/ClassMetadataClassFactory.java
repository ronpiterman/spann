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

package com.masetta.spann.spring.util.type;


import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.spring.util.Factory;

public class ClassMetadataClassFactory implements Factory<Class<?>> {
	
	private final ClassMetadata classMetadata;
	
	public ClassMetadataClassFactory( ClassMetadata cm ) {
		this.classMetadata = cm;
	}

	public Class<?> create() {
		try {
			return classMetadata.getClassLoader().loadClass( classMetadata.getName() );
		}
		catch ( ClassNotFoundException ex ) {
			throw new RuntimeException( ex );
		}
	}

}
