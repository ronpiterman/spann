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

import java.text.ParseException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.annotations.MethodNameJpqlGenerator;

public class MethodNameJpqlGeneratorTest {
	
	@Test(dataProvider="data")
	public void testParse( String method , String[] parameters, String expectedEql ) {
		MethodNameJpqlGenerator b = new MethodNameJpqlGenerator(method , "Entity" , parameters );
		try {
			String result = b.getEql().trim();
			Assert.assertNotNull( result );
			Assert.assertEquals( result, expectedEql );
		}
		catch ( ParseException ex ) {
			Assert.assertNull( expectedEql );
		}
	}
	
	@DataProvider(name="data")
	public Object[][] getData() {
		String[] a = {"a"};
		String[] ab = {"a","b"};
		String[] abc = {"a","b","c"};
		return new Object[][] {
			 { "findByName" , null , "FROM Entity AS e WHERE e.name = ?" },	
			 { "getByName" , null , "FROM Entity AS e WHERE e.name = ?" },	
			 { "findByA" , null , "FROM Entity AS e WHERE e.a = ?" },
			 { "findByAnd" , null , "FROM Entity AS e WHERE e.and = ?" },
			 { "findByAAnd" , null , "FROM Entity AS e WHERE e.aAnd = ?" },
			 { "findByAAndB" , null , "FROM Entity AS e WHERE e.a = ? AND e.b = ?" },
			 { "findByAAndBoo" , null , "FROM Entity AS e WHERE e.a = ? AND e.boo = ?" },
			 { "findByAaaAndBoo" , null , "FROM Entity AS e WHERE e.aaa = ? AND e.boo = ?" },
			 { "findByAaaBbbAndBooCii" , null , "FROM Entity AS e WHERE e.aaaBbb = ? AND e.booCii = ?" },
			 { "findByAAndBAnd" , null , "FROM Entity AS e WHERE e.a = ? AND e.bAnd = ?" },
			 { "findByAAndBAndC" , null , "FROM Entity AS e WHERE e.a = ? AND e.b = ? AND e.c = ?" },
			 { "findByAAndBAndCAnd" , null , "FROM Entity AS e WHERE e.a = ? AND e.b = ? AND e.cAnd = ?" },
			// named parameters
			 { "findByName" , a , "FROM Entity AS e WHERE e.name = :a" },	
			 { "findByA" , a , "FROM Entity AS e WHERE e.a = :a" },
			 { "findByAnd" , a, "FROM Entity AS e WHERE e.and = :a" },
			 { "findByAAnd" , a, "FROM Entity AS e WHERE e.aAnd = :a" },
			 { "findByAAndB" , ab , "FROM Entity AS e WHERE e.a = :a AND e.b = :b" },
			 { "findByAAndBoo" , ab , "FROM Entity AS e WHERE e.a = :a AND e.boo = :b" },
			 { "findByAaaAndBoo" , ab , "FROM Entity AS e WHERE e.aaa = :a AND e.boo = :b" },
			 { "findByAaaBbbAndBooCii" , ab , "FROM Entity AS e WHERE e.aaaBbb = :a AND e.booCii = :b" },
			 { "findByAAndBAnd" , ab , "FROM Entity AS e WHERE e.a = :a AND e.bAnd = :b" },
			 { "findByAAndBAndC" , abc, "FROM Entity AS e WHERE e.a = :a AND e.b = :b AND e.c = :c" },
			 { "findByAAndBAndCAnd" , abc , "FROM Entity AS e WHERE e.a = :a AND e.b = :b AND e.cAnd = :c" },
			// fail
			 { "findByName" , ab , null },	
			 { "findByAAndBAndCAnd" , a , null },
			// func
			 { "count" , null, "SELECT count(e) FROM Entity AS e" },
			 { "countDistinctName" , null, "SELECT count( distinct e.name ) FROM Entity AS e" },
			 { "distinctName" , null, "SELECT distinct e.name FROM Entity AS e" },
			 { "avgAge" , null, "SELECT avg(e.age) FROM Entity AS e" },
			 { "maxAge" , null, "SELECT max(e.age) FROM Entity AS e" },
			 { "minAge" , null, "SELECT min(e.age) FROM Entity AS e" },
			 { "sumAge" , null, "SELECT sum(e.age) FROM Entity AS e" },
			 { "countByName" , null, "SELECT count(e) FROM Entity AS e WHERE e.name = ?" },
			 { "distinctNameByAge" , null, "SELECT distinct e.name FROM Entity AS e WHERE e.age = ?" },
			 { "avgAgeByCountry" , null, "SELECT avg(e.age) FROM Entity AS e WHERE e.country = ?" },
			 { "maxAgeByCountry" , null, "SELECT max(e.age) FROM Entity AS e WHERE e.country = ?" },
			 { "minAgeByCountry" , null, "SELECT min(e.age) FROM Entity AS e WHERE e.country = ?" },
			 { "sumAgeByCountry" , null, "SELECT sum(e.age) FROM Entity AS e WHERE e.country = ?" },
			 
			
		};
	}

}
