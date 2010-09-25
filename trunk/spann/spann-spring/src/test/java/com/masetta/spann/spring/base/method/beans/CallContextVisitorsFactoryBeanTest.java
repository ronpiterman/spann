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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.spring.base.method.beans.CallContextHandlerChainFactoryBean;

public class CallContextVisitorsFactoryBeanTest {
	
	@Test
	public void testBuilder() {
		CallContextHandlerChainFactoryBean<?> impl = new CallContextHandlerChainFactoryBean<Object>( 1 );
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] { 0 } );
		
		impl.addAndConsume( null, 0 );
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] {} );
	}
	
	@Test
	public void testBuilder2() {
		CallContextHandlerChainFactoryBean<?> impl = new CallContextHandlerChainFactoryBean<Object>(3);
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] { 0 , 1 , 2} );
		
		impl.addAndConsume( null, 1 );
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] { 0 , 2 } );

		impl.addAndConsume( null, 2 );
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] { 0 } );
		
		impl.addAndConsume( null, 0 );
		Assert.assertEquals( impl.getUnconsumedArguments() , new Integer[] {} );
	}

}
