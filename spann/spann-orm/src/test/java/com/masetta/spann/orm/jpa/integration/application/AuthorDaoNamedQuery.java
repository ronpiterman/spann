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


import java.util.Date;
import java.util.List;

import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.orm.jpa.annotations.NamedParameter;
import com.masetta.spann.orm.jpa.annotations.NamedQuery;
import com.masetta.spann.orm.jpa.beans.BaseDaoImpl;
import com.masetta.spann.orm.jpa.support.QueryPosition;

@Dao
public abstract class AuthorDaoNamedQuery extends BaseDaoImpl<Author,Long> {
	
	@DaoMethod
	@NamedQuery
	public abstract List<Author> findByName( String name , QueryPosition qp );
	
	@DaoMethod
	@NamedQuery
	@NamedParameter("a")
	public abstract List<Author> findByFoo( String name , QueryPosition qp );

	@DaoMethod
	@NamedQuery
	@NamedParameter({"foo","bar","baz"})
	public abstract List<Author> findByFooAndBarAndBaz( String foo , QueryPosition qp ,
			Date bar , Long baz );
	
	@DaoMethod
	@NamedQuery
	public abstract List<Author> findByBazAndBarAndFoo( String baz , QueryPosition qp ,
			Date bar , Long foo );


}
