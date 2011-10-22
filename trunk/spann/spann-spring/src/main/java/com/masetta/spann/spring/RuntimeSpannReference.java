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

package com.masetta.spann.spring;


import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * Similar to Spring's RuntimeBeanReference, only is indicating scope and role.
 * <p>
 * The reference is resolved after the scan and before registering the beans
 * in spring.
 * <p>
 * It allows deferring attached bean resolution to avoid visitor order conflicts.
 * <p>
 * If no bean is attached to the given metadata in the given scope and role, an 
 * {@link IllegalConfigurationException} is thrown, unless the reference is optional.
 *  
 * @author Ron Piterman
 */
public class RuntimeSpannReference {
	
	private final Metadata metadata;
	
	private final Artifact scope;
	
	private final String role;
	
	private final boolean optional;

	public boolean isOptional() {
		return optional;
	}

	public RuntimeSpannReference(Metadata metadata, Artifact scope, String role) {
		this( metadata , scope , role, false );
	}
	
	public RuntimeSpannReference(Metadata metadata, Artifact scope, String role, boolean optional ) {
		super();
		this.metadata = metadata;
		this.scope = scope;
		this.role = role;
		this.optional = optional;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public Artifact getScope() {
		return scope;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "RuntimeSpannReference [metadata=" + metadata + ", scope=" + scope + ", role="
				+ role + ", optional=" + optional + "]";
	}
	
}
