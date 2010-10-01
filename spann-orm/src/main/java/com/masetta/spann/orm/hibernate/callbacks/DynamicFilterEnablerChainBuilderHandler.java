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

import java.util.Collection;
import java.util.Map;

import com.masetta.spann.orm.hibernate.chain.DynamicFilterEnabler;
import com.masetta.spann.orm.hibernate.support.FilterActivation;
import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupport;
import com.masetta.spann.spring.base.method.beans.CallContextChainBuilder;
import com.masetta.spann.spring.util.Handler;

public class DynamicFilterEnablerChainBuilderHandler implements Handler<CallContextChainBuilder<QueryCallContext>> {
	
	private EntityManagerSupport entityManagerSupport;
	
	private Collection<FilterActivation> filters;
	
	public DynamicFilterEnablerChainBuilderHandler(EntityManagerSupport entityManagerSupport,
			Collection<FilterActivation> filters) {
		super();
		this.entityManagerSupport = entityManagerSupport;
		this.filters = filters;
	}

	public void handle(CallContextChainBuilder<QueryCallContext> builder) {
		for ( FilterActivation fa : filters ) {
			for ( Map.Entry<String, Object> e : fa.getParameters() ) {
				builder.addAndConsume( null, (Integer) e.getValue() );
			}
		}
		builder.addAndConsume( new DynamicFilterEnabler(entityManagerSupport, filters) );
	}

}
