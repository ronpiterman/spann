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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Repository;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.orm.jpa.beans.BaseDaoImpl;
import com.masetta.spann.spring.base.Extends;
import com.masetta.spann.spring.base.Implement;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.impl.Ignore;
import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.VisitMethods;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Annotation for DAO interface or 
 * @author Ron Piterman
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Repository
@Implement
@VisitMethods
@Extends(superclass=BaseDaoImpl.class)
@Visitor(value=DaoVisitor.class,order=Order.AFTER_CREATE)
@BeanConfig(attached=@Attached(role="main",scope=Artifact.CLASS))
public @interface Dao {
	
	/** Dao Implement name */
	@Ignore String value() default "";
	
	String persistenceUnit() default "";

}
