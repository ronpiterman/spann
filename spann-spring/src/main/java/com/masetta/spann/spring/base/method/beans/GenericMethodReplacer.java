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

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.MethodReplacer;

import com.masetta.spann.spring.util.Chain;
import com.masetta.spann.spring.util.ChainExecutor;
import com.masetta.spann.spring.util.Resolver;
import com.masetta.spann.spring.util.Resolver2;

/**
 * A generic method replacer which uses a configurable workflow to implement a method.
 * <p>
 * The method call will execute the following steps:
 * <ol>
 * 	 <li>Create a context via the contextFactory.</li>
 *   <li>Pass the context to a list of context handler, which may mutate it</li>
 *   <li>Create a result via a strategy object</li>
 *   <li>Transform the result via a resultTransformer (optional)</li>
 * </ol>
 * 
 * For creating the call context visitors, the factory bean {@link CallContextHandlerChainFactoryBean}
 * is used.
 * 
 * @author Ron Piterman
 *
 * @param <T> the type of the call context.
 */
public class GenericMethodReplacer<T> implements MethodReplacer , InitializingBean {
	
	public static final String CONTEXT_FACTORY_PROPERTY = "contextFactory";
	
	public static final String CONTEXT_HANDLER_CHAIN_PROPERTY = "contextHandlerChain";
	
	public static final String RESULT_STRATEGY_PROPERTY = "resultStrategy";
	
	public static final String RESULT_TRANSFORMER_PROPERTY = "resultTransformer";
	
	public static final String DESCRIPTION_PROPERTY = "description";
	
	private Resolver<T,Object[]> contextFactory;
	
	private Chain<Object,T>[] contextHandlerChain;
	
	private Resolver<Object,T> resultStrategy;
	
	private Chain<Object,T>[] mergedChain;
	
	private Resolver2<Object,Object,T> resultTransformer;
	
	private String description;

	public Object reimplement( Object obj, Method method, Object[] args ) throws Throwable {
		T context = contextFactory.resolve( args );
		ChainExecutor<Object, T> executor = new ChainExecutor<Object, T>(
				this.mergedChain );
		
		Object result = executor.next(context);
		if ( resultTransformer != null ) {
			result = resultTransformer.resolve( result , context );
		}
		return result;
	}

	/**
	 * Set a context factory.
	 * @param contextFactory
	 */
	public void setContextFactory(Resolver<T, Object[]> contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * Set an array of context handler which may mutate the context.
	 * Each context handler may return true to shortcut handling of 
	 * the method an return null.
	 * 
	 * @param contextHandlerChain
	 */
	public void setContextHandlerChain(Chain<Object, T>[] contextHandlerChain) {
		this.contextHandlerChain = contextHandlerChain;
	}
	
	public Chain<Object, T>[] getContextHandlerChain() {
		return contextHandlerChain;
	}

	/**
	 * An optional transformer, to transform the result produced by the
	 * result factory.
	 * @param resultTransformer
	 */
	public void setResultTransformer(Resolver2<Object, Object, T> resultTransformer) {
		this.resultTransformer = resultTransformer;
	}

	public void afterPropertiesSet() throws Exception {
		if ( this.contextFactory == null ) {
			throw new IllegalStateException( description + ":Generic method call context factory not set.");
		}
		if ( this.resultStrategy == null ) {
			throw new IllegalStateException( description + ":Generic method resultStrategy not set.");
		}
		ResultStrategyChainAdapter<T> fl = new ResultStrategyChainAdapter( this.resultStrategy );
		if ( this.contextHandlerChain == null ) {
			this.mergedChain = new Chain[] { fl };
		}
		else {
			this.mergedChain = Arrays.copyOf( contextHandlerChain, contextHandlerChain.length + 1);
			this.mergedChain[ this.mergedChain.length - 1] = fl;
		}
	}

	/**
	 * A Literal description of this method replacer. Used for error reporting.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set a result command, which creates the result from the call context.
	 * @param resultStrategy strategy object to generate 
	 */
	public void setResultStrategy(Resolver<Object, T> resultStrategy) {
		this.resultStrategy = resultStrategy;
	}

	private static class ResultStrategyChainAdapter<P> implements Chain<Object,P> {
		
		private final Resolver<Object,P> resultStrategy;

		public ResultStrategyChainAdapter(Resolver<Object, P> resultStrategy) {
			super();
			this.resultStrategy = resultStrategy;
		}

		public Object perform(P param, ChainExecutor<Object, P> next) {
			return this.resultStrategy.resolve( param );
		}
		
	}
	
}
