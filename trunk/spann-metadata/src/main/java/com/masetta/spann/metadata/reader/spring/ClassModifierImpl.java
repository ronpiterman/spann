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

package com.masetta.spann.metadata.reader.spring;

import org.springframework.asm.Opcodes;

import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.ClassType;

public class ClassModifierImpl extends ModifierImpl implements ClassModifier {
    
    private static final int TYPES = Opcodes.ACC_INTERFACE | Opcodes.ACC_ENUM | Opcodes.ACC_ANNOTATION;
    
    private static final int ANN = Opcodes.ACC_INTERFACE | Opcodes.ACC_ANNOTATION;
    
    public ClassModifierImpl(int modifier) {
        super(modifier);
    }

    public ClassType getClassType() {
        switch ( modifier & TYPES ) {
            case 0 : return ClassType.CLASS;
            case Opcodes.ACC_INTERFACE : return ClassType.INTERFACE;
            case ANN : return ClassType.ANNOTATION;
            case Opcodes.ACC_ENUM: return ClassType.ENUM;
        }
        throw new IllegalStateException("Unreachable");
    }

    public boolean isAbstract() {
        return (modifier & Opcodes.ACC_ABSTRACT) != 0;
    }

}
