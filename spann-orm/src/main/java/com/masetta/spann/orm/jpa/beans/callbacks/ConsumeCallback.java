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

package com.masetta.spann.orm.jpa.beans.callbacks;


import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.spring.base.method.beans.CallContextHandlerChainBuilder;
import com.masetta.spann.spring.base.method.beans.CallContextHandlerChainBuilderCallback;

/**
 * Callback which consumes method arguments without adding any visitors.
 * @author Ron Piterman
 */
public class ConsumeCallback implements CallContextHandlerChainBuilderCallback<QueryCallContext> {
	
	private int[] consume;
	
	public ConsumeCallback(int ...consume) {
		super();
		this.consume = consume;
	}

	public void perform(CallContextHandlerChainBuilder<QueryCallContext> builder) {
		builder.addAndConsume( null, consume );
	}

}
