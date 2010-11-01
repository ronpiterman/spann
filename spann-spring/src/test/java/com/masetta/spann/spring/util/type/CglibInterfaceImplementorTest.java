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

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.masetta.spann.spring.util.type.CglibInterfaceImplementor;

public class CglibInterfaceImplementorTest {
    
    private SomeInterface impl;
    
    @BeforeClass
    public void createInstance() {
        this.impl = create();
    }
    
    @Test
    public void testCreate() {
        Assert.assertNotNull( this.impl );
    }
    
    @Test(expectedExceptions=AbstractMethodError.class)
    public void testMethods() {
        impl.doSomething();
    }
    
    @Test
    public void testToString() {
        System.out.println( impl.toString() );
    }

    private SomeInterface create() {
        Class cls = new CglibInterfaceImplementor().implement( SomeInterface.class );
        Assert.assertNotNull( cls );
        SomeInterface si = (SomeInterface) instanciate( cls );
        return si;
    }
    
    public static <T> T instanciate(Class<T> cls) {
        try {
            return (T) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException( e );
        } catch (IllegalAccessException e) {
            throw new RuntimeException( e );
        }
    }

    private static interface SomeInterface {
        void doSomething();
    }

}
