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

package com.masetta.spann.orm.jpa.beans.factories;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.context.ApplicationContextAware;

import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupport;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupportImpl;
import com.masetta.spann.spring.util.Resolver;

public abstract class AbstractQueryCallContextFactory implements Resolver<QueryCallContext,Object[]> {
	
	public static final String QUERY_PROPERTY = "query";
	
	public static final String ENTITY_MANAGER_SUPPORT_PROPERTY = "entityManagerSupport";
	
	private EntityManagerSupport entityManagerSupport;
	
	private String query;
	
	public QueryCallContext resolve(Object[] param) {
		return new QueryCallContext( createQuery( getEntityManager() , getQuery( param ), param ) , param  );
	}

	protected abstract Query createQuery(EntityManager em, String query, Object[] param);

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery( Object[] param ) {
		return query;
	}
	
	protected final EntityManager getEntityManager() {
		return this.entityManagerSupport.getEntityManager();
	}
	
	public void setEntityManagerSupport(EntityManagerSupport entityManagerSupport) {
		this.entityManagerSupport = entityManagerSupport;
	}

}
