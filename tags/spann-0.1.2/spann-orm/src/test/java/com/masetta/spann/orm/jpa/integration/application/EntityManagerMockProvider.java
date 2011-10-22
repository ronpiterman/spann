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

package com.masetta.spann.orm.jpa.integration.application;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerProvider;

@Component
public class EntityManagerMockProvider implements EntityManagerProvider {
	
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager resolve(String persisteceUnitName) {
		return entityManager;
	}
	

}
