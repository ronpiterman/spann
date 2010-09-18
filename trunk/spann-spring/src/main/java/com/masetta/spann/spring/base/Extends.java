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
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Defines a base class for implementing an interface.<p>
 * 
 * If annotated on a class will assert that the class extends the given superclass, subclassRole
 * is then ignored.
 * 
 * @author Ron Piterman    
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Visitor(order=Order.AFTER_CREATE , value=ExtendsVisitor.class)
@Implement
public @interface Extends {
    
    String ROLE_ATTRIBUTE = "subclassRole";
    String SUPERCLASS_ATTRIBUTE = "superclass";

    /** 
     * Base class to use when implementing an interface.
     * 
     * @return
     */
    Class<?> superclass();
    
    String subclassRole() default "main";

}
