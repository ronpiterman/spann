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


import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.support.QueryPosition;
import com.masetta.spann.spring.util.Chain;
import com.masetta.spann.spring.util.ChainExecutor;

public class QueryPositionSetter implements Chain<Object, QueryCallContext> {

	private int queryPositionArgumentIndex;

	public QueryPositionSetter(int queryPositionArgumentIndex) {
		super();
		this.queryPositionArgumentIndex = queryPositionArgumentIndex;
	}

	public Object perform(QueryCallContext ctx, ChainExecutor<Object, QueryCallContext> next) {
		QueryPosition qp = (QueryPosition) ctx.getArgument(queryPositionArgumentIndex);

		if ( qp != null ) {
			if ( qp.getOffset() != null ) {
				ctx.getQuery().setFirstResult(qp.getOffset());
			}
			if ( qp.getMaxResults() != null ) {
				ctx.getQuery().setMaxResults(qp.getMaxResults());
			}
		}
		return next.next( ctx );
	}

}
