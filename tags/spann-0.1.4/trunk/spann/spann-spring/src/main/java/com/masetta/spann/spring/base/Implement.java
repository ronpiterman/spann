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

package com.masetta.spann.spring.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.VisitMethods;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Same as spring's Component annotation, only it is not picked up by
 * spring but by spann, and may creates beans of abstract classes
 * and interfaces.
 * <p>
 * If using this annotation on an interface, the super class for the 
 * cglib interface implementation can be specified using the {@link Extends}
 * annotation.
 * 
 * @see Extends
 * 
 * @author Ron Piterman    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Visitor(order=Order.CREATE_BEAN , value=ImplementVisitor.class)
@VisitMethods
public @interface Implement {
    
    /** Implement name */
    String value() default "";
    
}
