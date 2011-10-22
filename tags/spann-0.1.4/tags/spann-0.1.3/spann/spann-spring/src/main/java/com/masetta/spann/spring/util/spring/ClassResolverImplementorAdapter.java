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

import com.masetta.spann.spring.util.Resolver;
import com.masetta.spann.spring.util.type.Implementor;

/**
 * Adapter between ClassResolver (user by {@link LazyClassBeanDefinition})
 * and the {@link Implementor} interface.
 * 
 * @author Ron Piterman    
 */
public class ClassResolverImplementorAdapter implements Resolver<Class<?>,BeanDefinition> {
    
    private Implementor implementor;
    
    private ClassLoader classLoader;
    
    public ClassResolverImplementorAdapter(Implementor implementor,
            ClassLoader classLoader) {
        super();
        this.implementor = implementor;
        this.classLoader = classLoader;
    }

    public Class<?> resolve(BeanDefinition param) {
        Class<?> ifc = loadClass( param.getBeanClassName() );
        return this.implementor.implement( ifc );
    }
    
    private Class<?> loadClass(String ifcName) {
        try {
            return classLoader.loadClass( ifcName );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException( "Could not load class " + ifcName , e );
        }
    }

    public Implementor getImplementor() {
        return implementor;
    }

}
