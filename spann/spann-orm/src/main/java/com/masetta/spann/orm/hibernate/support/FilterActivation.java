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

package com.masetta.spann.orm.hibernate.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FilterActivation implements Serializable {
	
	private static final long serialVersionUID = -4281014324370708351L;

	private String name;
	
	private Map<String,Object> parameters;
	
	public FilterActivation( String name , Object ...parameters ) {
		this.name = name;
		for ( int i = 0; i < parameters.length; ) {
			setParameter( (String)parameters[i++], parameters[i++] );
		}
	}
	
	public FilterActivation setParameter( String name , Object value ) {
		if ( this.parameters == null ) {
			this.parameters = new HashMap<String, Object>();
		}
		this.parameters.put( name, value );
		return this;
	}

	public String getName() {
		return name;
	}
	
	public Collection<Map.Entry<String,Object>> getParameters() {
		if ( this.parameters == null )
			return Collections.emptySet();
		else
			return parameters.entrySet();
	}

	@Override
	public String toString() {
		return "FilterActivation [name=" + name + ", parameters=" + parameters + "]";
	}
	
}
