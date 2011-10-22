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

package com.masetta.spann.spring.util.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.masetta.spann.spring.ScanFinishedListener;
import com.masetta.spann.spring.util.Resolver;

/**
 * A Implement definition which uses a class resolver to resolve the class
 * of the bean on the first call to {@link #getBeanClass()}
 * @author Ron Piterman    
 */
public class LazyClassBeanDefinition extends GenericBeanDefinition implements ScanFinishedListener {
    
    private static final long serialVersionUID = -8464668783704788301L;

    private Resolver<Class<?>,BeanDefinition> classResolver;
    
    private Class<?> beanClass;
    
    public LazyClassBeanDefinition(BeanDefinition original, Resolver<Class<?>,BeanDefinition> beanClassResolver ) {
        super(original);
        this.classResolver = beanClassResolver;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        if ( beanClass == null ) {
            beanClass = this.classResolver.resolve( this );
        }
        return beanClass;  
    }

    public Resolver<Class<?>, BeanDefinition> getClassResolver() {
        return classResolver;
    }

    public void setClassResolver(Resolver<Class<?>, BeanDefinition> classResolver) {
        this.classResolver = classResolver;
    }

    public void scanFinished() {
        getBeanClass();
        setBeanClassName( this.beanClass.getCanonicalName() );
    }

}
