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
import com.masetta.spann.spring.util.Resolver;

public class PositionalArgumentSetter implements Resolver<Boolean,QueryCallContext> {
	
	private int queryParamPosition;
	
	private int methodArgIndex;
	
	public PositionalArgumentSetter(int queryParamPosition,
			int methodArgIndex) {
		super();
		this.queryParamPosition = queryParamPosition;
		this.methodArgIndex = methodArgIndex;
	}

	public Boolean resolve(QueryCallContext param) {
		param.getQuery().setParameter( queryParamPosition + 1, param.getArgument( methodArgIndex ) );
		return false;
	}
	
	public String toString() {
		return "PositionalArgumentSetter [queryParamPosition=" + queryParamPosition + ",methodArgIndex=" + methodArgIndex +"]";
	}

}
