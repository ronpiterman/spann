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

package com.masetta.spann.orm.hibernate.chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.masetta.spann.orm.hibernate.support.FilterActivation;
import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupport;

/**
 * A FilterEnabler which uses a pre-set FilterActivation list.
 * The list contains as filter-parameter-values method argument indexes.
 * 
 * The actual values are extracted at runtime according to the specified
 * indexes.
 *  
 * @author Ron Piterman
 */
public class DynamicFilterEnabler extends AbstractFilterEnabler {

	private Collection<FilterActivation> filters;

	public DynamicFilterEnabler(EntityManagerSupport entityManagerSupport, 
			Collection<FilterActivation> filters ) {
		super(entityManagerSupport);
		this.filters = filters;
	}

	@Override
	protected Collection<FilterActivation> getFilterActivations(QueryCallContext param) {
		Collection<FilterActivation> activations = new ArrayList<FilterActivation>( 
				this.filters.size() );
		Object value;
		for ( FilterActivation fa : this.filters ) {
			FilterActivation newFa = new FilterActivation( fa.getName() );
			for ( Map.Entry<String, Object> e : fa.getParameters() ) {
				value = param.getArgument( (Integer) e.getValue() );
				newFa.setParameter( e.getKey(), value );
			}
			activations.add( newFa );
		}
		
		return activations;
	}

}
