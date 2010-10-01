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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import com.masetta.spann.orm.jpa.support.QueryCount;
import com.masetta.spann.orm.jpa.support.QueryPositionCount;
import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;
import com.masetta.spann.spring.util.Resolver;

/**
 * Perform a second count query for this method, and
 * update the given QueryCount argument.
 * <p>
 * The Count annotation will make the method call perform a second "count" query,
 * which is automatic generated from the underlying jpql query.
 * <p>
 * For supported syntax of Jpql Queries this annotation supports, see {@link SelectCountGenerator}. 
 * <p>
 * If should only be used with Jpql queries (e.g. methods annotated
 * with Jpql or ByMethodName, but not with named queries).
 * <p>
 * Methods annotated by Count must have one argument which
 * implements {@link QueryCount}. This first QueryCount argument will be used as out parameter.
 *
 * @see QueryCount
 * @see QueryPositionCount
 * @see SelectCountGenerator
 * 
 * @author Ron Piterman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Visitor(value=CountVisitor.class,order=Order.FINALIZE)
@Documented
public @interface Count {
	
	String GENERATOR_ATTRIBUTE = "generator";

	/**
	 * A class to use to generate a count jpql from a find jpql query.
	 * @return
	 */
	Class<? extends Resolver<String,String>> generator() default SelectCountGenerator.class;
}
