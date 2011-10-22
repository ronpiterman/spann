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

package com.masetta.spann.spring.integration.replace;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.spring.integration.BaseIntegrationTest;

public class TestReplace extends BaseIntegrationTest {
	
	private void testReplacedMethods( ReplacedMethods bean ) {
		String replacedByStringBean = bean.replacedBySpringBean("a", 1, 2.5f );
		Assert.assertEquals( replacedByStringBean, "1");
		
		String replacedByClass = bean.replacedByClass("a", 1, 2.5f );
		Assert.assertEquals( replacedByClass, "2.5");
	}
	
	private void testNotReplacedImpl( ReplacedMethods bean ) {
		String r = bean.notReplacedImplemented("a",1,2.5f);
		Assert.assertEquals( r, "a" );
	}
	
	private void testNotReplacedNotImpl( ReplacedMethods bean ) {
		try {
			bean.notReplacedNotImplemented("a",1,2.5f);
			Assert.assertTrue(false,"method implemented");
		}
		catch ( AbstractMethodError e ) {
		}
	}
	
	@Test(dataProvider="beanClasses")
	public <T extends ReplacedMethods> void testBean( Class<T> cls ) {
		T bean = assertSingleBeanByType( cls,  null );
		testReplacedMethods( bean );
		testNotReplacedNotImpl( bean );
		if ( ! cls.isInterface() ) {
			testNotReplacedImpl( bean );
		}
	}
	
	@DataProvider(name="beanClasses")
	public Object[][] getBeansTypes() {
		return new Object[][] {
			{ BeanAbstract.class },
			{ BeanClass.class },
			{ BeanInterface.class}
		};
	}


}
