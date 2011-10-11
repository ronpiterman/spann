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

package com.masetta.spann.spring.base.method;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;

/**
 * Create a bean by implementing an interface or an abstract class and delegate
 * the single abstract method to the method annotated by this annotation.
 * <p>
 * 
 * @author Ron Piterman
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Visitor(value=SyntheticAdapterVisitor.class,order=Order.CREATE_BEAN)
public @interface SyntheticAdapter {
	
	String IMPLEMENT_ATTRIBUTE = "implement";
	String CLASS_PROPERTIES_ATTRIBUTE = "classTypeProperties";
	String NAME_ATTRIBUTE = "adapterBeanName";


	/**
	 * The adapter class. This must be either either a single method interface
	 * or an abstract class with one abstract method.
	 * <p>
	 * The abstract method will be implemented by spring and delegate to this method.
	 * 
	 * @return
	 */
	Class<?> implement();
	
	/**
	 * The bean name of the adapter.
	 * The name is looked up in the annotation path, so any annotation
	 * annotated with {@link SyntheticAdapter} may also define an adapterBeanName
	 * attribute, which will be used, if set. 
	 */
	String adapterBeanName() default "";
	
	/**
	 * Indicate properties in the adapter class, which should be
	 * set according to the method signature (return type and parameter types).
	 * @return
	 */
	ClassTypeProperty[] classTypeProperties() default {};
	

}
