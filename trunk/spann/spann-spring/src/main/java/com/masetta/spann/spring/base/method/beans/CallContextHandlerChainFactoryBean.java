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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.util.Chain;
import com.masetta.spann.spring.util.Resolver;

/**
 * Factory bean for creating chain of method call context handler ( a List of Resolver&lt;Boolean,X> ).
 * <p>
 * The factory is using a configured list of {@link CallContextHandlerChainBuilderCallback}. 
 * Each callback may add handler to the cahin and consume method arguments by index.
 * <p>
 * Consuming method arguments allows dynamic mapping of source-to-target method arguments.
 * By convention, the last callback in the configured list should consume all not consumed
 * arguments, so MetadataVisitors which mutate the callbacks list should usually add 
 * to the beginning of the list, rather than to the end.
 * <p>
 * Method call customization is therefore done in 3 Phases:
 * <OL>
 *  <LI>Annotation/Configuration Phase: Annotation visitors add (or otherwise mutate) a list of callbacks.<p>
 *  	The List order is important: In the next phase, every callback may consume
 *  	method arguments and add method-call-context handlers to the chain.
 *  <LI>Implement creation phase: This factory bean creates a builder and passes it to the callbacks,
 *  	which, in turn, may add method-call-context handler and consume arguments by index.
 *  <LI>Actual method call, A method call context is passed to each handler which may mutate
 *  	the context
 * </OL>
 * 
 * The three phases allows a very flexible configuration of the method call with very little
 * performance cost at method-call-time, since customization should be done in the first two phases;
 * the only cost being some complexity of the configuration.
 * 
 * @author Ron Piterman
 *
 * @param <T> type of the method call context.
 */
public class CallContextHandlerChainFactoryBean<T> implements FactoryBean , CallContextHandlerChainBuilder<T> , InitializingBean {
	
	private SpannLog log = SpannLogFactory.getLog( CallContextHandlerChainFactoryBean.class );
	
	private static final Chain<?,?>[] EMPTY_CHAIN_ARRAY = {}; 
	
	private static final Integer[] EMPTY_INT_ARRAY = new Integer[0];

	public static final String CALLBACKS_PROPERTY = "callbacks";

	private final List<Integer> arguments;

	private final List<Chain<Object,T>> visitors = new ArrayList<Chain<Object,T>>();
	
	private List<CallContextHandlerChainBuilderCallback<T>> callbacks;
	
	public CallContextHandlerChainFactoryBean( int argumentsCount ) {
		if ( log.isDebugEnabled() )
			log.debug( "create new QueryVisitorListBuilderImpl(" + argumentsCount + ")" );
		this.arguments = new ArrayList<Integer>(argumentsCount);
		for ( int i = 0; i < argumentsCount; i++ )
			arguments.add( Integer.valueOf( i ) );
	}

	public Object getObject() throws Exception {
		return visitors.toArray( EMPTY_CHAIN_ARRAY );
	}

	public Class<?> getObjectType() {
		return Resolver[].class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void addAndConsume( Chain<Object, T> contextChain, int... argIndexes) {
		if ( log.isDebugEnabled() )
			log.debug( "addAndConsume( " + contextChain + "," + Arrays.toString( argIndexes ) );
		if ( contextChain != null)
			this.visitors.add(contextChain);
		if ( argIndexes != null )
			for ( int i : argIndexes )
				this.arguments.remove( Integer.valueOf( i ));
	}

	public Integer[] getUnconsumedArguments() {
		return arguments.toArray(EMPTY_INT_ARRAY);
	}

	public void afterPropertiesSet() throws Exception {
		for ( CallContextHandlerChainBuilderCallback<T> c : callbacks ) {
			c.perform( this );
		}
	}

	public void setCallbacks(List<CallContextHandlerChainBuilderCallback<T>> callbacks) {
		this.callbacks = callbacks;
	}

}
