
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
public class EnumValue {
    
    private final String value;
    
    private final ClassMetadata enumType;

    /**
     * <p>Constructor for EnumValue.</p>
     *
     * @param enumType a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     * @param value a {@link java.lang.String} object.
     */
    public EnumValue(ClassMetadata enumType, String value) {
        super();
        this.enumType = enumType;
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Getter for the field <code>enumType</code>.</p>
     *
     * @return a {@link com.masetta.spann.metadata.core.ClassMetadata} object.
     */
    public ClassMetadata getEnumType() {
        return enumType;
    }
    
    /**
     * <p>resolve</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param <T> a T object.
     * @return a T object.
     */
    public <T extends Enum<T>> T resolve( Class<T> type ) {
    	return Enum.valueOf( type, value ); 
    }
    
}
