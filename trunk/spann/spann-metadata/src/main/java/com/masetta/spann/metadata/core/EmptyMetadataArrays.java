
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

package com.masetta.spann.metadata.core;

import java.util.HashMap;
import java.util.Map;

import com.masetta.spann.metadata.util.EmptyArrays;
public final class EmptyMetadataArrays {
    
    /** Constant <code>CLASS_METADATA</code> */
    public static final ClassMetadata[] CLASS_METADATA = {};
    
    /** Constant <code>ENUM_VALUE</code> */
    public static final EnumValue[] ENUM_VALUE = {};
    
    /** Constant <code>ANNOTATION_METADATA</code> */
    public static final AnnotationMetadata[] ANNOTATION_METADATA = {};
    
    private static final Map<Class<?>,Object> EMPTY_ARRAYS = emptyArrays();
    
    private EmptyMetadataArrays() {}
    
    /**
     * <p>getEmptyArray</p>
     *
     * @param componentType a {@link java.lang.Class} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object getEmptyArray( Class<?> componentType ) {
    	return EMPTY_ARRAYS.get( componentType );
    }
    
    @SuppressWarnings("unchecked")
    private static Map<Class<?>, Object> emptyArrays() {
        return map( 
            Boolean.TYPE , EmptyArrays.BOOLEAN,
            Integer.TYPE , EmptyArrays.INT,
            Long.TYPE , EmptyArrays.LONG, 
            Short.TYPE , EmptyArrays.SHORT,
            Byte.TYPE , EmptyArrays.BYTE, 
            Character.TYPE , EmptyArrays.CHAR,
            Float.TYPE , EmptyArrays.FLOAT, 
            Double.TYPE , EmptyArrays.DOUBLE,
            String.class ,  EmptyArrays.STRING, 
            ClassMetadata.class , EmptyMetadataArrays.CLASS_METADATA,
            EnumValue.class , EmptyMetadataArrays.ENUM_VALUE,
            AnnotationMetadata.class , EmptyMetadataArrays.ANNOTATION_METADATA
    	);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map map( Object ...values ) {
        Map map = new HashMap( values.length / 2);
        for ( int i = 0; i < values.length ;)
            map.put( values[i++], values[i++] );
        return map;
    }

}
