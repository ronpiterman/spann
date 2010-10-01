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

package com.masetta.spann.orm.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

@Retention(RetentionPolicy.RUNTIME)
@Visitor(value=DaoMethodVisitor.class,order=Order.CREATE_BEAN)
public @interface DaoMethod {
	
	String OP_ATTRIBUTE = "op"; 
	
	public static enum Op {
		
		/** Perform an update */
		UPDATE, 
		/** 
		 * Perfrom a query and return a list result.
		 * 
		 * If the method return value is not a list, return
		 * only the first result or null if none.
		 */
		FIND, 
		
		/** 
		 * Perform a query and return a singel result. 
		 * See DaoMethod.getSingleResult() for exceptions thrown by this method 
		 */
		GET,
		
		/**
		 * Use method name as indicator.
		 * FIND is used as default, unless method name starts with "update".
		 */
		DETECT
		
	}
	
	Op op() default Op.DETECT;

}
