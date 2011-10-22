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

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoJpqlWithCount;
import com.masetta.spann.orm.jpa.support.QueryCount;

public class TestCount extends BaseIntegrationTest {
	
	protected AuthorDaoJpqlWithCount authorDaoJpqlWithCount;
	
	//	@Jpql("FROM Author WHERE foo = ?")
	//	@Count
	//	public List<Author> findByFoo( String foo , QueryCount c );
	@Test
	public void testSimple() {
		List<Author> expected = Arrays.asList( new Author("a"), new Author("b"));
		QueryCountStab c = new QueryCountStab();
		expectCountPoisitional( "SELECT count(*) FROM Author WHERE foo = ?" , null, 5, "name");
		expectJpqlPositional( "FROM Author WHERE foo = ?" , null, expected, "name" );
		replay();
		
		List<Author> result = authorDaoJpqlWithCount.findByFoo( "name", c );
		Assert.assertSame( result, expected );
		Assert.assertEquals( c.getCount().intValue(), 5 );
		
		verify();
		
	}
	
	//	
	//	@Jpql("SELECT id FROM Author WHERE foo = ? ORDER BY {0}")
	//	@Count
	//	public List<Author> findByFooOrder( String order , String foo , QueryCount c );
	@Test
	public void testWithMessageFormat() {
		List<Author> expected = Arrays.asList( new Author("a"), new Author("b"));
		QueryCountStab c = new QueryCountStab();
		expectCountPoisitional( "SELECT count(id) FROM Author WHERE foo = ?" , null, 5, "name");
		expectJpqlPositional( "SELECT id FROM Author WHERE foo = ? ORDER BY created" , null, expected, "name" );
		replay();
		
		List<Author> result = authorDaoJpqlWithCount.findByFooOrder( "created", "name" , c );
		Assert.assertSame( result, expected );
		Assert.assertEquals( c.getCount().intValue(), 5 );
		
		verify();
		
	}
	
	//	
	//	@Jpql("SELECT distinct {0} FROM Author WHERE foo = ?")
	//	@Count
	//	public Author findDistinct( String field, String foo , QueryCount c );
	@Test
	public void testWithDistinctMessage() {
		List<Author> expected = Arrays.asList( new Author("a"), new Author("b"));
		QueryCountStab c = new QueryCountStab();
		expectCountPoisitional( "SELECT count(distinct name) FROM Author WHERE foo = ?" , null, 5, "foo");
		expectJpqlPositional( "SELECT distinct name FROM Author WHERE foo = ?" , null, expected, "foo" );
		replay();
		
		Author result = authorDaoJpqlWithCount.findDistinct( "name", "foo" , c );
		Assert.assertSame( result, expected.get( 0 ) );
		Assert.assertEquals( c.getCount().intValue(), 5 );
		
		verify();
		
	}

	public void setAuthorDaoJpqlWithCount(AuthorDaoJpqlWithCount authorDaoJpqlWithCount) {
		this.authorDaoJpqlWithCount = authorDaoJpqlWithCount;
	}
	
	private static class QueryCountStab implements QueryCount {
		private Number count;

		public Number getCount() {
			return count;
		}

		public void setCount(Number count) {
			this.count = count;
		}
	}

}
