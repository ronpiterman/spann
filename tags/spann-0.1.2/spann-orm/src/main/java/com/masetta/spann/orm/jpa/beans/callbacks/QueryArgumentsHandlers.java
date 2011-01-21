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


import java.util.Arrays;

import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.handler.NamedArgumentSetter;
import com.masetta.spann.orm.jpa.beans.handler.PositionalArgumentSetter;
import com.masetta.spann.spring.base.method.beans.CallContextChainBuilder;
import com.masetta.spann.spring.util.Chain;
import com.masetta.spann.spring.util.Handler;

public final class QueryArgumentsHandlers {
	
	private QueryArgumentsHandlers() {}
	
	private abstract static class Abstract implements Handler<CallContextChainBuilder<QueryCallContext>> {
		public void handle(CallContextChainBuilder<QueryCallContext> builder) {
			Integer[] available = builder.getUnconsumedArguments();
			for ( int i = 0; i < available.length; i++ ) {
				builder.addAndConsume( createChain( i, available[i] ), available[i] );
			}
		}
		protected abstract Chain<Object,QueryCallContext> createChain(int queryPos, int methodArgIndex );
	}
	
	public final static class Positional extends Abstract {
		
		public static final Handler<CallContextChainBuilder<QueryCallContext>> INSTANCE = new Positional();
		
		private Positional() {}
	
		protected final Chain<Object,QueryCallContext> createChain(int queryPos, int methodArgIndex ) {
			return new PositionalArgumentSetter( queryPos , methodArgIndex );
		}
	}
	
	public final static class Named extends Abstract {
		
		private final String[] namedParameters;
		
		public Named(String[] namedPaameters) {
			super();
			this.namedParameters = namedPaameters;
		}
		
		protected final Chain<Object,QueryCallContext> createChain(int queryPos, int methodArgIndex ) {
			return new NamedArgumentSetter( this.namedParameters[queryPos] , methodArgIndex );
		}

		@Override
		public String toString() {
			return "QueryArgumentsHandlers$Named [namedParameters=" + Arrays.toString(namedParameters) + "]";
		}
		
	}


}
