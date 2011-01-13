
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
 *
 * @author rpt
 * @version $Id: $
 */

package com.masetta.spann.metadata.util;

/**
 * What should be used? slf4j? commons-logging? just another log wrapper...
 * @author Ron Piterman
 *
 */
public interface SpannLog {
    
    /**
     * <p>isInfoEnabled</p>
     *
     * @return a boolean.
     */
    boolean isInfoEnabled();
    
    /**
     * <p>isDebugEnabled</p>
     *
     * @return a boolean.
     */
    boolean isDebugEnabled();
    
    /**
     * <p>isTraceEnabled</p>
     *
     * @return a boolean.
     */
    boolean isTraceEnabled();
    
    /**
     * <p>info</p>
     *
     * @param object a {@link java.lang.Object} object.
     */
    void info( Object object );
    
    /**
     * <p>debug</p>
     *
     * @param object a {@link java.lang.Object} object.
     */
    void debug( Object object );
    
    /**
     * <p>trace</p>
     *
     * @param object a {@link java.lang.Object} object.
     */
    void trace( Object object );

	/**
	 * <p>warn</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 */
	void warn( Object object );

}
