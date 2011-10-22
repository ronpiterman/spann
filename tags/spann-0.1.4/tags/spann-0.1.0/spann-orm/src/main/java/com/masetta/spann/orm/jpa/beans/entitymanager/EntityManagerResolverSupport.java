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

package com.masetta.spann.orm.jpa.beans.entitymanager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.ApplicationContext;

import com.masetta.spann.spring.util.Resolver;

/**
 * Utility class to try to locate an EntityManager in the application context.
 * 
 * @author Ron Piterman
 *
 */
public final class EntityManagerResolverSupport  {
	
	private EntityManagerResolverSupport() {}
	
	/**
	 * Create a factory for an entity manager found in the given ApplicationContext.
	 * <p>
	 * This method searches the given applicationContext for a <b>single</b> bean of a given type.
	 * The first one which is found is used in this order: {@link EntityManagerProvider}, 
	 * EntityManager, EntityManagerFactory.
	 * 
	 * @param applicationContext
	 * @return
	 */
	public static Resolver<EntityManager,String> getEntityManagerResolver( ApplicationContext applicationContext ) {
		EntityManagerProvider ems = getBeanOfType( EntityManagerProvider.class , applicationContext );
		if ( ems != null )
			return ems;
		EntityManager em = getBeanOfType( EntityManager.class , applicationContext );
		if ( em != null )
			return new EntityManagerAdapter( em );
		
		EntityManagerFactory emf = getBeanOfType( EntityManagerFactory.class , applicationContext );
		if ( emf != null )
			return new EntityManagerFactoryAdapter( emf );
		throw new IllegalArgumentException( "No Entity Manager found. Need a unique bean of one of the types " +
				"EntityManagerProvider, EntityManagerFactory or EntityManager.");
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getBeanOfType( Class<T> cls , ApplicationContext ctx ) {
		String[] beanNames = ctx.getBeanNamesForType( cls );
		if ( beanNames.length != 1 )
			return null;
		return (T) ctx.getBean( beanNames[0] );
	}
	
	private static class EntityManagerAdapter implements Resolver<EntityManager,String> {
		private final EntityManager entityManager;
		public EntityManagerAdapter(
				EntityManager entityManager) {
			super();
			this.entityManager = entityManager;
		}
		public EntityManager resolve(String param) {
			return entityManager;
		}
	}

	
	private static class EntityManagerFactoryAdapter implements Resolver<EntityManager,String> {
		private final EntityManagerFactory entityManagerFactory;
		public EntityManagerFactoryAdapter(
				EntityManagerFactory entityManagerFactory) {
			super();
			this.entityManagerFactory = entityManagerFactory;
		}
		public EntityManager resolve(String param) {
			return entityManagerFactory.createEntityManager();
		}
	}

	
}
