
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
    
    /** Constant <code>CLASS</code> */
    public static final Class<?>[] CLASS = {};

    /** Constant <code>OBJECT</code> */
    public static final Object[] OBJECT = {};

    /** Constant <code>BOOLEAN={}</code> */
    public static final boolean[] BOOLEAN = {};

	/** Constant <code>METHOD</code> */
	public static final Method[] METHOD = {};

    /** Constant <code>SHORT={}</code> */
    public static final short[] SHORT = {}; 
    
    /** Constant <code>CHAR={}</code> */
    public static final char[] CHAR = {};
    
    /** Constant <code>BYTE={}</code> */
    public static final byte[] BYTE = {};
    
    /** Constant <code>INT={}</code> */
    public static final int[] INT = {};
    
    /** Constant <code>LONG={}</code> */
    public static final long[] LONG = {};
    
    /** Constant <code>FLOAT={}</code> */
    public static final float[] FLOAT = {};
    
    /** Constant <code>DOUBLE={}</code> */
    public static final double[] DOUBLE = {};
    
    /** Constant <code>STRING="{}"</code> */
    public static final String[] STRING = {};

	/** Constant <code>ENUM</code> */
	public static final Enum<?>[] ENUM = {};
    
}
