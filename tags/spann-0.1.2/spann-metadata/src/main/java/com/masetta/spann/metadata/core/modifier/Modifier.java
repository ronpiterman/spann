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

package com.masetta.spann.metadata.core.modifier;
/**
 * Base interface for Modifier interfaces ( ClassModifier, FieldModifier and MethodModifier)
 * 
 * @author Ron Piterman
 */
public interface Modifier {

	/**
	 * Retrieve the 'static' modifier of the artifact.
	 * 
	 * @return true if the artifact is static.
	 */
    boolean isStatic();
    
    /**
	 * Retrieve the 'final' modifier of the artifact.
	 * 
	 * @return true if the artifact is final.
	 */
    boolean isFinal();
    
    /**
	 * Retrieve the 'synthetic' modifier of the artifact.
	 * 
	 * @return true if the artifact is synthetic.
	 */
    boolean isSynthetic();
    
    /**
	 * Retrieve the Access level of the artifact.
	 * 
	 * @return the Access enum representing the access level of the artifact.
	 */
    Access getAccess();

}
