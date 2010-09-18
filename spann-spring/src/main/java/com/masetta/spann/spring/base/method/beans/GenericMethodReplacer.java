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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.MethodReplacer;

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
	
	public static final String EMPTY_RESULT_PROPERTY = "emptyResult";
	
	private Resolver<T,Object[]> contextFactory;
	
	private Resolver<Boolean,T>[] contextHandlerChain;
	
	private Resolver<Object,T> resultStrategy;
	
	private Resolver2<Object,Object,T> resultTransformer;
	
	private Object emptyResult;
	
	private String description;

	public Object reimplement( Object obj, Method method, Object[] args ) throws Throwable {
		T context = contextFactory.resolve( args );
		if ( ! performChain(context) )
			return emptyResult;
		Object result = resultStrategy.resolve( context );
		if ( resultTransformer != null ) {
			result = resultTransformer.resolve( result , context );
		}
		return result;
	}

	/**
	 * pass the given context to the context handler.
	 * 
	 * @param context
	 * @return if normal invocation should continue. returns false if invocation should
	 * 	return empty value.
	 */
	protected final boolean performChain(T context) {
		Boolean shortcut;
		for ( Resolver<Boolean,T> handler : contextHandlerChain ) {
			shortcut = handler.resolve( context );
			if ( shortcut != null && shortcut ) 
				return false;
		}
		return true;
	}
	
	/**
	 * Set a context factory.
	 * @param contextFactory
	 */
	public void setContextFactory(Resolver<T, Object[]> contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * Set an array of context contextHandler which may mutate the context.
	 * @param contextVisitor
	 */
	public void setContextHandlerChain(Resolver<Boolean, T>[] contextHandlerChain) {
		this.contextHandlerChain = contextHandlerChain;
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
	 * @param resultFactory
	 */
	public void setResultStrategy(Resolver<Object, T> resultStrategy) {
		this.resultStrategy = resultStrategy;
	}

	public void setEmptyResult(Object emptyResult) {
		this.emptyResult = emptyResult;
	}
	
}
