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
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoByMethodName;
import com.masetta.spann.orm.jpa.support.QueryPosition;

public class TestByMethodName extends BaseIntegrationTest {
	
	protected AuthorDaoByMethodName authorDaoByMethodName;
	
	// finder 
	
	// List<Author> findByName( String string );
	@Test
	public void testFinder() {
		List<Author> expected = Arrays.asList( new Author("a"), new Author("b"));
		expectJpqlPositional( "FROM Author AS e WHERE e.name = ?" , null, expected, "name" );
		replay();
		List<Author> result = authorDaoByMethodName.findByName( "name" );
		Assert.assertSame( result , expected );
		verify();
	}
	
	// List<Author> findByName( String string , QueryPosition pos );
	@Test
	public void testFinderWithPos() {
		List<Author> expected = Arrays.asList( new Author("c"), new Author("d"));
		QueryPosition qp = new QueryPosition( 5 , 7 );
		expectJpqlPositional( "FROM Author AS e WHERE e.name = ?" , qp, expected, "name" );
		replay();
		List<Author> result = authorDaoByMethodName.findByName( "name" , qp );
		Assert.assertSame( result , expected );
		verify();
	}
	
	// Author getByName( String name , QueryPosition pos );
	@Test
	public void testFindOne() {
		List<Author> in = Arrays.asList( new Author("c"), new Author("d"));
		QueryPosition qp = new QueryPosition( 5 , 7 );
		expectJpqlPositional( "FROM Author AS e WHERE e.foo = ?" , qp, in, "name" );
		replay();
		Author result = authorDaoByMethodName.findByFoo( "name" , qp );
		Assert.assertSame( result, in.get( 0 ) );
		verify();
	}

	// Author getByCountry( String country);
	@Test
	public void testGetSingle() {
		Author a = new Author("p");
		QueryPosition qp = null;
		expectJpqlPositionalSingle( "FROM Author AS e WHERE e.country = ?" , qp, a, "name" );
		replay();
		Author result = authorDaoByMethodName.getByCountry( "name" );
		Assert.assertSame( result, a );
		verify();
	}

	//	Author getByCountry( String name , QueryPosition pos );
	@Test
	public void testFindOneWithPos() {
		List<Author> in = Arrays.asList( new Author("f"), new Author("g"));
		QueryPosition qp = new QueryPosition( 5 , 7 );
		expectJpqlPositional( "FROM Author AS e WHERE e.country = ?" , qp, in, "name" );
		replay();
		Author result = authorDaoByMethodName.findByCountry( "name" , qp );
		Assert.assertSame( result, in.get( 0 ) );
		verify();
	}
	
	@Test
	public void testFindOneWithNullPos() {
		List<Author> in = Arrays.asList( new Author("t"), new Author("z"));
		QueryPosition qp = null;
		expectJpqlPositional( "FROM Author AS e WHERE e.country = ?" , qp, in, "name" );
		replay();
		Author result = authorDaoByMethodName.findByCountry( "name" , qp );
		Assert.assertSame( result, in.get( 0 ) );
		verify();
	}


	
	// distinct finder
	
//	@DaoMethod
//	@ByMethodName
//	List<Integer> distinctYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	List<Integer> distinctYearOfBirthByCountry( String country );
//	
	// count
//	
//	@DaoMethod
//	@ByMethodName
//	Number count();
//	
//	@DaoMethod
//	@ByMethodName
//	Number countByCountry( String country );
//	
//	@DaoMethod
//	@ByMethodName
//	Number countByCountryAndYearOfBirth( String coutnry , int year );
	
	// count distinct
	
//	@DaoMethod
//	@ByMethodName
//	int distinctCountryYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	int distinctCountYearOfBirthByCountry( String country );
//	
	// avg
	
//	@DaoMethod
//	@ByMethodName
//	int avgYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	int avgYearOfBirthByCountry( String country );
	
	// min
	
//	@DaoMethod
//	@ByMethodName
//	int minYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	int minYearOfBirthByCountry( String country );
	
	// max
	
//	@DaoMethod
//	@ByMethodName
//	int maxYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	int maxYearOfBirthByCountry( String country );
	
	// sum
//	
//	@DaoMethod
//	@ByMethodName
//	int sumYearOfBirth();
//	
//	@DaoMethod
//	@ByMethodName
//	int sumYearOfBirthByCountry( String country );


	public void setAuthorDao1(AuthorDaoByMethodName authorDaoByMethodName) {
		this.authorDaoByMethodName = authorDaoByMethodName;
	}
	

}
