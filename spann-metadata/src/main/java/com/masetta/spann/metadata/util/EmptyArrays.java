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

import java.lang.reflect.Method;

public final class EmptyArrays {
	
	private EmptyArrays() {}
    
    public static final Class<?>[] CLASS = {};

    public static final Object[] OBJECT = {};

    public static final boolean[] BOOLEAN = {};

	public static final Method[] METHOD = {};

    public static final short[] SHORT = {}; 
    
    public static final char[] CHAR = {};
    
    public static final byte[] BYTE = {};
    
    public static final int[] INT = {};
    
    public static final long[] LONG = {};
    
    public static final float[] FLOAT = {};
    
    public static final double[] DOUBLE = {};
    
    public static final String[] STRING = {};

	public static final Enum<?>[] ENUM = {};
    
}
