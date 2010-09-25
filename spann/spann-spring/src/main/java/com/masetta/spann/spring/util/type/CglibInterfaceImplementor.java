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

package com.masetta.spann.spring.util.type;

import net.sf.cglib.proxy.Enhancer;

import com.masetta.spann.spring.util.Factory;

public class CglibInterfaceImplementor extends AbstractCglibImplementor implements InterfaceImplementor {
    
    private Factory<Class<?>> superclass;
    
    public Class<?> implement( final Class<?> ifc ) {
        if ( ! ifc.isInterface() ) {
            throw new IllegalArgumentException( ifc.getCanonicalName() + " is not an interface." );
        }
        
        Enhancer enhancer = new Enhancer();
        Class<?> supercls = superclass == null ? Object.class : superclass.create();    
        enhancer.setSuperclass( supercls );
        enhancer.setInterfaces( getInterfaces( ifc ) );
        
        setupCallbacks(enhancer);
        return enhancer.createClass();
    }

    /** 
     * Set the superclass to use when implementing an interface.
     * @param superclass
     */
    public void setSuperclass( Factory<Class<?>> classFactory ) {
        if ( classFactory == null )
            throw new IllegalArgumentException("Superclass may not be null.");
        this.superclass = classFactory;
    }

}
