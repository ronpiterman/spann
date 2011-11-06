package com.masetta.spann.orm.jpa.integration;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoSql;

public class TestSql extends BaseIntegrationTest {
	
	private AuthorDaoSql authorDaoSql;
	
	public void setAuthorDaoSql(AuthorDaoSql authorDaoSql) {
		this.authorDaoSql = authorDaoSql;
	}

//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo = ?")
//	public List<Author> findByFoo( String foo );
	@Test
	public void testNoMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo = ?", null, null, null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findByFoo( "a" );
		Assert.assertSame( result, list );
		verify();
	}
	
//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo = ?", resultSetClass=Author.class)
//	public List<Author> findEntityByFoo( String foo );
	@Test
	public void testClassMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo = ?", Author.class, null, null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findEntityByFoo( "a" );
		Assert.assertSame( result, list );
		verify();
	}
	
//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo = ?", resultSetMapping="bar" )
//	public List<Author> findMappingByFoo( String foo );
	@Test
	public void testNamedMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo = ?", null, "bar", null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findMappingByFoo( "a" );
		Assert.assertSame( result, list );
		verify();
	}
	
//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo {2} ?")
//	public List<Author> findByFoo( String foo , String op );
	@Test
	public void testDynamicNoMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo like ?", null, null, null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findByFoo( "a" , "like");
		Assert.assertSame( result, list );
		verify();
	}
	
//	
//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo {2} ?", resultSetClass=Author.class)
//	public List<Author> findEntityByFoo( String foo , String op );
	@Test 
	public void testDynamicClassMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo < ?", Author.class, null, null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findEntityByFoo( "a" , "<");
		Assert.assertSame( result, list );
		verify();
	}
//	
//	@DaoMethod
//	@SQL(value="SELECT * FROM Author WHERE foo {2} ?", resultSetMapping="bar" )
//	public List<Author> findMappingByFoo( String foo , String op );
	@Test
	public void testDynamicNamedMapping() {
		List<Author> list = Arrays.asList( new Author("Shalev"), new Author("Jehoshua"));
		expectSQL("SELECT * FROM Author WHERE foo >= ?", null, "bar", null, list, true, "a");
		replay();
		List<Author> result = this.authorDaoSql.findMappingByFoo( "a" , ">=");
		Assert.assertSame( result, list );
		verify();
	}

	
}
