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

package com.masetta.spann.spring.base.beanconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.spring.base.Attached;
import com.masetta.spann.spring.base.beanconfig.impl.Def;
import com.masetta.spann.spring.base.beanconfig.impl.Ignore;
import com.masetta.spann.spring.base.beanconfig.impl.Ref;
import com.masetta.spann.spring.core.annotations.Order;
import com.masetta.spann.spring.core.annotations.Visitor;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

/**
 * A Meta annotation for creating annotations which configure bean properties
 * of any bean attached to the current metadata. Via BeanConfig developers can easily customize
 * any beans created via other Annotations.
 * <p>
 * The BeanConfig annotation handler is using {@link AttributeHandler}s to handle the 
 * attribute of the annotation.
 * <p>
 * Handlers are configurable on annotation attribute level via the {@link AttributeHandlerDefinition} 
 * (meta-) annotation. This guarantees great flexibility while remaining very simple to use.
 * <p>
 * The {@link Ignore}, {@link Ref} and {@link Def} annotations are readily delivered here, and
 * allow to ignore an annotation attribute, to set bean references by name, and to 
 * mutate the bean definition itself (instead of the bean's properties), respectively.
 * <p>
 * <b>For example:</b>
 * <p>
 * Having the following bean class:
 * <pre>
 * public class Foo {
 * 	 ...
 *   public void setBar( Long bar ) {...}
 *   public void setBaz( Baz baz ) {...}
 * }
 * </pre>
 * 
 * The following annotation can be used to configure Foo:
 * 
 * <pre>
 * &#064;BeanProperties(...)
 * public &#064;interface FooDef {
 *   Long bar();
 *   &#064;Ignore int someOtherAttr();
 *   &#064;Ref String bazBeanName();
 *   &#064;Def String scope() default "singleton";
 * }
 * </pre>
 * 
 * @author Ron Piterman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Visitor(value=BeanConfigVisitor.class,order=Order.FINALIZE)
public @interface BeanConfig {
	
	String ATTACHED_ATTRIBUTE = "attached";
	String CREATE_ATTRIBUTE = "create";
	String EXPLICIT_ATTRIBUTE = "explicit";
	String REFERENCES_ATTRIBUTE = "references";
	String DEFAULT_ATTRIBUTE_HANDLER_ATTRIBUTE = "defaultAttributeHandler";
	
	/**
	 * The attache scope of the bean to configure.
	 * <p>
	 * If {@link #create()} is specified, the created bean will be
	 * attached to this scope and role.
	 * <p>
	 * If {@link #create()} is not specified, the configured bean
	 * will be looked up in the given role and the annotated element's scope (e.g. Method), 
	 * <b> the specified scope is thus ignored</b>.
	 * If no bean is attached to the given role and {@link #create()} is not specified,
	 * an {@link IllegalConfigurationException} is thrown.
	 */
	Attached attached();
	
	/**
	 * If specified, a bean of the given class will be created and attached
	 * to the role and scope specified by the {@link #attached()} attribute.
	 */
	Class<?> create() default Void.class;
	
	/**
	 * If explicit is true, only explicit set attributes of the annotation annotated by BeanConfig
	 * will be used as bean property values.
	 * If explicit is false, any default values set by the annotation class will be applied
	 * if not otherwise specified by the actual annotation.
	 * <p>
	 * For exmaple: having
	 * <code><pre>
	 * &#064;BeanConfig(...,explicit=true)
	 * public &#064;interface JmsContainer {
	 *    ...
	 *    long receiveTimeout() default 0;
	 * }
	 * </pre></code>
	 * Later when using the JmsContainer annotation, receiveTimeout will only be applied 
	 * if explicitly set in the annotation. The default value 0 will be ignored.
	 */
	boolean explicit() default true;
	
	/**
	 * Set collaborators created by spann, indicated by their attach scope and role.
	 */
	SpannReference[] references() default {};
	
	/**
	 * Default Handler of the annotation attributes. Every annotation attribute
	 * which is not itself annotated by {@link AttributeHandlerDefinition} 
	 * (on any meta-annotation level) will be handled by the given Handler.
	 * 
	 * @return
	 */
	Class<? extends AttributeHandler> defaultAttributeHandler() default DefaultAttributeHandler.class;
	
}
