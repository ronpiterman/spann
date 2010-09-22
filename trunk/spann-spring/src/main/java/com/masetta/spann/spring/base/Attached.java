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

package com.masetta.spann.spring.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.metadata.common.Artifact;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Attached {
	
	String SCOPE_ATTRIBUTE = "scope";
	String ROLE_ATTRIBTUE = "role";
	
	/**
	 * The attach scope. UNDEFINED ( the default ) represents the global scope.
	 * UNKNOWN represents the current metadata scope.
	 */
	Artifact scope() default Artifact.UNDEFINED;
	
	String role() default "main";

}
