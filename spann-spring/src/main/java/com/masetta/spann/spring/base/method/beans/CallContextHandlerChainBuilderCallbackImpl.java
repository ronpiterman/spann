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

import com.masetta.spann.spring.util.Resolver;

public class CallContextHandlerChainBuilderCallbackImpl<T> implements CallContextHandlerChainBuilderCallback<T> {
	
	private final Resolver<Boolean,T> handler;
	
	private final int[] consumeArguments;
	
	public CallContextHandlerChainBuilderCallbackImpl(Resolver<Boolean, T> handler,
			int ...consumeArguments) {
		super();
		this.handler = handler;
		this.consumeArguments = consumeArguments;
	}

	public void perform(CallContextHandlerChainBuilder<T> builder) {
		builder.addAndConsume( handler, consumeArguments );
	}
}
