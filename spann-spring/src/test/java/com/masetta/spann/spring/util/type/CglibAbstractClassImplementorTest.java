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

import com.masetta.spann.spring.util.type.CglibAbstractClassImplementor;

public class CglibAbstractClassImplementorTest {
    
    private AbstractClass instance;
    
    @BeforeClass
    public void create() {
        Class cls = new CglibAbstractClassImplementor().implement( AbstractClass.class );
        this.instance = (AbstractClass) CglibInterfaceImplementorTest.instanciate( cls );
    }
    
    @Test
    public void testCreate() {
        Assert.assertNotNull( instance , "created" );
    }
    
    @Test
    public void testMethodCall() {
        Assert.assertEquals( instance.getBla(), "bla" , "normal method call.");
    }
    
    @Test(expectedExceptions=AbstractMethodError.class )
    public void testAbstractMethodCall() {
        instance.doNothing();
    }


    
    public static abstract class AbstractClass {
        public String getBla() {
            return "bla";
        }
        public abstract void doNothing();
        
    }

}
