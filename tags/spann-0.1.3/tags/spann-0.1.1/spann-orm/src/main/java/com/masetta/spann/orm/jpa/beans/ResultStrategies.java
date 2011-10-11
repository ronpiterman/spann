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

package com.masetta.spann.orm.jpa.beans;

import java.util.List;

import com.masetta.spann.spring.util.Resolver;

/**
 * Query command constants
 * @author Ron Piterman
 */
public final class ResultStrategies {
	
	private ResultStrategies() {}

	/**
	 * Performs a query as an update.
	 */
	public final static Resolver<Integer,QueryCallContext> UPDATE = new Resolver<Integer, QueryCallContext>() {
		public Integer resolve(QueryCallContext q) {
			return q.getQuery().executeUpdate();
		}
	};
	
	/**
	 * Perform a query as a finder.
	 */
	public final static Resolver<List<?>,QueryCallContext> FIND = new Resolver<List<?>, QueryCallContext>() {
		public List<?> resolve(QueryCallContext q) {
			return q.getQuery().getResultList();
		}
	};
	
	/**
	 * Performs a query as a finder, returning only the first result, if available, otherwise return null.
	 */
	public final static Resolver<Object,QueryCallContext> FIND_ONE = new Resolver<Object, QueryCallContext>() {
		public Object resolve(QueryCallContext q) {
			List<?> list = q.getQuery().getResultList();
			if ( list.isEmpty() )
				return null;
			return list.get( 0 );
		}
	};
	
	/**
	 * Performs a query as a unique result, using EntityManager.getSingleResult() method.
	 * This executor will 
	 */
	public final static Resolver<Object,QueryCallContext> GET = new Resolver<Object, QueryCallContext>() {
		public Object resolve(QueryCallContext q) {
			return q.getQuery().getSingleResult();
		}
	};
}
