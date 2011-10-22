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

package com.masetta.spann.spring.integration.synadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.spring.integration.BaseIntegrationTest;

public class TestSyntheticAdapter extends BaseIntegrationTest {
	
	@Test
	public void testSimple() {
		Extractor ifcImpl = getSimple();
		Assert.assertNotNull( ifcImpl );
		String result = (String) ifcImpl.extract( 2f );
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void testIllegalArg() {
		Extractor ifcImpl = getSimple();
		Assert.assertNotNull( ifcImpl );
		String result = (String) ifcImpl.extract( "" );
	}
	
	@Test()
	public void testImpl() {
		Map<String,ExtractorImpl> impls = getApplicationContext().getBeansOfType( ExtractorImpl.class );
		Assert.assertEquals( impls.size(), 2 );
		
		List<ExtractorImpl> list = new ArrayList<ExtractorImpl>( impls.values() );
		ExtractorImpl i1 = list.get( 0 );
		ExtractorImpl i2 = list.get( 1 );
		
		ExtractorImpl[] i = { i1 , i2 };
		
		Class<?>[] ret = { i1.getReturnType(), i2.getReturnType() };
		Class<?>[] arg = { i1.getArgumentType() , i2.getArgumentType() };
		
		int first = ret[0].equals( String.class ) ? 0 : 1;
		int second = first ^ 1;
		
		Assert.assertEquals( ret[first], String.class );
		Assert.assertEquals( ret[second], Class.class );
		
		Assert.assertEquals( arg[first], Long.class );
		Assert.assertEquals( arg[second], Object.class );
		
		String s = (String) i[first].extract( 1l );
		Assert.assertEquals(s, "1");
		
		Class<?> c = (Class<?>) i[second].extract( "" );
		Assert.assertEquals(c, String.class );
		
		Assert.assertSame( impls.get( "classExtractor"), i[ second ] );
	}

	private Extractor getSimple() {
		Map<String,Object> both = getApplicationContext().getBeansOfType( Extractor.class );
		for ( Object o : both.values() ) {
			if ( ! ( o instanceof ExtractorImpl ) ) {
				return (Extractor) o;
			}
		}
		return null;
	}

}
