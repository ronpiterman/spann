
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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.reader.VisitorAdapter;
import com.masetta.spann.metadata.util.EmptyArrays;
public class AnnotationVisitorImpl extends AbstractVisitor<AbstractMetadataImpl> {
    
    private final Stack<Object> stack = new Stack<Object>();
    
    private final Stack<AnnotationMetadataImpl> metadataStack = new Stack<AnnotationMetadataImpl>();
    
    private VisitorAdapter<AnnotationVisitorImpl> adapter;

    AnnotationVisitorImpl( VisitorController controller) {
        super( AbstractMetadataImpl.class, controller);
    }
    
    void init( VisitorAdapter<AnnotationVisitorImpl> adapter ) {
        this.adapter = adapter;
    }
    
    /**
     * <p>visit</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public void visit(String name, Object value) {
        Object v = null;
        if ( isSimple(value) ) {
            v = value;
        }
        else {
            v = getVisitorDelegate().resolveAttributeValue( value );
        }
        
        set(name, v);
    }

	private boolean isSimple(Object value) {
		return value.getClass().isPrimitive() || value instanceof Boolean ||
                value instanceof Number || value instanceof String || value instanceof Class || 
                value.getClass().isArray();
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void set(String name, Object v) {
        if ( name != null ) {
            ((AnnotationMetadataImpl)getMetadata()).addAttribute(name, v);
        }
        else {
            // annotation attribute default value.
            if ( stack.isEmpty() ) {
                ((MethodMetadataImpl)getMetadata()).setDefault( v );
            }
            else {
                ((List)stack.peek()).add( v );
            }
        }
    }

    /**
     * <p>visitAnnotation</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param desc a {@link java.lang.String} object.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public VisitorAdapter<AnnotationVisitorImpl> visitAnnotation(String name, String desc) {
        ClassMetadata annotationCls = getClassMetadataFromType( desc, true );
        Integer index = name == null && stack.size() > 0 ?
        		((List<?>)this.stack.peek()).size() : null;
        AnnotationMetadataImpl ann = new AnnotationMetadataImpl( getMetadata() , annotationCls , 
        		index );
        if ( name != null ) {
        	((AnnotationMetadataImpl)getMetadata()).addAttribute(name, ann );
        }
        else {
        	if ( ! stack.isEmpty() )
        		((List)stack.peek()).add( ann );
        }
        this.stack.push( ann );
        this.metadataStack.push( ann );
        return adapter;
    }

	/**
	 * <p>visitArray</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
	 */
	public VisitorAdapter<AnnotationVisitorImpl> visitArray(String name) {
        this.stack.push( name );
        this.stack.push( new ArrayList<Object>() );
        return adapter;
    }

    /**
     * <p>visitEnum</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param desc a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public void visitEnum(String name, String desc, String value) {
        ClassMetadata cm = getClassMetadataFromType( desc, true );
        EnumValue ev = new EnumValue( cm , value );
        set( name , ev );
    }

    /** {@inheritDoc} */
    @Override
    protected AbstractMetadataImpl getMetadata() {
        if ( metadataStack.isEmpty() )
        	return super.getMetadata();
        return metadataStack.peek();
    }
    
    /** {@inheritDoc} */
    @Override
    public void visitEnd() {
        if ( this.stack.isEmpty() ) {
            super.visitEnd();
            return;
        }
        
        Object pop = this.stack.pop();
        if ( pop instanceof List ) {
            @SuppressWarnings("rawtypes")
			Object[] array = createArray( (List)pop );
            set((String)stack.pop(), array );
        }
        else if ( pop instanceof AnnotationMetadataImpl ) {
        	this.metadataStack.pop();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object[] createArray(List list) {
        if ( list.isEmpty() ) {
            return EmptyArrays.OBJECT;
        }
        return list.toArray( (Object[])AnnotationMetadataImpl.getEmptyArray( 
                list.get( 0 ).getClass() ) );
    }

}
