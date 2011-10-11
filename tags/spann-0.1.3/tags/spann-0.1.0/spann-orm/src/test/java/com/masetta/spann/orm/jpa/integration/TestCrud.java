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

package com.masetta.spann.orm.jpa.integration;


import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoByMethodName;

public class TestCrud extends BaseIntegrationTest {
	
	protected AuthorDaoByMethodName authorDaoByMethodName;
	
	@Test
	public void testPersist() {
		Author a = new Author();
		
		entityManager.persist( a );
		EasyMock.expectLastCall();
		replay();
		
		authorDaoByMethodName.persist(a);
		
		verify();
	}
	
	@Test
	public void testMerge() {
		Author a = new Author();
		
		Author a2 = new Author(); 
		
		entityManager.merge( a );
		EasyMock.expectLastCall().andReturn( a2 );
		replay();
		
		Author a3 = authorDaoByMethodName.merge(a);
		Assert.assertSame( a3 , a2 );
		
		verify();
	}
	
	@Test
	public void testRemove() {
		Author a = new Author();
		
		entityManager.remove( a );
		EasyMock.expectLastCall();
		replay();
		
		authorDaoByMethodName.remove(a);
		
		verify();
	}
	
	@Test
	public void testRefresh() {
		Author a = new Author();
		
		entityManager.refresh( a );
		EasyMock.expectLastCall();
		replay();
		
		authorDaoByMethodName.refresh(a);
		
		verify();
	}
	
	@Test
	public void testFind() {
		Author a = new Author();
		entityManager.find( Author.class , 15l );
		EasyMock.expectLastCall().andReturn( a );
		replay();
		
		Author a2 = authorDaoByMethodName.find( 15l );
		
		Assert.assertSame( a2 , a );
		
		verify();
	}

	public void setAuthorDao1(AuthorDaoByMethodName authorDaoByMethodName) {
		this.authorDaoByMethodName = authorDaoByMethodName;
	}



}
