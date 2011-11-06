
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

import java.lang.reflect.Method;
public final class EmptyArrays {
	
	private EmptyArrays() {}
    
    /** Empty class array */
    public static final Class<?>[] CLASS = {};

    /** Empty object array */
    public static final Object[] OBJECT = {};

    /** Empty boolean array */
    public static final boolean[] BOOLEAN = {};

	/** Empty method array */
	public static final Method[] METHOD = {};

    /** Empty short array */
    public static final short[] SHORT = {}; 
    
    /** Empty char array */
    public static final char[] CHAR = {};
    
    /** Empty byte array */
    public static final byte[] BYTE = {};
    
    /** Empty int array */
    public static final int[] INT = {};
    
    /** Empty Integer array */
    public static final Integer[] INTEGER = {};
    
    /** Empty long array */
    public static final long[] LONG = {};
    
    /** Empty float array */
    public static final float[] FLOAT = {};
    
    /** Empty double array */
    public static final double[] DOUBLE = {};
    
    /** Empty String array */
    public static final String[] STRING = {};

	/** Empty Enum array */
	public static final Enum<?>[] ENUM = {};
    
}
