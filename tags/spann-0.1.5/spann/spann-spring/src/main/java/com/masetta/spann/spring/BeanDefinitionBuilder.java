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

package com.masetta.spann.spring;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.masetta.spann.metadata.common.Artifact;


/**
 * Convenience builder for a bean definition.
 * 
 * @author Ron Piterman
 *
 */
public interface BeanDefinitionBuilder {
	
	/**
	 * Retrieve the bean property value of the given property.
	 * @param property
	 * @return the property name of the given property.
	 */
	Object get( String property );
	
	/**
	 * Set the given bean property to the given property value.
	 * 
	 * @param property property name to set
	 * @param value property value
	 * 
	 * @return this builder
	 */
	BeanDefinitionBuilder set( String property , Object value );
	
	/**
	 * Set a property to a bean with the given classname attached to the given scope and role.
	 * If no such bean is found, a new one is first created and attached, and used
	 * as property value.
	 * 
	 * @param property
	 * @param scope
	 * @param classname
	 * @param role
	 * @return this builder
	 */
	BeanDefinitionBuilder setReference( String property , Artifact scope , String classname , String role );
	
	/**
	 * Set the given bean (by name) to the given property.
	 * 
	 * @param property the name of the property to set
	 * @param beanname the name of the bean to set
	 * 
	 * @return this builder.
	 */
	BeanDefinitionBuilder setReference( String property , String beanname );
	
	/**
	 * Create a bean name for the bean. May not be called after {@link #setBeanName(String)}
	 * 
	 * @return this builder.
	 */
	BeanDefinitionBuilder generateBeanName();
	
	/**
	 * Set the given bean name to the bean. 
	 * May not be called after {@link #generateBeanName()}.
	 * If the given name is null, a name will be generated via {@link #generateBeanName()}.
	 * 
	 * @param name The bean's name.
	 * @return this builder.
	 */
	BeanDefinitionBuilder setBeanName( String name );
	
	/**
	 * Add the given constructor argument to the bean definition.
	 * 
	 * @param value
	 * @return this builder
	 */
	BeanDefinitionBuilder addConstructorArgument( Object value );
	
	/**
	 * Attach the bean to the context and return the BeanDefintionHolder.
	 * <p>
	 * If no bean name was set, a new bean name is created.
	 * <p>
	 * Note that calling this method will finalize the bean builder. Any call to a mutating method 
	 * (set... or add...) after calling {@link #attach(Artifact, String)} will throw an IllegalStateException.
	 * 
	 * @param scope
	 * @param role
	 * @return The BeanDefinitionHolder that was built.
	 */
	BeanDefinitionHolder attach( Artifact scope , String role );
	
	/**
	 * Add the bean definition to the container without attaching it.
	 * <p>
	 * If no bean name was set, a new bean name is created.
	 * <p>
	 * Note that calling this method will finalize the bean builder. Any call to a mutating method after
	 * calling {@link #addFinal()} will throw an IllegalStateException.
	 * 
	 * @return The BeanDefinitionHolder that was built.
	 */
	BeanDefinitionHolder addFinal();


}
