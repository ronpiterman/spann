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

package com.masetta.spann.metadata.core;

/**
 * Reference to a ClassMetadata, used as super interface of different generic metadata interface.
 * 
 * @author Ron Piterman
 */
public interface TypeReference {
    
	/**
	 * Retrieve the class metadata this Object references. The exact semantic
	 * of the returned ClassMetadata is varying, depending on the sub-class extending this interface.
	 * 
	 * @return The ClassMetadata this object references.
	 */
    ClassMetadata getType();

}
