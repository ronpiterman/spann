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

package com.masetta.spann.orm.jpa.beans.handler;

import java.util.Collection;

import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.spring.base.method.beans.CallContextChainBuilder;
import com.masetta.spann.spring.util.Chain;
import com.masetta.spann.spring.util.ChainExecutor;
import com.masetta.spann.spring.util.Handler;

public class ProtectEmptyCollectionsHandler implements Chain<Object,QueryCallContext> , Handler<CallContextChainBuilder<QueryCallContext>> {
	
	private Integer[] collectionArgumentIndexes;
	
	private Object returnValue;
	
	public ProtectEmptyCollectionsHandler(Object returnValue, Integer[] collectionArgumentIndexes) {
		super();
		this.returnValue = returnValue;
		this.collectionArgumentIndexes = collectionArgumentIndexes;
	}

	public void handle(CallContextChainBuilder<QueryCallContext> t) {
		t.addAndConsume( this );
	}

	public Object perform(QueryCallContext param, ChainExecutor<Object, QueryCallContext> next) {
		for ( int idx : collectionArgumentIndexes ) {
			Collection<?> c = (Collection<?>) param.getArgument( idx );
			if ( c == null || c.isEmpty() ) {
				return returnValue;
			}
		}
		return next.next( param );
	}

}
