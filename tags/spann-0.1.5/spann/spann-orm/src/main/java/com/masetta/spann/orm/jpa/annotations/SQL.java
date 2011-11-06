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

import javax.persistence.EntityManager;

import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * @author Ron Piterman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Documented
@DaoMethod
@Visitor(value=SQLVisitor.class,order=Order.AFTER_CREATE)
public @interface SQL {
	
	String VALUE = "value";
	String RESULT_SET_CLASS = "resultSetClass";
	String RESULT_SET_MAPPING = "resultSetMapping";
	String USE_MESSAGE_FORMAT = "useMessageFormat";
	
	/**
	 * The SQL query.<br/>
	 * MessageFormat is supported, unless turned out using the 
	 * {@link #useMessageFormat()} attribute.<br/>
	 * MessageFormat allows '{1}' style expressions in the query
	 * to be replaced on runtime via method arguments.
	 * <br/>
	 */
	String value();

	/**
	 * Result set class.
	 * @see EntityManager#createNativeQuery(String, Class)
	 */
	Class<?> resultSetClass() default Void.class;
	
	/**
	 * Result set mapping name.
	 * @see EntityManager#createNativeQuery(String, String)
	 */
	String resultSetMapping() default "";

	/**
	 * If MessageFormat should be used with the SQL. If the sql uses
	 * some literals which do not allow MessageFormat to be used, set to false
	 * to disable the parameter substitution.
	 * @return
	 */
	boolean useMessageFormat() default true;

}
