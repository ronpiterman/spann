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

import java.util.List;

import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.orm.jpa.annotations.Jpql;
import com.masetta.spann.orm.jpa.annotations.NamedParameter;
import com.masetta.spann.orm.jpa.annotations.DaoMethod.Op;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.orm.jpa.support.QueryPosition;

@Dao
public interface AuthorDaoJpql extends BaseDao<Author,Long> {
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ?")
	public List<Author> findByFoo( String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ? ORDER BY {0}")
	public List<Author> findByFooOrder( String order , String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ? ORDER BY {1}")
	public List<Author> findByFooOrder2( String foo, String order);
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ?")
	public Author findSingle( String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY {0} {1}")
	public Author findByManyArguments( String orderField, String orderOrder, 
			String foo , Long bar , Integer baz , QueryPosition pos );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = ? AND bar = ? AND baz = ? ORDER BY {4} {5}")
	public Author findByManyArguments( String foo , Long bar , Integer baz , QueryPosition pos ,
			String orderField, String orderOrder);
	
	// named parameters
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a")
	@NamedParameter("a")
	public List<Author> findByBar( String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a ORDER BY {0}")
	@NamedParameter("a")
	public List<Author> findBybarOrder( String order , String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a ORDER BY {1}")
	@NamedParameter("a")
	public List<Author> findByBarOrder2( String foo, String order);
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a")
	@NamedParameter("a")
	public Author findSingleBar( String foo );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY {0} {1}")
	@NamedParameter({"c","a","b"})
	public Author findByManyNamedArguments( String orderField, String orderOrder, 
			Integer baz , String foo , Long bar, QueryPosition pos );
	
	@DaoMethod
	@Jpql("FROM Author WHERE foo = :a AND bar = :b AND baz = :c ORDER BY {4} {5}")
	@NamedParameter({"c","a","b"})
	public Author findByManyNamedArguments( Integer baz , String foo , Long bar, 
			QueryPosition pos , String orderField, String orderOrder);
	
	// update
	
	@DaoMethod
	@Jpql("UPDATE Author SET name = :name WHERE id = :id")
	@NamedParameter({"name","id"})
	public int updateNameById( String name , Long id );
	
	@DaoMethod
	@Jpql("UPDATE Author SET country = ? WHERE id = ?")
	public int updateCountryById( String name , Long id );
	
	@DaoMethod(op=Op.UPDATE)
	@Jpql("UPDATE Author SET name = :name WHERE id = :id")
	@NamedParameter({"name","id"})
	public void setNameById( String name , Long id );

}
