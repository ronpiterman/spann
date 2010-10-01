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

package com.masetta.spann.orm.hibernate.integration.application;

import com.masetta.spann.orm.hibernate.annotations.EnableFilter;
import com.masetta.spann.orm.hibernate.annotations.EnableFilters;
import com.masetta.spann.orm.hibernate.annotations.FilterParam;
import com.masetta.spann.orm.hibernate.support.FilterActivations;
import com.masetta.spann.orm.jpa.annotations.ByMethodName;
import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.Jpql;
import com.masetta.spann.orm.jpa.beans.BaseDao;
import com.masetta.spann.orm.jpa.integration.application.Author;

@Dao
public interface FiltersDao extends BaseDao<Author, Long> {
	
	@EnableFilters
	@ByMethodName
	Author findByName( String name , FilterActivations fa );
	
	@EnableFilters({@EnableFilter(name="age",parameters=@FilterParam(name="age",index=1))})
	@Jpql("FROM Author AS e WHERE e.name = ?")
	Author findByNameAndAge( String name , int age );

}
