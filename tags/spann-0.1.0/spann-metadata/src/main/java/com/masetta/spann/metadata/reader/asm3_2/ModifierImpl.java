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

package com.masetta.spann.metadata.reader.asm3_2;

import org.objectweb.asm.Opcodes;

import com.masetta.spann.metadata.core.modifier.Access;
import com.masetta.spann.metadata.core.modifier.Modifier;

public class ModifierImpl implements Modifier{
    
    private final static int ACCESS = Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_PUBLIC; 
    
    protected final int modifier;
    
    public ModifierImpl(int modifier) {
        super();
        this.modifier = modifier;
    }

    public Access getAccess() {
        switch ( modifier & ACCESS ) {
            case 0 : return Access.DEFAULT;
            case Opcodes.ACC_PUBLIC : return Access.PUBLIC;
            case Opcodes.ACC_PRIVATE : return Access.PRIVATE;
            case Opcodes.ACC_PROTECTED : return Access.PROTECTED;
        }
        throw new IllegalStateException("Unreachanble");
    }

    public boolean isFinal() {
        return ( modifier & Opcodes.ACC_FINAL ) != 0;
    }

    public boolean isStatic() {
        return ( modifier & Opcodes.ACC_STATIC ) != 0;
    }

    public boolean isSynthetic() {
        return ( modifier & Opcodes.ACC_SYNTHETIC ) != 0;
    }

}
