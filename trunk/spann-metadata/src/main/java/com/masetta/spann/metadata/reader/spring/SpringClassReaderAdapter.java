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

import java.io.IOException;
import java.io.InputStream;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.asm.signature.SignatureReader;
import org.springframework.asm.signature.SignatureVisitor;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.core.modifier.MethodModifier;
import com.masetta.spann.metadata.reader.AbstractClassReaderAdapter;
import com.masetta.spann.metadata.reader.ClassMetadataResolver;
import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.metadata.visitors.ClassVisitorImpl;
import com.masetta.spann.metadata.visitors.SignatureVisitorImpl;

public class SpringClassReaderAdapter extends AbstractClassReaderAdapter {

	private SpannLog spannLog = SpannLogFactory.getLog(AbstractClassReaderAdapter.class);

	public void readClass(InputStream in, VisitorAdapter<ClassVisitorImpl> visitor)
			throws IOException {
		ClassReader reader = new ClassReader(in);
		reader.accept((ClassVisitor) visitor, false);
	}

	public void readSignature(String signature,
			VisitorAdapter<SignatureVisitorImpl> signatureVisitor) {
		if ( signature == null )
			return;
		new SignatureReader(signature).accept((SignatureVisitor) signatureVisitor);
	}

	protected ClassModifier createClassModifierInternal(int argument) {
		return new ClassModifierImpl(argument);
	}

	protected FieldModifier createFieldModifierInternal(int argument) {
		return new FieldModifierImpl(argument);
	}

	protected MethodModifier createMethodModifierInternal(int argument) {
		return new MethodModifierImpl(argument);
	}

	public ClassMetadata[] getMethodArguments(ClassMetadataResolver resolver, String desc) {
		Type[] args = Type.getArgumentTypes(desc);
		return getClassMetadata(resolver, args);
	}

	private ClassMetadata[] getClassMetadata(ClassMetadataResolver resolver, Type[] types) {
		ClassMetadata[] cm = new ClassMetadata[types.length];
		for (int i = 0; i < cm.length; i++)
			cm[i] = getClassMetadata(resolver, types[i], false);
		return cm;
	}

	private ClassMetadata getClassMetadata(ClassMetadataResolver resolver, Type type,
			boolean ignoreArray) {
		if ( type.getSort() == Type.ARRAY ) {
			return resolver.resolve(type.getElementType().getClassName(), ignoreArray ? 0 : type
					.getDimensions());
		} else
			return resolver.resolve(type.getClassName(), 0);
	}

	public ClassMetadata getMethodReturnType(ClassMetadataResolver resolver, String desc) {
		return getClassMetadata(resolver, Type.getReturnType(desc), false);
	}

	public ClassMetadata resolveDescriptor(ClassMetadataResolver resolver, String desc,
			boolean ignoreArray) {
		return getClassMetadata(resolver, Type.getType(desc), false);
	}

	public String getClassForBaseType(char descriptor) {
		Type type = Type.getType("" + descriptor);
		return type.getClassName();
	}

	public Object resolveAttributeValue(ClassMetadataResolver resolver, Object value) {
		Type type = null;
		if ( value instanceof Type )
			type = (Type)value;
		else if ( value instanceof Class<?> ) 
			type = Type.getType( (Class<?>) value );
		else 
			throw new IllegalArgumentException("Unknown value type: " + value.getClass().getCanonicalName() );
		return getClassMetadata( resolver, type , true );
	}

	public GenericCapture resolveCapture(char wildcard) {
		switch ( wildcard ) {
			case SignatureVisitor.EXTENDS:
				return GenericCapture.EXTENDS;
			case SignatureVisitor.INSTANCEOF:
				return GenericCapture.IS;
			case SignatureVisitor.SUPER:
				return GenericCapture.SUPER_OF;
		}
		throw new IllegalArgumentException("Unknwon wildcard: " + wildcard);
	}

	@SuppressWarnings("unchecked")
	public <T> VisitorAdapter<T> createVisitorAdapter(T t) {
		VisitorAdapter<T> a = new VisitorAdapterImpl<T>(t);
		return (VisitorAdapter<T>) SpannLogFactory.createTraceProxy(a, spannLog,
				VisitorAdapter.class, ClassVisitor.class, MethodVisitor.class, FieldVisitor.class,
				AnnotationVisitor.class, SignatureVisitor.class);
	}

	public VisitorAdapter<?> createEmptyVisitor(Artifact artifact) {
		switch ( artifact ) {
			case ANNOTATION:
				return new EmptyAdapter();
			default:
				return null;
		}
	}

	private static class EmptyAdapter extends EmptyVisitor implements VisitorAdapter<Object> {
		public Object getDelegate() {
			return null;
		}
	}

}
