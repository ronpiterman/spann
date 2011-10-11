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

package com.masetta.spann.metadata.util;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create a log, trying to use slf4j, commons-logging or juli.
 * 
 * @author Ron Piterman
 */
public final class SpannLogFactory {
	
	private static SpannLog log = getLog( SpannLogFactory.class );
	
	private SpannLogFactory() {}
	
	public static SpannLog getLog(Class<?> cls) {
		String cn = cls.getCanonicalName();
		if ( cn == null ) {
			return getLog( cls.getEnclosingClass() );
		}
		return getLog( cn );
	}

    public static SpannLog getLog(String name) {
        try {
            return Slf4JLogFactory.createLog(name);
        } catch (Throwable t) {
        }
        try {
            return CommonsLogFactory.creatLog(name);
        } catch (Throwable t) {
        }
        try {
            return JdkLogFactory.createLog(name);
        } catch (Throwable t) {
        }
        throw new RuntimeException("No log adapter found.");
    }

    private static class CommonsLogFactory {
    	private CommonsLogFactory() {}
        public static SpannLog creatLog( String name ) {
            return new CommonsLog( LogFactory.getLog( name ) );
        }
    }

    private static class Slf4JLogFactory {
    	private Slf4JLogFactory() {}
        public static SpannLog createLog(String name) {
            return new Slf4JLog( LoggerFactory.getLogger( name ) );
        }
    }

    private static class JdkLogFactory {
    	private JdkLogFactory() {}
        public static SpannLog createLog(String name) {
            return new JdkLog( LogManager.getLogManager().getLogger( name ) );
        }
    }
    
    public static Object createTraceProxy( Object target , SpannLog lg, Class<?> ...interfaces ) {
    	SpannLogFactory.log.debug("Creating trace proxy for " + target );
    	SpannLog traceLog = lg == null ? getLog( target.getClass() ) : lg;
    	if ( ! traceLog.isTraceEnabled() )
    		return target;
    	
    	return Proxy.newProxyInstance( interfaces[0].getClassLoader(), interfaces, new 
    			TraceInvocationHandler( target , null ) );
    }
    
    private static class CommonsLog implements SpannLog {
        private final Log log;
        public CommonsLog(Log log) { this.log = log; }

        public void debug(Object message) { log.debug(message); }

        public void info(Object message) { log.info(message); }

        public boolean isDebugEnabled() { return log.isDebugEnabled(); }

        public boolean isInfoEnabled() { return log.isInfoEnabled(); }

        public boolean isTraceEnabled() { return log.isTraceEnabled(); }

        public void trace(Object message) { log.trace(message); }

		public void warn( Object message) { log.warn( message ); }
        
    }
    
    private static class Slf4JLog implements SpannLog {
        private final Logger log;
        public Slf4JLog(Logger log) { this.log = log; }

        public void debug(Object message) { log.debug(String.valueOf( message) ); }

        public void info(Object message) { log.info( String.valueOf(message) ); }

        public boolean isDebugEnabled() { return log.isDebugEnabled(); }

        public boolean isInfoEnabled() { return log.isInfoEnabled(); }

        public boolean isTraceEnabled() { return log.isTraceEnabled(); }

        public void trace(Object message) { log.trace( String.valueOf(message) ); }
        
        public void warn( Object message) { log.warn( String.valueOf( message ) ); }
        
    }
    
    private static class JdkLog implements SpannLog {
        private final java.util.logging.Logger log;
        public JdkLog(java.util.logging.Logger log) { this.log = log; }

        public void debug(Object message) { log.fine( String.valueOf( message) ); }

        public void info(Object message) { log.info( String.valueOf(message) ); }

        public boolean isDebugEnabled() { return log.isLoggable( Level.FINE ); }

        public boolean isInfoEnabled() { return log.isLoggable( Level.INFO );  }

        public boolean isTraceEnabled() { return log.isLoggable( Level.FINEST );  }

        public void trace(Object message) { log.finest( String.valueOf(message) ); }
        
        public void warn( Object message) { log.warning( String.valueOf( message ) ); }
        
    }

}
