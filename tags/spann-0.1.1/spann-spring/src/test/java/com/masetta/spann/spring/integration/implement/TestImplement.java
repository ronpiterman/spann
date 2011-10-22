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

package com.masetta.spann.spring.integration.implement;

import org.testng.annotations.Test;

import com.masetta.spann.spring.integration.BaseIntegrationTest;

public class TestImplement extends BaseIntegrationTest {
	
	@Test
	public void testAbstractClass() {
		assertSingleBeanByType( AbstractClassBean.class, "acb" );
	}
	
	@Test
	public void testInterfaceBean() {
		assertSingleBeanByType( InterfaceBean.class, "interfaceBean" );
	}
	
	@Test
	public void testJustABean() {
		assertSingleBeanByType( JustABean.class, "jab" );
	}
	
	@Test
	public void testNoNameBean() {
		assertSingleBeanByType( NoNameBean.class, null );
	}
	
	@Test(expectedExceptions=AssertionError.class)
	public void testNoBeanForTestClass() {
		assertSingleBeanByType( TestImplement.class, null );
	}

}
