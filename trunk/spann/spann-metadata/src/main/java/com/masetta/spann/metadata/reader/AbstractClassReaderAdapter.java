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

package com.masetta.spann.metadata.reader;

import java.util.HashMap;
import java.util.Map;

import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.core.modifier.MethodModifier;

/**
 * Base class for implementing ClassReaderAdapter, implementing reuse of modifier.
 * 
 * @author Ron Piterman
 */
public abstract class AbstractClassReaderAdapter implements ClassReaderAdapter {
    
	/**
	 * Modifier cache.
	 */
    private final Map<String,Object> modifiers = new HashMap<String, Object>();
    
    /**
     * {@inheritDoc}
     * Caches modifier and delegates to the abstract createClassModifierInternal.
     */
    public ClassModifier createClassModifier(int argument) {
        Object o = modifiers.get( "c" + argument );
        if ( o == null ) {
            o = createClassModifierInternal(argument);
            modifiers.put( "c" + argument , o );
        }
        return (ClassModifier) o;
    }

    protected abstract ClassModifier createClassModifierInternal(int argument);

    /**
     * {@inheritDoc}
     * Caches modifier and delegates to the abstract createFieldModifierInternal.
     */
    public FieldModifier createFieldModifier(int argument) {
        Object o = modifiers.get( "f" + argument );
        if ( o == null ) {
            o = createFieldModifierInternal(argument);
            modifiers.put( "f" + argument , o );
        }
        return (FieldModifier) o;
    }

    protected abstract FieldModifier createFieldModifierInternal(int argument);

    /**
     * {@inheritDoc}
     * Caches modifier and delegates to the abstract createMethodModifierInternal.
     */
    public MethodModifier createMethodModifier(int argument) {
        Object o = modifiers.get( "m" + argument );
        if ( o == null ) {
            o = createMethodModifierInternal(argument);
            modifiers.put( "m" + argument , o );
        }
        return (MethodModifier) o;
    }

    protected abstract Object createMethodModifierInternal(int argument);
    
    

}
