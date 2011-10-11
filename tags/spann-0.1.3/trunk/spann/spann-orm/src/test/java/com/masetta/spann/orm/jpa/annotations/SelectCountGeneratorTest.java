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

package com.masetta.spann.orm.jpa.annotations;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.annotations.SelectCountGenerator;

public class SelectCountGeneratorTest {
	
	private SelectCountGenerator impl = new SelectCountGenerator();
	
	@Test(dataProvider="jpql")
	public void testCreate( String jpql , String expected ) {
		try {
			String result = impl.resolve( jpql );
			Assert.assertEquals( result, expected );
		}
		catch ( IllegalArgumentException ex ) {
			Assert.assertNull( expected , "Could not process: " + jpql );
		}
	}
	
	@DataProvider(name="jpql")
	public Object[][] getJpql() {
		return new Object[][] {
				{ "FROM Entity AS e WHERE" , "SELECT count(*) FROM Entity AS e WHERE" },
				{ "SELECT e FROM Entity AS e WHERE" , "SELECT count(e) FROM Entity AS e WHERE" },
				{ "SELECT e.foo FROM Entity AS e WHERE" , "SELECT count(e.foo) FROM Entity AS e WHERE" },
				{ "SELECT e.foo, e.bar FROM Entity AS e WHERE" , "SELECT count(e.foo) FROM Entity AS e WHERE" },
				{ "SELECT distinct e.foo FROM Entity AS e WHERE" , "SELECT count(distinct e.foo) FROM Entity AS e WHERE" },
				{ "SELECT new pak.Obj( e.bar ) FROM Entity AS e WHERE" , "SELECT count(e.bar) FROM Entity AS e WHERE" },
				{ "SELECT new pak.Obj( e.bar, e.foo ) FROM Entity AS e WHERE" , "SELECT count(e.bar) FROM Entity AS e WHERE" },
				{ "SELECT new pak.Obj( e.bar, e.foo ), e.baz FROM Entity AS e WHERE" , "SELECT count(e.bar) FROM Entity AS e WHERE" },
				{ "SELECT new pak.Obj$Internal( e.bar, e.foo ), e.baz FROM Entity AS e WHERE" , "SELECT count(e.bar) FROM Entity AS e WHERE" },
				
		};
	}

}
