
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

/**
 * Represents an Enum value, used as an annotation attibute value.
 * 
 * @author Ron Piterman
 */
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
     * Retrieve the value-name of this enum value object.
     *
     * @return the name of the enum constant represented by this enum value object.
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieve the class metadata of the enum type represented by this value.
     *
     * @return a ClassMetadata of the enum type to which the value represented by the object belongs.
     */
    public ClassMetadata getEnumType() {
        return enumType;
    }
    
    /**
     * Resolve this enum value to the java Enum object.
     *
     * @param type the Enum Type, must be the same as the class represented by this
     * value's enumType property.
     * 
     * @return the java enum value represented by this object.
     */
    public <T extends Enum<T>> T resolve( Class<T> type ) {
    	return Enum.valueOf( type, value ); 
    }
    
}
