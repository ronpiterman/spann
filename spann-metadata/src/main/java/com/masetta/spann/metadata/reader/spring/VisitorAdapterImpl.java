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

public class VisitorAdapterImpl<T> implements ClassVisitor, MethodVisitor ,  FieldVisitor , 
    AnnotationVisitor , SignatureVisitor , VisitorAdapter<T> {
    
    private final T delegate;
    
    public VisitorAdapterImpl( T visitor ) {
        this.delegate = visitor;
    }
    
    // ---------------------------------------------------------------------------------------------
    // common
    // ---------------------------------------------------------------------------------------------
    
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return (AnnotationVisitor) ((AbstractVisitor<?>)delegate).visitAnnotation(desc, visible);
    }
    
    public void visitAttribute(Attribute attr) {
    }
    
    public void visitEnd() {
        ((VisitEndSupport)delegate).visitEnd();
    }
    
    // ---------------------------------------------------------------------------------------------
    // signature visitor 
    // ---------------------------------------------------------------------------------------------
    
    public SignatureVisitor visitArrayType() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitArrayType();
    }

    public void visitBaseType(char descriptor) {
        ((SignatureVisitorImpl)delegate).visitBaseType(descriptor);
    }

    public SignatureVisitor visitClassBound() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitClassBound();
    }

    public void visitClassType(String name) {
        ((SignatureVisitorImpl)delegate).visitClassType(name);
    }

    public SignatureVisitor visitExceptionType() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitExceptionType();
    }

    public void visitFormalTypeParameter(String name) {
        ((SignatureVisitorImpl)delegate).visitFormalTypeParameter(name);
    }

    public void visitInnerClassType(String name) {
        ((SignatureVisitorImpl)delegate).visitInnerClassType(name);
    }

    public SignatureVisitor visitInterface() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitInterface();
    }

    public SignatureVisitor visitInterfaceBound() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitInterfaceBound();
    }

    public SignatureVisitor visitParameterType() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitParameterType();
    }

    public SignatureVisitor visitReturnType() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitReturnType();
    }

    public SignatureVisitor visitSuperclass() {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitSuperclass();
    }

    public void visitTypeArgument() {
        ((SignatureVisitorImpl)delegate).visitTypeArgument();
    }

    public SignatureVisitor visitTypeArgument(char wildcard) {
        return (SignatureVisitor) ((SignatureVisitorImpl)delegate).visitTypeArgument(wildcard);
    }

    public void visitTypeVariable(String name) {
        ((SignatureVisitorImpl)delegate).visitTypeVariable(name);
    }
    
    // ---------------------------------------------------------------------------------------------
    // annotation visitor 
    // ---------------------------------------------------------------------------------------------

    public void visit(String name, Object value) {
        ((AnnotationVisitorImpl)delegate).visit(name, value);
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return (AnnotationVisitor) ((AnnotationVisitorImpl)delegate).visitAnnotation(name, desc);
    }

    public AnnotationVisitor visitArray(String name) {
        return (AnnotationVisitor) ((AnnotationVisitorImpl)delegate).visitArray(name);
    }

    public void visitEnum(String name, String desc, String value) {
        ((AnnotationVisitorImpl)delegate).visitEnum(name, desc, value);
    }
    
    
    // ---------------------------------------------------------------------------------------------
    // field visitor 
    // ---------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // method visitor 
    // ---------------------------------------------------------------------------------------------

    public AnnotationVisitor visitAnnotationDefault() {
        return (AnnotationVisitor) ((MethodVisitorImpl)delegate).visitAnnotationDefault();
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter,
            String desc, boolean visible) {
        return (AnnotationVisitor) ((MethodVisitorImpl)delegate).visitParameterAnnotation(parameter, desc, visible);
    }

    public void visitCode() {
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
    }

    public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
    }

    public void visitIincInsn(int var, int increment) {
    }

    public void visitInsn(int opcode) {
    }

    public void visitIntInsn(int opcode, int operand) {
    }

    public void visitJumpInsn(int opcode, Label label) {
    }

    public void visitLabel(Label label) {
    }

    public void visitLdcInsn(Object cst) {
    }

    public void visitLineNumber(int line, Label start) {
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start,
            Label end, int index) {
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    }

    public void visitMaxs(int maxStack, int maxLocals) {
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    }

    public void visitTypeInsn(int opcode, String type) {
    }

    public void visitVarInsn(int opcode, int var) {
    }

    // ---------------------------------------------------------------------------------------------
    // class visitor
    // ---------------------------------------------------------------------------------------------

    public void visit(int version, int access, String name, String signature, String superName,
            String[] interfaces) {
        ((ClassVisitorImpl)delegate).visit(version, access, name, signature, superName, interfaces);
    }

    public FieldVisitor visitField(int access, String name, String desc,
            String signature, Object value) {
        return (FieldVisitor) ((ClassVisitorImpl)delegate).visitField(access, name, desc, signature, value);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        ((ClassVisitorImpl)delegate).visitInnerClass(name, outerName, innerName, access);
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        return (MethodVisitor) ((ClassVisitorImpl)delegate).visitMethod(access, name, desc, signature, exceptions);
    }

    public void visitOuterClass(String owner, String name, String desc) {
        ((ClassVisitorImpl)delegate).visitOuterClass(owner, name, desc);
    }

    public void visitSource(String source, String debug) {
    }

    public T getDelegate() {
        return delegate;
    }

}
