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

import java.io.IOException;
import java.io.InputStream;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.core.modifier.MethodModifier;
import com.masetta.spann.metadata.reader.asm3_2.AsmClassReaderAdapter;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.visitors.ClassVisitorImpl;
import com.masetta.spann.metadata.visitors.SignatureVisitorImpl;

/**
 * Adapter to a version of asm.
 * 
 * Allows asm to be used form different package and/or api versions. Specifically used to make
 * spann-metadata portable between asm dependency (org.objectweb.asm) and spring dependency 
 * (which includes asm in an older version than 3.2 under org.springframework.asm ).
 * 
 * @see SpringClassReaderAdapter
 * @see AsmClassReaderAdapter
 * 
 * @author Ron Piterman    
 */
public interface ClassReaderAdapter {
    
    <T> VisitorAdapter<T> createVisitorAdapter( T t );
    
    /**
     * What to use as empty visitor for the given artifact.
     * <p>
     * May return null if the implementation accepts a null.
     * <p>
     * The 'empty visitor' was introduced because spring's ASM implementation 
     * does not accept null as AnnotationVisitor, so an empty visitor must be used for
     * all visitAnnotation methods.
     * <p>
     * For axample, ASM 3.2 adapter just returns null, 
     * since ASM 3.2 always allows to return null visitors. 
     * 
     * @param <T>
     * @param t
     * @return
     */
    VisitorAdapter<?> createEmptyVisitor( Artifact artifact );
    
    void readClass( InputStream in , VisitorAdapter<ClassVisitorImpl> visitor ) throws IOException;
    
    void readSignature( String signature, VisitorAdapter<SignatureVisitorImpl> signatureVisitor );
    
    ClassModifier createClassModifier( int modifier );
    
    MethodModifier createMethodModifier( int modifier );
    
    FieldModifier createFieldModifier( int modifier );

    ClassMetadata getMethodReturnType( ClassMetadataResolver resolver, String desc );

    ClassMetadata[] getMethodArguments( ClassMetadataResolver resolver, String desc );

    ClassMetadata resolveDescriptor( ClassMetadataResolver resolver, String desc, boolean ignoreArray);

    String getClassForBaseType(char descriptor);

    Object resolveAttributeValue(ClassMetadataResolver resolver, Object value);

    GenericCapture resolveCapture(char wildcard);

}
