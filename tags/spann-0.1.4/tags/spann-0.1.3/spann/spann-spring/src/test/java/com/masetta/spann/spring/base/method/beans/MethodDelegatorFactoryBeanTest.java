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

package com.masetta.spann.spring.base.method.beans;

import junit.framework.Assert;

import org.springframework.beans.factory.support.MethodReplacer;
import org.testng.annotations.Test;

import com.masetta.spann.spring.base.method.beans.MethodDelegatorFactoryBean;

public class MethodDelegatorFactoryBeanTest {
	
	@Test
	public void testSimple() throws Throwable {
		MethodDelegatorFactoryBean impl = new MethodDelegatorFactoryBean();
		
		//impl.setApplicationContext( ac );
		impl.setTargetBean( "foo" );
		impl.setTargetParameterClasses( new Class[] { Object.class } );
		impl.setTargetMethodName("equals");
		
		impl.afterPropertiesSet();
		
		MethodReplacer r = (MethodReplacer)impl.getObject();
		
		Object result = r.reimplement( "bar", null, new Object[] {"foo" } );
		Assert.assertTrue( ((Boolean)result).booleanValue() );
		
		result = r.reimplement( "bar", null, new Object[] {"bar" } );
		Assert.assertFalse( ((Boolean)result).booleanValue() );
	}
	
	

}
