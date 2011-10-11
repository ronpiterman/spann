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

package com.masetta.spann.metadata.visitors;

import java.io.IOException;
import java.io.InputStream;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.core.modifier.MethodModifier;
import com.masetta.spann.metadata.core.support.ClassMetadataResolver;
import com.masetta.spann.metadata.reader.ClassReaderAdapter;
import com.masetta.spann.metadata.reader.VisitorAdapter;

/**
 * Intermediary between visitor classes and the asm implementation adapter.
 * 
 * @author Ron Piterman
 */
class VisitorDelegate {
    
    private final ClassReaderAdapter classReaderAdapter; 
    
    private final ClassMetadataResolver resolver;
    
    VisitorDelegate(ClassReaderAdapter classReaderAdapter,
            ClassMetadataResolver resolver) {
        super();
        this.classReaderAdapter = classReaderAdapter;
        this.resolver = resolver;
    }

    ClassModifier createClassModifier(int access) {
        return classReaderAdapter.createClassModifier( access );
    }

    MethodModifier createMethodModifier(int access) {
        return classReaderAdapter.createMethodModifier( access );
    }

    FieldModifier createFieldModifier(int access) {
        return classReaderAdapter.createFieldModifier( access );
    }

    ClassMetadata getMethodReturnType(String desc) {
        return classReaderAdapter.getMethodReturnType(resolver, desc);
    }

    ClassMetadata[] getMethodArguments(String desc ) {
        return classReaderAdapter.getMethodArguments(resolver, desc);
    }
    
    ClassMetadata resolveDescriptor(String desc, boolean ignoreArray) {
        return classReaderAdapter.resolveDescriptor( resolver, desc, ignoreArray );
    }

    String getClassForBaseType(char descriptor) {
        return classReaderAdapter.getClassForBaseType(descriptor);
    }

    Object resolveAttributeValue(Object value) {
        return classReaderAdapter.resolveAttributeValue( resolver , value );
    }
    
    GenericCapture resolveCapture(char wildcard) {
        return classReaderAdapter.resolveCapture( wildcard );
    }

    void readClass(InputStream in, VisitorAdapter<ClassVisitorImpl> visitor)
            throws IOException {
        classReaderAdapter.readClass(in, visitor);
    }

}
