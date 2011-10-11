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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;

import com.masetta.spann.spring.util.Factory;

public class HibernateSupport {
	
	public static <T> T doWithFilters( EntityManager entityManager , Collection<FilterActivation>
		filters , Factory<T> callback ) {
		
		if ( filters == null || filters.isEmpty() ) {
			return callback.create();
		}
		
		Session session = (Session) entityManager.getDelegate();
		List<String> list = enableFilters( session, filters );
		try {
			return callback.create();
		}
		finally {
			disableFilters( session, list );
		}
	}
	
	private static void disableFilters(Session session, List<String> filters ) {
		for ( String filter : filters ) {
			session.disableFilter( filter );
		}
	}
	
	private static List<String> enableFilters( Session session, Collection<FilterActivation> 
		filterList ) {
		
		if ( filterList == null || filterList.isEmpty() ) 
			return Collections.emptyList();
		
		List<String> applied = new ArrayList<String>();
		Filter filter;
		for ( FilterActivation f : filterList ) {
			String filterName = f.getName();

			Collection<Map.Entry<String, Object>> parameters = f.getParameters();
			// skip filters which are already enabled,
			filter = session.getEnabledFilter( filterName );
			if ( filter != null ) {
				if ( f.getParameters().size() > 0 ) {
					throw new IllegalArgumentException("Filter '" + f.getName()+ "' is already activated. Can not enable filter with different arguments.");
				}
				continue;
			}
			
			filter = session.enableFilter( filterName );
			applied.add( filterName );
			for ( Map.Entry<String,Object> e : parameters ) {
				Object value = e.getValue();
				if ( value == null ) {
					filter.setParameter( e.getKey() , value );
				}
				else if ( value instanceof Collection ) {
					filter.setParameterList( e.getKey(), (Collection)value);
				}
				else if ( value.getClass().isArray() ) {
					filter.setParameterList( e.getKey() , (Object[])value );
				}
				else {
					filter.setParameter( e.getKey(), value );
				}
			}
			filter.validate();
		}
		return applied;
	}

}
