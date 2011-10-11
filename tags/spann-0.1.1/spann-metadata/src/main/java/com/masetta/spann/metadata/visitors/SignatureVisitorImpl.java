
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

package com.masetta.spann.metadata.visitors;

import java.util.Iterator;
import java.util.Stack;

import com.masetta.spann.metadata.common.ResourceUtil;
import com.masetta.spann.metadata.core.ArtifactPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.TypeParameter;
import com.masetta.spann.metadata.core.TypeReference;
import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.util.Provider;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
public class SignatureVisitorImpl implements Provider<ClassLoader> , VisitEndSupport {

    private static final String CANONICAL_NAME = SignatureVisitorImpl.class.getCanonicalName();

    private static final Object ARRAY_MARKER = new Object();

    private static final Object INTERFACE_MARKER = new Object();

    private final SpannLog log = SpannLogFactory.getLog(SignatureVisitorImpl.class);

    private final VisitorController visitorController;

    private final VisitorDelegate delegate;

    private Stack<Object> stack = new Stack<Object>();

    private Metadata target;

    private ParameterMetadataImpl currentParameter;
    
    private VisitorAdapter<SignatureVisitorImpl> adapter;

    SignatureVisitorImpl(VisitorController visitorController) {
        this.visitorController = visitorController;
        this.delegate = visitorController.getDelegate();
    }
    
    void init( VisitorAdapter<SignatureVisitorImpl> adapter ) {
        this.adapter = adapter;
    }

    /**
     * <p>reset</p>
     */
    public void reset() {
        this.stack.clear();
        this.currentParameter = null;
        this.target = null;
    }

    /**
     * <p>visitFormalTypeParameter</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void visitFormalTypeParameter(String name) {
        TypeParameter pc = new TypeParameterImpl(name);
        ((TypeParameterSupport) target).addTypeParameter(pc);
        push(pc);
    }

    /**
     * <p>visitClassBound</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitClassBound() {
        return adapter;
    }

    /**
     * <p>visitInterfaceBound</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitInterfaceBound() {
        return adapter;
    }

    /**
     * <p>visitClassType</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void visitClassType(String name) {
        visitClassTypeInternal(name, false);
    }

    private void visitClassTypeInternal(String name, boolean inner) {
        int dimensions = getArrayDimensions();
        final Object current = stack.peek();
        ClassMetadata previous = null;
        ClassMetadata classMetadata = visitorController.getClassMetadata(name, dimensions, this);
        if ( inner ) {
            classMetadata = new TypeMetadataImpl(classMetadata);
        }
        if ( current instanceof TypeReferenceImpl ) {
            previous = ((TypeReferenceImpl) current).getType();
            ((TypeReferenceImpl) current).setType(classMetadata);
        } else if ( current instanceof TypeMetadataImpl ) {
            final TypeMetadataImpl typeMetadata = (TypeMetadataImpl) current;
            if ( !inner && typeMetadata.getClassMetadata() != null ) {
                String classname = ResourceUtil.convertResourcePathToClassName(name);
                final String n = typeMetadata.getName();
                if ( !n.equals(classname) )
                    throw new IllegalStateException(
                            "Visiting super class does not match (visiting " + classname
                                    + " but definition has " + n + ")");
            } else {
                previous = typeMetadata.getClassMetadata();
                typeMetadata.setClass(classMetadata);
            }
        } else
            throw new IllegalStateException("Unknown stack state in visitClassType(" + name + ")");

        if ( inner ) {
            ((TypeMetadataImpl) classMetadata).setOuterType(previous);
        }
    }

    private int getArrayDimensions() {
        int dimensions = 0;
        while ( stack.peek() == ARRAY_MARKER ) {
            dimensions++;
            pop();
        }
        return dimensions;
    }

    /**
     * <p>visitTypeArgument</p>
     *
     * @param wildcard a char.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitTypeArgument(char wildcard) {
        final GenericCapture capture = visitorController.getDelegate().resolveCapture(wildcard);
        return addTypeArgument(capture);
    }

    /**
     * <p>visitTypeArgument</p>
     */
    public void visitTypeArgument() {
        addTypeArgument(null);
        visitEnd();
    }

    private VisitorAdapter<SignatureVisitorImpl> addTypeArgument(final GenericCapture capture) {
        Object current = stack.peek();
        if ( current instanceof TypeReferenceImpl ) {
            TypeMetadata cm = getOrCreateTypeMetadata((TypeReferenceImpl) current);
            TypeArgumentImpl impl = new TypeArgumentImpl(capture);
            ((TypeMetadataImpl) cm).addTypeArgument(impl);
            push(impl);
        } else if ( current instanceof TypeMetadata ) {
            TypeArgumentImpl tp = new TypeArgumentImpl(capture);
            ((TypeMetadataImpl) current).addTypeArgument(tp);
            push(tp);
        } else {
            throw new IllegalStateException("Unknown stack state: " + current
                    + ", expected TypeParameter or TypeMetadata");
        }
        return adapter;
    }

    private TypeMetadata getOrCreateTypeMetadata(TypeReferenceImpl typeParameterOrArgument) {
        ClassMetadata cm = getTypeMetadata(typeParameterOrArgument);
        if ( cm != null && !(cm instanceof TypeMetadata) ) {
            cm = new TypeMetadataImpl(cm);
            typeParameterOrArgument.setType(cm);
        }
        return (TypeMetadata) cm;
    }

    private ClassMetadata getTypeMetadata(Object typeParameterOrArgument) {
        return typeParameterOrArgument instanceof TypeReference ? ((TypeReference) typeParameterOrArgument)
                .getType()
                : ((TypeArgument) typeParameterOrArgument).getType();
    }

    /**
     * <p>visitTypeVariable</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void visitTypeVariable(String name) {
        int dimensions = getArrayDimensions();
        ((NameMutable) stack.peek()).setName(name);
        if ( dimensions > 0 ) {
            ((TypeArgumentImpl) stack.peek()).setDimensions(dimensions);
        }
        visitEnd();
    }

    /**
     * Visit the 'extends' clause of a class definition
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitSuperclass() {
        while ( !stack.isEmpty() )
            pop();
        if ( target instanceof ClassMetadata ) {
            ClassMetadata sup = ((ClassMetadata) target).getSuperClass( false );
            TypeMetadataImpl tm = new TypeMetadataImpl(sup);
            push(tm);
        } else if ( target instanceof FieldMetadataImpl ) {
            GenericType t = new GenericTypeImpl();
            ((FieldMetadataImpl) target).setFieldType(t);
            push(t);
        }
        return adapter;
    }

    /**
     * Visit an interface in the 'implements' clause of a class definition
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitInterface() {
        Object prev = null;
        boolean ifc = false;
        while ( !stack.isEmpty() ) {
            Object o = pop();
            if ( o == INTERFACE_MARKER ) {
                ifc = true;
            } else {
                prev = o;
            }
        }

        if ( prev != null ) {
            TypeMetadataImpl prevType = (TypeMetadataImpl) prev;
            setSuperOrInterface(prevType, ifc);
        }
        push(INTERFACE_MARKER);
        push(new TypeMetadataImpl());
        return adapter;
    }

    private void setSuperOrInterface(TypeMetadataImpl prevType, boolean ifc) {
        if ( prevType.getTypeArguments().size() > 0 ) {
            if ( ifc )
                ((ClassMetadataImpl) target).setInterface(prevType);
            else
                ((ClassMetadataImpl) target).setSuper(prevType);
        }
    }

    /**
     * <p>visitEnd</p>
     */
    public void visitEnd() {
        Object last = pop();

        boolean ifc = !stack.isEmpty() && stack.peek() == INTERFACE_MARKER && returnTrue(pop());
        returnTrue(stack.isEmpty() && (visitEndClass(last, ifc) || visitEndMethod(last)));
    }

    private boolean visitEndMethod(Object last) {
        if ( target instanceof MethodMetadata ) {

            if ( last instanceof GenericType && !isEmpty((GenericType) last) )
                ((MethodMetadataImpl) target).setReturnType((GenericType) last);

            if ( currentParameter != null ) {
                if ( isEmpty(currentParameter.getParameterType()) )
                    currentParameter.setParameterType(null);
                currentParameter = null;
            }
        }
        return false;
    }

    private boolean visitEndClass(Object last, boolean ifc) {
        if ( target instanceof ClassMetadata ) {
            if ( last instanceof TypeMetadataImpl ) {
                setSuperOrInterface((TypeMetadataImpl) last, ifc);
            }
            return true;
        }
        return false;
    }

    private boolean isEmpty(GenericType type) {
        return type == null || type.getContextBoundTypeParameter() == null
                && (type.getType() == null || !(type.getType() instanceof TypeMetadata));
    }

    private boolean returnTrue(Object pop) {
        return true;
    }

    /**
     * <p>visitArrayType</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitArrayType() {
        push(ARRAY_MARKER);
        return adapter;
    }

    /**
     * <p>visitBaseType</p>
     *
     * @param descriptor a char.
     */
    public void visitBaseType(char descriptor) {
        String typename = delegate.getClassForBaseType(descriptor);
        visitClassType(typename);
        visitEnd();
    }

    /**
     * <p>visitExceptionType</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitExceptionType() {
        return null;
    }

    /**
     * <p>visitInnerClassType</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void visitInnerClassType(String name) {
        visitClassTypeInternal(currentClassNameForInnerType() + "$" + name, true);
    }

    private String currentClassNameForInnerType() {
        final Object current = stack.peek();
        if ( current instanceof TypeReference ) {
            return ((TypeReference) current).getType().getName();
        } else if ( current instanceof TypeMetadataImpl ) {
            return ((TypeMetadata) current).getName();
        } else
            throw new IllegalStateException("Unknown stack state in currentClassNameForInnerType()");
    }

    // only for methods
    /**
     * <p>visitParameterType</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitParameterType() {
        Iterator<?> params;
        if ( this.stack.isEmpty() ) {
            params = ((MethodMetadata) target).getParameters().iterator();
            push(params);
        } else {

            if ( isEmpty(currentParameter.getParameterType()) )
                currentParameter.setParameterType(null);

            while ( this.stack.size() > 1 )
                pop();

            if ( !(this.stack.peek() instanceof Iterator) ) {
                log.info("Non iterator: " + stack);
            }

            params = (Iterator<?>) this.stack.peek();
        }
        if ( !params.hasNext() ) {
            throw new IllegalStateException("Signature does not match.");
        }
        this.currentParameter = (ParameterMetadataImpl) params.next();
        GenericType type = new GenericTypeImpl();
        this.currentParameter.setParameterType(type);
        push(type);
        return adapter;
    }

    // only for methods
    /**
     * <p>visitReturnType</p>
     *
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<SignatureVisitorImpl> visitReturnType() {
        while ( !stack.isEmpty() )
            pop();
        push(new GenericTypeImpl());
        return adapter;
    }

    /**
     * <p>Setter for the field <code>target</code>.</p>
     *
     * @param target a {@link com.masetta.spann.metadata.core.Metadata} object.
     */
    public void setTarget(Metadata target) {
        this.target = target;
    }

    /**
     * <p>get</p>
     *
     * @return a {@link java.lang.ClassLoader} object.
     */
    public ClassLoader get() {
        ArtifactPath path = this.target.getPath();
        while ( !(path.getMetadata() instanceof ClassMetadata) ) {
            path = path.getParent();
        }
        return ((ClassMetadata) path.getMetadata()).getClassLoader();
    }

    private void push(Object object) {
        stack.push(object);
        if ( log.isTraceEnabled() ) {
            log.trace("PUSH " + getTraceMethod() + " > " + stack);
        }
    }

    private Object pop() {
        if ( log.isTraceEnabled() ) {
            log.trace("POP " + getTraceMethod() + " > " + stack);
        }
        return stack.pop();
    }

    private String getTraceMethod() {
        RuntimeException t = new RuntimeException();
        StackTraceElement[] stackTrace = t.getStackTrace();
        StackTraceElement e = null;
        for (StackTraceElement ste : stackTrace) {
            if ( !ste.getClassName().equals(CANONICAL_NAME) ) {
                return e.getMethodName();
            }
            e = ste;
        }
        return "?";
    }

}
