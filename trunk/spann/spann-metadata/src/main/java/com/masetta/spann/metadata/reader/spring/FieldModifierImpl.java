
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

package com.masetta.spann.metadata.reader.spring;

import org.springframework.asm.Opcodes;

import com.masetta.spann.metadata.core.modifier.FieldModifier;
public class FieldModifierImpl extends ModifierImpl implements FieldModifier {

    /**
     * <p>Constructor for FieldModifierImpl.</p>
     *
     * @param modifier a int.
     */
    public FieldModifierImpl(int modifier) {
        super(modifier);
    }

    /**
     * <p>isTrasient</p>
     *
     * @return a boolean.
     */
    public boolean isTrasient() {
        return (modifier & Opcodes.ACC_TRANSIENT) != 0;
    }

    /**
     * <p>isVolatile</p>
     *
     * @return a boolean.
     */
    public boolean isVolatile() {
        return (modifier & Opcodes.ACC_VOLATILE) != 0;
    }

}
