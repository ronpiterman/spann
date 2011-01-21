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

package com.masetta.spann.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.PriorityOrdered;

import com.masetta.spann.spring.core.ClassPathScanner;

/**
 * Extension of {@link ClassPathScanner} which implements spring's BeanFactoryPostProcessor.
 * 
 * @author Ron Piterman
 */
public class SpannBeanFactoryPostProcessor extends ClassPathScanner implements BeanFactoryPostProcessor, PriorityOrdered {
	
	private int order = 0;
	
	public SpannBeanFactoryPostProcessor() {
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		scan( (BeanDefinitionRegistry) beanFactory );
	}

	/**
	 * Implementation of PriorityOrdered, to define the order of this BeanFactoryPostProcessor.
	 * <p>
	 * The default value is 0. Spring's BeanFactoryPostProcessor implememntations are all
	 * using values around Integer.MAX_VALUE. This sets this post processor way before, 
	 * but still allows other BeanFactoryPostProcessors to prioritize before.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the priority of the BeanFactoryPostProcessor.
	 * <p>
	 * The default value is 0.
	 * 
	 * @param order 
	 * 
	 * @see getOrder()
	 */
	public void setOrder(int order) {
		this.order = order;
	}

}
