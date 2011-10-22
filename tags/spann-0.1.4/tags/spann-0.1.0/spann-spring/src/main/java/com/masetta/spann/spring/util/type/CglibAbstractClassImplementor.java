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

import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;

public class CglibAbstractClassImplementor extends AbstractCglibImplementor {

    public Class<?> implement(Class<?> cls) {
        if ( cls.isInterface() )
            throw new IllegalArgumentException( cls.getCanonicalName() + " is an interface.");
        
        // if not abstract just return the class.
        if ( ! Modifier.isAbstract( cls.getModifiers() ) )
            return cls;
        
        // also if no additional interfaces should be implemented, or any
        // special callbacks be added, just return the abstract class.
        final Class<?>[] additionalInterfaces = getAdditionalInterfaces();
        if ( isEmpty( additionalInterfaces ) && isEmpty( getCallbackTypes() ) )
            return cls;
        
        Enhancer e = new Enhancer();
        e.setSuperclass( cls );
        if ( ! isEmpty( additionalInterfaces ) )
            e.setInterfaces( additionalInterfaces );
        
        setupCallbacks( e );
        return e.createClass();
    }

}
