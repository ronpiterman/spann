
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

import com.masetta.spann.metadata.core.modifier.MethodModifier;
public class MethodModifierImpl extends ModifierImpl implements MethodModifier {

    /**
     * <p>Constructor for MethodModifierImpl.</p>
     *
     * @param modifier a int.
     */
    public MethodModifierImpl(int modifier) {
        super(modifier);
    }

    /**
     * <p>isBridge</p>
     *
     * @return a boolean.
     */
    public boolean isBridge() {
        return (modifier & Opcodes.ACC_BRIDGE) != 0;
    }

    /**
     * <p>isSynchronized</p>
     *
     * @return a boolean.
     */
    public boolean isSynchronized() {
        return (modifier & Opcodes.ACC_SYNCHRONIZED) != 0;
    }

    /**
     * <p>isAbstract</p>
     *
     * @return a boolean.
     */
    public boolean isAbstract() {
        return (modifier & Opcodes.ACC_ABSTRACT) != 0;
    }

}
