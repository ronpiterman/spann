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

import com.masetta.spann.spring.util.Chain;

/**
 * Builder for a list of Resolver&lt;Boolean,T> based on a list of method arguments.
 * 
 * The builder is passed to callbacks which may "consume" method arguments by index,
 * and add handler to the chain.
 * 
 * @author Ron Piterman
 *
 * @param <T>
 */
public interface CallContextChainBuilder<T> {
	
	/** 
	 * Return the indexes of the arguments which were not yet consumed 
	 */
	Integer[] getUnconsumedArguments();
	
	/**
	 * Add the given context handler to the chain and consume the arguments 
	 * with the given indexes.
	 */
	void addAndConsume( Chain<Object,T> contextHandler , int ...argIndexes );
	

}
