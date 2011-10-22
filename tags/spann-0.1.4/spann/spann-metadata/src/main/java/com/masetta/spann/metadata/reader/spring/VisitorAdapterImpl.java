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

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.signature.SignatureVisitor;

import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.visitors.AbstractVisitor;
import com.masetta.spann.metadata.visitors.AnnotationVisitorImpl;
import com.masetta.spann.metadata.visitors.ClassVisitorImpl;
import com.masetta.spann.metadata.visitors.MethodVisitorImpl;
import com.masetta.spann.metadata.visitors.SignatureVisitorImpl;
import com.masetta.spann.metadata.visitors.VisitEndSupport;

public class VisitorAdapterImpl<T> implements ClassVisitor, MethodVisitor, FieldVisitor,
		AnnotationVisitor, SignatureVisitor, VisitorAdapter<T> {

	private final T delegate;

	/**
	 * <p>
	 * Constructor for VisitorAdapterImpl.
	 * </p>
	 * 
	 * @param visitor
	 *            the visitor delegate which contains the actual
	 *            implementations.
	 */
	public VisitorAdapterImpl(T visitor) {
		this.delegate = visitor;
	}

	// ---------------------------------------------------------------------------------------------
	// common
	// ---------------------------------------------------------------------------------------------

	/** {@inheritDoc} */
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return (AnnotationVisitor) ((AbstractVisitor<?>) delegate).visitAnnotation(desc, visible);
	}

	/** {@inheritDoc} */
	public void visitAttribute(Attribute attr) {
	}

	/**
	 * <p>
	 * visitEnd
	 * </p>
	 */
	public void visitEnd() {
		((VisitEndSupport) delegate).visitEnd();
	}

	// ---------------------------------------------------------------------------------------------
	// signature visitor
	// ---------------------------------------------------------------------------------------------

	/**
	 * <p>
	 * visitArrayType
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitArrayType() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitArrayType();
	}

	/** {@inheritDoc} */
	public void visitBaseType(char descriptor) {
		((SignatureVisitorImpl) delegate).visitBaseType(descriptor);
	}

	/**
	 * <p>
	 * visitClassBound
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitClassBound() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitClassBound();
	}

	/** {@inheritDoc} */
	public void visitClassType(String name) {
		((SignatureVisitorImpl) delegate).visitClassType(name);
	}

	/**
	 * <p>
	 * visitExceptionType
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitExceptionType() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitExceptionType();
	}

	/** {@inheritDoc} */
	public void visitFormalTypeParameter(String name) {
		((SignatureVisitorImpl) delegate).visitFormalTypeParameter(name);
	}

	/** {@inheritDoc} */
	public void visitInnerClassType(String name) {
		((SignatureVisitorImpl) delegate).visitInnerClassType(name);
	}

	/**
	 * <p>
	 * visitInterface
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitInterface() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitInterface();
	}

	/**
	 * <p>
	 * visitInterfaceBound
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitInterfaceBound() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitInterfaceBound();
	}

	/**
	 * <p>
	 * visitParameterType
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitParameterType() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitParameterType();
	}

	/**
	 * <p>
	 * visitReturnType
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitReturnType() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitReturnType();
	}

	/**
	 * <p>
	 * visitSuperclass
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.signature.SignatureVisitor}
	 *         object.
	 */
	public SignatureVisitor visitSuperclass() {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitSuperclass();
	}

	/**
	 * <p>
	 * visitTypeArgument
	 * </p>
	 */
	public void visitTypeArgument() {
		((SignatureVisitorImpl) delegate).visitTypeArgument();
	}

	/** {@inheritDoc} */
	public SignatureVisitor visitTypeArgument(char wildcard) {
		return (SignatureVisitor) ((SignatureVisitorImpl) delegate).visitTypeArgument(wildcard);
	}

	/** {@inheritDoc} */
	public void visitTypeVariable(String name) {
		((SignatureVisitorImpl) delegate).visitTypeVariable(name);
	}

	// ---------------------------------------------------------------------------------------------
	// annotation visitor
	// ---------------------------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void visit(String name, Object value) {
		((AnnotationVisitorImpl) delegate).visit(name, value);
	}

	/** {@inheritDoc} */
	public AnnotationVisitor visitAnnotation(String name, String desc) {
		return (AnnotationVisitor) ((AnnotationVisitorImpl) delegate).visitAnnotation(name, desc);
	}

	/** {@inheritDoc} */
	public AnnotationVisitor visitArray(String name) {
		return (AnnotationVisitor) ((AnnotationVisitorImpl) delegate).visitArray(name);
	}

	/** {@inheritDoc} */
	public void visitEnum(String name, String desc, String value) {
		((AnnotationVisitorImpl) delegate).visitEnum(name, desc, value);
	}

	// ---------------------------------------------------------------------------------------------
	// field visitor
	// ---------------------------------------------------------------------------------------------

	// ---------------------------------------------------------------------------------------------
	// method visitor
	// ---------------------------------------------------------------------------------------------

	/**
	 * <p>
	 * visitAnnotationDefault
	 * </p>
	 * 
	 * @return a {@link org.springframework.asm.AnnotationVisitor} object.
	 */
	public AnnotationVisitor visitAnnotationDefault() {
		return (AnnotationVisitor) ((MethodVisitorImpl) delegate).visitAnnotationDefault();
	}

	/** {@inheritDoc} */
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		return (AnnotationVisitor) ((MethodVisitorImpl) delegate).visitParameterAnnotation(
				parameter, desc, visible);
	}

	/**
	 * <p>
	 * visitCode
	 * </p>
	 */
	public void visitCode() {
	}

	/** {@inheritDoc} */
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
	}

	/**
	 * <p>
	 * visitFrame
	 * </p>
	 * 
	 * @param type
	 *            a int.
	 * @param local
	 *            a int.
	 * @param local2
	 *            an array of {@link java.lang.Object} objects.
	 * @param stack
	 *            a int.
	 * @param stack2
	 *            an array of {@link java.lang.Object} objects.
	 */
	public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
	}

	/** {@inheritDoc} */
	public void visitIincInsn(int var, int increment) {
	}

	/** {@inheritDoc} */
	public void visitInsn(int opcode) {
	}

	/** {@inheritDoc} */
	public void visitIntInsn(int opcode, int operand) {
	}

	/** {@inheritDoc} */
	public void visitJumpInsn(int opcode, Label label) {
	}

	/** {@inheritDoc} */
	public void visitLabel(Label label) {
	}

	/** {@inheritDoc} */
	public void visitLdcInsn(Object cst) {
	}

	/** {@inheritDoc} */
	public void visitLineNumber(int line, Label start) {
	}

	/** {@inheritDoc} */
	public void visitLocalVariable(String name, String desc, String signature, Label start,
			Label end, int index) {
	}

	/**
	 * <p>
	 * visitLookupSwitchInsn
	 * </p>
	 * 
	 * @param dflt
	 *            a {@link org.springframework.asm.Label} object.
	 * @param keys
	 *            an array of int.
	 * @param labels
	 *            an array of {@link org.springframework.asm.Label} objects.
	 */
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
	}

	/** {@inheritDoc} */
	public void visitMaxs(int maxStack, int maxLocals) {
	}

	/** {@inheritDoc} */
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
	}

	/** {@inheritDoc} */
	public void visitMultiANewArrayInsn(String desc, int dims) {
	}

	/**
	 * <p>
	 * visitTableSwitchInsn
	 * </p>
	 * 
	 * @param min
	 *            a int.
	 * @param max
	 *            a int.
	 * @param dflt
	 *            a {@link org.springframework.asm.Label} object.
	 * @param labels
	 *            an array of {@link org.springframework.asm.Label} objects.
	 */
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
	}

	/** {@inheritDoc} */
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
	}

	/** {@inheritDoc} */
	public void visitTypeInsn(int opcode, String type) {
	}

	/** {@inheritDoc} */
	public void visitVarInsn(int opcode, int var) {
	}

	// ---------------------------------------------------------------------------------------------
	// class visitor
	// ---------------------------------------------------------------------------------------------

	/**
	 * <p>
	 * visit
	 * </p>
	 * 
	 * @param version
	 *            a int.
	 * @param access
	 *            a int.
	 * @param name
	 *            a {@link java.lang.String} object.
	 * @param signature
	 *            a {@link java.lang.String} object.
	 * @param superName
	 *            a {@link java.lang.String} object.
	 * @param interfaces
	 *            an array of {@link java.lang.String} objects.
	 */
	public void visit(int version, int access, String name, String signature, String superName,
			String[] interfaces) {
		((ClassVisitorImpl) delegate)
				.visit(version, access, name, signature, superName, interfaces);
	}

	/** {@inheritDoc} */
	public FieldVisitor visitField(int access, String name, String desc, String signature,
			Object value) {
		return (FieldVisitor) ((ClassVisitorImpl) delegate).visitField(access, name, desc,
				signature, value);
	}

	/** {@inheritDoc} */
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		((ClassVisitorImpl) delegate).visitInnerClass(name, outerName, innerName, access);
	}

	/**
	 * <p>
	 * visitMethod
	 * </p>
	 * 
	 * @param access
	 *            a int.
	 * @param name
	 *            a {@link java.lang.String} object.
	 * @param desc
	 *            a {@link java.lang.String} object.
	 * @param signature
	 *            a {@link java.lang.String} object.
	 * @param exceptions
	 *            an array of {@link java.lang.String} objects.
	 * @return a {@link org.springframework.asm.MethodVisitor} object.
	 */
	public MethodVisitor visitMethod(int access, String name, String desc, String signature,
			String[] exceptions) {
		return (MethodVisitor) ((ClassVisitorImpl) delegate).visitMethod(access, name, desc,
				signature, exceptions);
	}

	/** {@inheritDoc} */
	public void visitOuterClass(String owner, String name, String desc) {
		((ClassVisitorImpl) delegate).visitOuterClass(owner, name, desc);
	}

	/** {@inheritDoc} */
	public void visitSource(String source, String debug) {
	}

	/**
	 * <p>
	 * Getter for the field <code>delegate</code>.
	 * </p>
	 * 
	 * @return a T object.
	 */
	public T getDelegate() {
		return delegate;
	}

}
