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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoJpql;
import com.masetta.spann.orm.jpa.support.QueryPosition;

public class TestJpql extends BaseIntegrationTest {
	
	private AuthorDaoJpql authorDaoJpql;
	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ?")
	//	public List<Author> findByFoo( String foo );
	@Test
	public void testSingleArg() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlPositional( "FROM Author WHERE foo = ?", null, list, "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findByFoo( "foo" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ? ORDER BY {0}")
	//	public List<Author> findByFooOrder( String order , String foo );
	@Test
	public void testMessageFormat() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlPositional( "FROM Author WHERE foo = ? ORDER BY bar", null, list, "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findByFooOrder( "bar" , "foo" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ? ORDER BY {1}")
	//	public List<Author> findByFooOrder2( String foo, String order);
	@Test
	public void testMessageFormat2() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlPositional( "FROM Author WHERE foo = ? ORDER BY bar", null, list, "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findByFooOrder2( "foo" , "bar" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ?")
	//	public Author findSingle( String foo );
	@Test
	public void testFindSingle() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlPositional( "FROM Author WHERE foo = ?", null, list, "foo" );
		replay();
		Author result = this.authorDaoJpql.findSingle( "foo" );
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}

	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY {0} {1}")
	//	public Author findByManyArguments( String orderField, String orderOrder, 
	//			String foo , Long bar , Integer baz , QueryPosition pos );
	@Test
	public void testFindByManyArgs() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		QueryPosition qp = new QueryPosition( 20 , 10 );
		expectJpqlPositional( "FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY ordfield asc", 
				qp, list, "foo" , 12l , 13 );
		replay();
		Author result = this.authorDaoJpql.findByManyArguments( "ordfield", "asc", "foo", 12l, 13, qp );
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY {4} {5}")
	//	public Author findByManyArguments( String foo , Long bar , Integer baz , QueryPosition pos ,
	//			String orderField, String orderOrder);
	//	
	@Test
	public void testFindByManyArgs2() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		QueryPosition qp = new QueryPosition( 20 , 10 );
		expectJpqlPositional( "FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY ordfield asc", 
				qp, list, "foo" , 12l , 13 );
		replay();
		Author result = this.authorDaoJpql.findByManyArguments( "foo", 12l, 13, qp , "ordfield", "asc");
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}
	
	// ---------------------------------------------------------------------------------------------
	// named parameters
	
	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a")
	//	@NamedParameter("a")
	//	public List<Author> findByBar( String foo );
	@Test
	public void testSingleNamedArg() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlNamed( "FROM Author WHERE foo = :a", null, list, "a", "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findByBar( "foo" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a ORDER BY {0}")
	//	@NamedParameter("a")
	//	public List<Author> findBybarOrder( String order , String foo );
	@Test
	public void testMessageFormatNamed() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlNamed( "FROM Author WHERE foo = :a ORDER BY bar", null, list, "a", "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findBybarOrder( "bar" , "foo" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a ORDER BY {1}")
	//	@NamedParameter("a")
	//	public List<Author> findByBarOrder2( String foo, String order);
	@Test
	public void testMessageFormatNamed2() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlNamed( "FROM Author WHERE foo = :a ORDER BY bar", null, list, "a", "foo" );
		replay();
		List<Author> result = this.authorDaoJpql.findByBarOrder2( "foo" , "bar" );
		Assert.assertSame( result, list );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a")
	//	@NamedParameter("a")
	//	public Author findSingleBar( String foo );
	@Test
	public void testFindSingleNamed() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectJpqlNamed( "FROM Author WHERE foo = :a", null, list, "a" , "foo" );
		replay();
		Author result = this.authorDaoJpql.findSingleBar( "foo" );
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}
	
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY {0} {1}")
	//	@NamedParameter({"c","a","b"})
	//	public Author findByManyNamedArguments( String orderField, String orderOrder, 
	//			Integer baz , String foo , Long bar, QueryPosition pos );
	@Test
	public void testFindByManyNamedArgs() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		QueryPosition qp = new QueryPosition( 20 , 10 );
		expectJpqlNamed( "FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY ordfield asc", 
				qp, list, "c" , 13 , "a" , "foo" , "b" , 12l );
		replay();
		Author result = this.authorDaoJpql.findByManyNamedArguments( "ordfield", "asc", 13, "foo", 12l, qp );
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}
	//	
	//	@DaoMethod
	//	@Jpql("FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY {4} {5}")
	//	@NamedParameter({"c","a","b"})
	//	public Author findByManyNamedArguments( Integer baz , String foo , Long bar, 
	//			QueryPosition pos , String orderField, String orderOrder);
	@Test
	public void testFindByManyNamedArgs2() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		QueryPosition qp = new QueryPosition( 20 , 10 );
		expectJpqlNamed( "FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY ordfield desc", 
				qp, list, "c" , 13 , "a" , "foo" , "b", 12l );
		replay();
		Author result = this.authorDaoJpql.findByManyNamedArguments( 13, "foo", 12l, qp , "ordfield", "desc");
		Assert.assertSame( result, list.get( 0 ) );
		verify();
	}
	
	
	// ---------------------------------------------------------------------------------------------
	// named parameters
	
	//	@DaoMethod
	//	@Jpql("UPDATE Author SET name = :name WHERE id = :id")
	//	@NamedParameter({"name","id"})
	//	public void updateNameById( String name , Long id );
	public void testUpdateNamed() {
		int num = 1;
		expectJpqlNamedUpdate( "UPDATE Author SET name = :name WHERE id = :id", num , "Lea Goldberg" , 12l );
		replay();
		int result = this.authorDaoJpql.updateNameById( "Lea Goldberg", 12l );
		Assert.assertEquals( result, num );
		verify();
	}
	//	
	//	@DaoMethod
	//	@Jpql("UPDATE Author SET country = ? WHERE id = ?")
	//	public void updateCountryById( String name , Long id );
	//	
	//	@DaoMethod(op=Op.UPDATE)
	//	@Jpql("UPDATE Author SET name = :name WHERE id = :id")
	//	@NamedParameter({"name","id"})
	//	public void setNameById( String name , Long id );
	
	@Test
	public void testMethodNameOverride() {
		List<?> result = new ArrayList();
		expectJpqlPositional( "A", null, result, 12l );
		replay();
		List actual = this.authorDaoJpql.findA( 12l );
		verify();
		clearMocks();
		
		expectJpqlPositional( "a", null, result, "12" );
		replay();
		actual = this.authorDaoJpql.findA( "12" );
		verify();
		
	}


	public void setAuthorDao2(AuthorDaoJpql authorDaoJpql) {
		this.authorDaoJpql = authorDaoJpql;
	}
}
