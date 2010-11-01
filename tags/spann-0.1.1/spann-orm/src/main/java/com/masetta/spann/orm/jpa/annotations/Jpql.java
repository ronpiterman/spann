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

import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Perform a JPQL query.
 * 
 * @author Ron Piterman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Documented
@DaoMethod
@Visitor(value=JpqlVisitor.class,order=Order.AFTER_CREATE)
public @interface Jpql {
	
	/**
	 * The JPQL query to perform.<p>
	 * <p>
	 * DaoMethod parameters will be set from the method arguments.
	 * <p>
	 * The query is formatted with a MessageFormat, so it may include literal
	 * references to method parameter.
	 * <p>
	 * For example <code>FROM Entity AS e ORDER BY e.{0}</code> will order
	 * the query by the field name given by the first method argument.
	 * <p>
	 * When embedding parameters in the query, any parameter used is excluded from the
	 * query arguments.
	 * <p>
	 * Normal query parameters should <b>not</b> be passed as litral references, but normal
	 * JPQL indexed (<code>?</code>) or named (<code>:foo</code>) parameters.
	 * @return
	 */
	String value();

}
