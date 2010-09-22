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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

public class ContextInitializer {
    
    private static final Map<String,ConfigurableApplicationContext> APPLICATION_CONTEXTS 
        = new HashMap<String, ConfigurableApplicationContext>();
    
    /**
     * Setup or retrieve a cached application context for the given xml locations.
     * 
     * Autowire the given test object - eithe by type, or by name.
     * 
     * @param test test object to autowire.
     * @param autowireMode autowire mode for the given test object 
     * @param xmlLocation spring xml configuration locations.
     * @return an ApplicationContext configured by the given xmlLocations.
     */
    public static ApplicationContext setupApplicationContext( Object test , int autowireMode, 
    		String ...xmlLocation ) {
    	
        String key = key( xmlLocation );
        ConfigurableApplicationContext ac = APPLICATION_CONTEXTS.get( key );
        if ( ac == null ) {
            ac = createApplicationContext( test, xmlLocation );
            APPLICATION_CONTEXTS.put( key , ac );
        }
        
        ac.getBeanFactory().autowireBeanProperties( test , 
                autowireMode, true );
        
        return ac;
    }
    
    public static ApplicationContext setupApplicationContext( Object test , int autowireMode ,
    		Resource beansXml ) {
    	GenericApplicationContext context = new GenericApplicationContext();
        createBeanDefinitionReader(context).loadBeanDefinitions(beansXml);
        context.refresh();
        context.getBeanFactory().autowireBeanProperties( test, autowireMode, true );
        return context;
    }
    
    public static void destroy() {
        for ( ConfigurableApplicationContext cac : APPLICATION_CONTEXTS.values() )
            cac.close();
    }

    private static ConfigurableApplicationContext createApplicationContext( Object test, 
            String[] xmlLocation) {
        
        GenericApplicationContext context = new GenericApplicationContext();
        createBeanDefinitionReader(context).loadBeanDefinitions(xmlLocation);
        context.refresh();
        return context;
        
    }
    
    protected static BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        return new XmlBeanDefinitionReader(context);
    }

    private static String key(String[] xmlLocation) {
        StringBuilder sb = new StringBuilder();
        for ( String s : xmlLocation )
            sb.append( s ).append("-//-");
        return sb.toString();
    }

}
