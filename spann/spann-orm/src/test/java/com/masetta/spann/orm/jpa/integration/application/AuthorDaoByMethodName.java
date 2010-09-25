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

import com.masetta.spann.orm.jpa.annotations.ByMethodName;
import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.orm.jpa.support.QueryPosition;



@Dao("authorDaoByMethodName")
public interface AuthorDaoByMethodName extends BaseDao<Author,Long> {
	
	// ---------------------------------------------------------------------------------------------
	// by method name 
	// ---------------------------------------------------------------------------------------------

	// finder 
	
	@DaoMethod
	@ByMethodName
	List<Author> findByName( String string );
	
	@DaoMethod
	@ByMethodName
	List<Author> findByName( String string , QueryPosition pos );
	
	@DaoMethod
	@ByMethodName
	Author findByFoo( String name , QueryPosition pos );
	
	@DaoMethod
	@ByMethodName
	Author getByCountry( String country);
	
	@DaoMethod
	@ByMethodName
	Author findByCountry( String name , QueryPosition pos );
	
	// distinct finder
	
	@DaoMethod
	@ByMethodName
	List<Integer> distinctYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	List<Integer> distinctYearOfBirthByCountry( String country );
	
	// count
	
	@DaoMethod
	@ByMethodName
	Number count();
	
	@DaoMethod
	@ByMethodName
	Number countByCountry( String country );
	
	@DaoMethod
	@ByMethodName
	Number countByCountryAndYearOfBirth( String coutnry , int year );
	
	// count distinct
	
	@DaoMethod
	@ByMethodName
	int countDistinctYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	int countDistinctYearOfBirthByCountry( String country );
	
	// avg
	
	@DaoMethod
	@ByMethodName
	int avgYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	int avgYearOfBirthByCountry( String country );
	
	// min
	
	@DaoMethod
	@ByMethodName
	int minYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	int minYearOfBirthByCountry( String country );
	
	// max
	
	@DaoMethod
	@ByMethodName
	int maxYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	int maxYearOfBirthByCountry( String country );
	
	// sum
	
	@DaoMethod
	@ByMethodName
	int sumYearOfBirth();
	
	@DaoMethod
	@ByMethodName
	int sumYearOfBirthByCountry( String country );
	
	
}
