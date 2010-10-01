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

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.masetta.spann.orm.jpa.annotations.Dao;
import com.masetta.spann.orm.jpa.annotations.Jpql;
import com.masetta.spann.orm.jpa.beans.BaseDao;

@Dao
public interface DaoForTestSupport extends BaseDao<Author,Long> {
	
	@Jpql("")
	public List<Author> findTestCollection( Collection<String> roles );
	
	@Jpql("")
	public List<Author> findTestSkip( Author author );
	
	@Jpql("")
	public List<Author> findTestSkipCollection( Collection<Author> authors );
	
	@Jpql("")
	public Author findTestNull( Comparator<String> skipThisMethod );
	
	@Jpql("")
	public Author findTestNullCollection( Collection<Comparator<String>> skipThisMethod );
	
	
}
