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

package com.masetta.spann.orm.hibernate.callbacks;

import com.masetta.spann.orm.hibernate.chain.FilterListEnabler;
import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupport;
import com.masetta.spann.spring.base.method.beans.CallContextChainBuilder;
import com.masetta.spann.spring.util.Handler;

public class FilterListEnablerChainBuilderHandler implements Handler<CallContextChainBuilder<QueryCallContext>> {
	
	private EntityManagerSupport entityManagerSupport;
	
	private int argumentIndex;
	
	public FilterListEnablerChainBuilderHandler(EntityManagerSupport entityManagerSupport,
			int argumentIndex) {
		super();
		this.entityManagerSupport = entityManagerSupport;
		this.argumentIndex = argumentIndex;
	}

	public void handle(CallContextChainBuilder<QueryCallContext> builder) {
		builder.addAndConsume( new FilterListEnabler(entityManagerSupport, argumentIndex ) , argumentIndex );
	}

}
