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
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoEmptyCollections;

public class TestProtectEmptyCollections extends BaseIntegrationTest {
	
	private AuthorDaoEmptyCollections authorDaoEmptyCollections;
	
	private List<Author> expected = Arrays.asList( new Author("a"), new Author("b") );
	
	@Test
	public void testWithoutProtect() {
		List<String> param = new ArrayList<String>();
		expectJpqlNamed( "FROM Author WHERE name in (:names)", null , expected , "names" , param);
		replay();
		
		List<Author> result = authorDaoEmptyCollections.findByNames( param );
		Assert.assertSame( result, expected );
		
		verify();
	}
	
	@Test
	public void testWithProtect() {
		List<String> param = new ArrayList<String>();
		expectJpql( "FROM Author WHERE name in (:names)" );
		replay();
		
		List<Author> result = authorDaoEmptyCollections.findByNamesProtected( param );
		Assert.assertSame( result, Collections.EMPTY_LIST );
		
		verify();
	}
	
	@Test
	public void testWithObsoleteProtect() {
		expectJpqlPositional( "FROM Author WHERE name = ?" , null , expected , (String)null );
		replay();
		
		List<Author> result = authorDaoEmptyCollections.findByName( null );
		Assert.assertSame( result, expected );
		
		verify();
	}

	public void setAuthorDaoEmptyCollections(AuthorDaoEmptyCollections authorDaoEmptyCollections) {
		this.authorDaoEmptyCollections = authorDaoEmptyCollections;
	}

}
