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
import com.masetta.spann.metadata.core.support.ClassMetadataResolver;
import com.masetta.spann.metadata.reader.asm3_2.AsmClassReaderAdapter;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.visitors.ClassVisitorImpl;
import com.masetta.spann.metadata.visitors.SignatureVisitorImpl;

/**
 * Adapter to a version of asm.<p>
 * 
 * Allows asm to be used form different packages and/or api versions. Specifically used to make
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
     * Retrieve an empty visitor for the given artifact.
     * <p>
     * May return null if the implementation accepts a null.
     * <p>
     * The 'empty visitor' was introduced mainly to support spring's ASM implementation (probably 
     * based on asm 2.x) which does not accept null as AnnotationVisitor, so an empty visitor must be used for
     * all visitAnnotation methods.
     * <p>
     * (ASM 3.2 adapter will always returns null, since the 3.2 implementation is null safe) 
     * 
     * @param artifact
     * @return a visitor to use as empty visitor or null, if supported by the asm implementation
     * 	underlying this adapter.
     */
    VisitorAdapter<?> createEmptyVisitor( Artifact artifact );
    
    /**
     * Visit the class underlying the given input stream, using the given visitor
     * 
     * @param in
     * @param visitor
     * 
     * @throws IOException
     */
    void readClass( InputStream in , VisitorAdapter<ClassVisitorImpl> visitor ) throws IOException;
    
    /**
     * Visit the given signature, using the given visitor.
     * @param signature
     * @param signatureVisitor
     */
    void readSignature( String signature, VisitorAdapter<SignatureVisitorImpl> signatureVisitor );
    
    /**
     * Create a class modifier for the given flag-integer.
     * <br>
     * The integer is asm implementation specific and therefore should be
     * resolved by the asm adapter.
     * 
     * @param modifier an integer representing the class modifier.
     * 
     * @return a ClassModifier representing the given flag-integer.
     */
    ClassModifier createClassModifier( int modifier );
    
    /**
     * Create a method modifier for the given flag-integer.
     * <br>
     * The integer is asm implementation specific and therefore should be
     * resolved by the asm adapter.
     * 
     * @param modifier an integer representing the method modifier.
     * 
     * @return a MethodModifier representing the given flag-integer.
     */
    MethodModifier createMethodModifier( int modifier );
    
    /**
     * Create a field modifier for the given flag-integer.
     * <br>
     * The integer is asm implementation specific and therefore should be
     * resolved by the asm adapter.
     * 
     * @param modifier an integer representing the field modifier.
     * 
     * @return a FieldModifier representing the given flag-integer.
     */
    FieldModifier createFieldModifier( int modifier );

    /**
     * Extract a class name and dimensions from the given desc, as provided by
     * asm and resolve it to a ClassMetadata using the given resolver.
     * 
     * @param resolver a resolver to resolve a class name.
     * @param desc a class description, as provided by a visit method from asm.
     * 
     * @return the resolved ClassMetadata for the given description.
     */
    ClassMetadata getMethodReturnType( ClassMetadataResolver resolver, String desc );

    /**
     * Extract class names and dimensions from the given method signature and resolve
     * them to ClassMetadata using the given resolver.
     * 
     * @param resolver a resolver to resolve class name and dimensions to a class metadata.
     * @param desc method signature, as given by asm to a visitor callback method. 
     * @return
     */
    ClassMetadata[] getMethodArguments( ClassMetadataResolver resolver, String desc );

    /**
     * Resolve the given descriptor to a class metadata, optionally ignoring array dimensions.
     * 
     * @param resolver resolver of class-name and array dimensions to a class metadata.
     * @param desc a class description, as provided by asm to a visitor callback method.
     * @param ignoreArray if to ignore array dimensions as provided by the description and always
     * 	use 0.
     * 
     * @return ClassMetadata representing the given description, optionally ignoring array dimensions.
     */
    ClassMetadata resolveDescriptor( ClassMetadataResolver resolver, String desc, boolean ignoreArray);

    /**
     * Return a class name for a primitive java type represented by the given descriptor.
     * 
     * @param descriptor a char representing a java primitive type.
     * 
     * @return the class name corresponding to the given char descriptor.
     */
    String getClassForBaseType(char descriptor);

    /**
     * Resolve an annotation attribute value.
     * 
     * @param resolver
     * @param value
     * @return
     */
    Object resolveAttributeValue(ClassMetadataResolver resolver, Object value);

    /**
     * Resolve a generic capture, represented by the given wildcard to a 
     * GenericCapture enum value.
     * 
     * @param wildcard
     * 
     * @return
     */
    GenericCapture resolveCapture(char wildcard);

}
