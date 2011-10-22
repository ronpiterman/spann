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

package com.masetta.spann.orm.hibernate.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.orm.jpa.annotations.DaoMethod;
import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
@Visitor(value=EnableFiltersVisitor.class,order=Order.AFTER_CREATE)
@DaoMethod
@Documented
public @interface EnableFilter {
	
	String NAME_ATTRIBUTE = "name";
	
	String PARAMETERS_ATTRIBUTE = "parameters";
	
	/** The name of the filter to activate */
	String name();
	
	FilterParam[] parameters() default {};
	
}
