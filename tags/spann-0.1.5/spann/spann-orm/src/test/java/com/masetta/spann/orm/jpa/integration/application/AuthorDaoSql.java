package com.masetta.spann.orm.jpa.integration.application;

import java.util.List;

import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.orm.jpa.annotations.SQL;
import com.masetta.spann.orm.jpa.beans.BaseDao;

@Dao
public interface AuthorDaoSql extends BaseDao<Author,Long> {
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo = ?")
	public List<Author> findByFoo( String foo );
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo = ?", resultSetClass=Author.class)
	public List<Author> findEntityByFoo( String foo );
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo = ?", resultSetMapping="bar" )
	public List<Author> findMappingByFoo( String foo );
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo {1} ?")
	public List<Author> findByFoo( String foo , String op );
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo {1} ?", resultSetClass=Author.class)
	public List<Author> findEntityByFoo( String foo , String op );
	
	@DaoMethod
	@SQL(value="SELECT * FROM Author WHERE foo {1} ?", resultSetMapping="bar" )
	public List<Author> findMappingByFoo( String foo , String op );

	
}