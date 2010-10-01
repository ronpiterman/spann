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

package com.masetta.spann.metadata.core.support;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;

/**
 * Support methods for FieldMetadata.
 * 
 * @author Ron Piterman
 */
public abstract class FieldMetadataSupport {
	
	private FieldMetadataSupport() {
	}

	/**
	 * Find the given field in the given class.
	 * Superclass fields are not included in the search. 
	 * 
	 * @param cm class metadata
	 * @param fieldname field name to find
	 * @return the field with the given name of the given class, or null.
	 */
	public static FieldMetadata findField(ClassMetadata cm, String fieldname) {
		if ( cm.getFields() == null )
			return null;
		for (FieldMetadata fm : cm.getFields())
			if ( fm.getName().equals(fieldname) )
				return fm;
		return null;
	}

	

}
