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

package com.masetta.spann.spring.integration.beanconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.spring.integration.BaseIntegrationTest;

public class TestBeanConfig extends BaseIntegrationTest {
	
	@Test
	public void testBeanConfig() {
		AnnotatedBean annBean = assertSingleBeanByType( AnnotatedBean.class , "annotatedBean" );
		Assert.assertNotNull( annBean );
		
		Map confBeans = getApplicationContext().getBeansOfType( ConfBean.class );
		Assert.assertEquals( confBeans.size(), 2 );
		
		List<String> names = new ArrayList<String>( confBeans.keySet() );
		ConfBean b1 = (ConfBean) confBeans.get( names.get( 0 ) );
		ConfBean b2 = (ConfBean) confBeans.get( names.get( 1 ) );
		
		ConfBean bb1 , bb2;
		
		if ( b1.getIntegers() == null ) {
			bb1 = b1;
			bb2 = b2;
		}
		else {
			bb1 = b2;
			bb2 = b1;
		}
		
		check( bb1 , 5 , true , "foo" , Serializable.class , null , annBean );
		check( bb2 , 6 , false , "bar" , Comparable.class, new int[] { 1 ,2 ,3 } , annBean );
		
	}

	private void check(ConfBean cb, int integer, boolean bool, String string, Class<?> type,
			int[] integers, Object parent ) {
		Assert.assertEquals( cb.getInteger() , integer, "integer" );
		Assert.assertEquals( cb.isBool(), bool, "bool" );
		Assert.assertEquals( cb.getString(), string , "string");
		Assert.assertEquals( cb.getType(), type , "type");
		Assert.assertTrue( Arrays.equals( cb.getIntegers() , (int[])integers ), "integers");
		Assert.assertSame( cb.getParent(), parent );
	}

}
