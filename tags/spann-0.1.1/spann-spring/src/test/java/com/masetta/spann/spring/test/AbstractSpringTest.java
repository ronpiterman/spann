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

package com.masetta.spann.spring.test;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public abstract class AbstractSpringTest {
    
    private ApplicationContext applicationContext;
    
    @BeforeSuite(alwaysRun=true)
    public void outputLog4jPropertiesLocation() {
        SpringTestUtil.outLog4JLocation( getClass() );
    }
    
    @BeforeSuite(alwaysRun=true)
    public void outputClasspath() {
        String cp = System.getProperty ("java.class.path");
        String[] paths = cp.split(":" );
        for ( String p : paths ) {
            if ( ! p.endsWith(".jar" ) ) 
                System.out.println( p );
        }
    }
    
    protected void afterApplicationContextSet() {
    }
    
    @BeforeClass(alwaysRun=true)
    public void setup() {
        final ApplicationContext ac = createApplicationContext();
        setup(ac);
    }

	protected ApplicationContext createApplicationContext() {
		return ContextInitializer.setupApplicationContext(
                this, getAutowireMode(), getConfigLocations() );
	}

	protected void setup(final ApplicationContext ac) {
		this.applicationContext = ac;
        afterApplicationContextSet();
	}

    protected abstract String[] getConfigLocations();
    
    protected int getAutowireMode() {
        return AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    public Resource getResource( String string ) {
        return this.applicationContext.getResource( string );
    }
    
    public URL getResourceUrl( String string ) {
        Resource r = getResource( string );
        if ( r == null )
            return null;
        try {
            return r.getURL();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
    
    
}
