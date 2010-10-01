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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EmptyMetadataArrays;
import com.masetta.spann.metadata.util.EqualsBuilder;

class AnnotationMetadataImpl extends AbstractMetadataImpl implements AnnotationMetadata {
    
    private ClassMetadata type;

    private Map<String, Object> attributes;
    
    private AbstractMetadataImpl parent;
    
    private Integer index;

    AnnotationMetadataImpl( AbstractMetadataImpl parent, ClassMetadata annotationType , Integer i) {
        super(parent.getPath(), Artifact.ANNOTATION, annotationType.getName());
        this.type = annotationType;
        this.parent = parent;
        this.index = i;
    }
    
    AnnotationMetadataImpl( AbstractMetadataImpl parent, ClassMetadata annotationType) {
    	this( parent , annotationType , null );
    }

    void addAttribute(String name, Object value) {
        if ( this.attributes == null )
            this.attributes = new HashMap<String, Object>();
        this.attributes.put(name, value);
    }

    public Object getAttribute(String name, boolean nullsafe) {
        Object o = null;
        if ( attributes != null ) {
            o = this.attributes.get(name);
        }
        if ( nullsafe && o == null ) {
            o = type.getAnnotationAttributeDefault(name);
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Class<T> type, String name, boolean nullsafe) {
        Object o = getAttribute(name, nullsafe);
        if ( o == null ) {
            return null;
        }
        if ( type.isAssignableFrom(o.getClass()) ) {
            return (T) o;
        }
        if ( type.isArray() && o.getClass().isArray() && Array.getLength(o) == 0 ) {
            return (T) getEmptyArray(type.getComponentType());
        }
        throw new IllegalArgumentException("Attribute " + name + ": value class is "
                + o.getClass().getCanonicalName() + ", expected " + type.getCanonicalName());
    }

    static Object getEmptyArray( Class<?> type) {
        Class<?> normalized = normalize(type);
        final Object emptyArray = EmptyMetadataArrays.getEmptyArray( normalized );
        if ( emptyArray == null ) {
            throw new IllegalArgumentException("Not supported: " + normalized);
        }
        return emptyArray;
    }

    static Class<?> normalize(Class<?> boxed) {
        if ( boxed.equals(Boolean.class) ) {
            return Boolean.TYPE;
        }
        if ( boxed.equals(Integer.class) ) {
            return Integer.TYPE;
        }
        if ( boxed.equals(Long.class) ) {
            return Long.TYPE;
        }
        if ( boxed.equals(Short.class) ) {
            return Short.TYPE;
        }
        if ( boxed.equals(Byte.class) ) {
            return Byte.TYPE;
        }
        if ( boxed.equals(Character.class) ) {
            return Character.TYPE;
        }
        if ( boxed.equals(Float.class) ) {
            return Float.TYPE;
        }
        if ( boxed.equals(Double.class) ) {
            return Double.TYPE;
        }
        if ( boxed.equals( ClassMetadataImpl.class ) )
        	return ClassMetadata.class;
        
        if ( boxed.equals( AnnotationMetadataImpl.class ) )
        	return AnnotationMetadata.class;
        
        return boxed;
    }
    
	public AnnotatedElementMetadata getAnnotatedElement() {
		if ( this.parent instanceof AnnotatedElementMetadata )
			return (AnnotatedElementMetadata) this.parent;
		return null;
	}

	@Override
	protected boolean equals(AbstractMetadataImpl obj, EqualsBuilder b) {
		final AnnotationMetadataImpl other = (AnnotationMetadataImpl)obj;
		return super.equals(obj, b) && b.eq( this.index , other.index )
			&& b.eq( this.parent , other.parent );
	}

	public ClassMetadata getType() {
		return this.type;
	}
	
	
}
        
    


