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

import com.masetta.spann.spring.util.Resolver;

/**
 * Provider for an instance of the EntityManager.
 * <p>
 * Used to specify how to retrieve the current EntityManager.
 * <p>
 * If the application does not expose a single EntityManager or an EntityManagerFactory
 * instance as bean, it must provides an <b>single</b> implementation of this interface
 * to allow the DaoDelegate access to the entity manager.
 * 
 * @author Ron Piterman.
 */
public interface EntityManagerProvider extends Resolver<EntityManager,String> {

	 EntityManager resolve( String persisteceUnitName );
	
}
