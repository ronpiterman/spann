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

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SpringTestUtil {
    
    private static final Log log = LogFactory.getLog( SpringTestUtil.class );
    
    public static void outLog4JLocation( Class<?> cls ) {
        URL url = cls.getClassLoader().getResource("log4j.properties");
        System.out.println( "=============================================================================================" );
        System.out.println( "location of log4j.properties: " + url );
        System.out.println( "classloader: " + cls.getCanonicalName() );
        System.out.println( "=============================================================================================" );
        
    }
    
    

}
