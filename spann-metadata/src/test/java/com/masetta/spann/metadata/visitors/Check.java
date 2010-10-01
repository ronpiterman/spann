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

import java.util.List;

import org.testng.Assert;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.GenericCapture;
import com.masetta.spann.metadata.core.GenericType;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.core.TypeArgument;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.TypeParameter;

public class Check {

    static TypeArgument check(GenericType p, int index, Class<?> expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        TypeMetadata cm = (TypeMetadata) p.getType();
        return check(cm, index, expectedClass, capture, expectedCtxBnd);
    }

    static TypeArgument check(TypeParameter p, int index, Class<?> expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        TypeMetadata cm = (TypeMetadata) p.getType();
        return check(cm, index, expectedClass, capture, expectedCtxBnd);
    }
    
    static TypeArgument check(GenericType t, int index, String expectedClass,
            GenericCapture capture) {
        TypeMetadata cm = (TypeMetadata) t.getType();
        return check(cm, index, expectedClass, capture, null);
    }

    static TypeArgument check(TypeParameter p, int index, String expectedClass,
            GenericCapture capture) {
        TypeMetadata cm = (TypeMetadata) p.getType();
        return check(cm, index, expectedClass, capture, null);
    }
    
    static TypeArgument check(TypeMetadata cm, int index, 
            GenericCapture capture, String expectedCtxBnd) {
        return Check.check( cm , index , (String)null , capture, expectedCtxBnd );
    }

    static TypeArgument check(TypeMetadata cm, int index, Class<?> expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        return Check
                .check(cm.getTypeArguments().get(index), expectedClass, capture, expectedCtxBnd);
    }

    static TypeArgument check(TypeMetadata cm, int index, String expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        return Check
                .check(cm.getTypeArguments().get(index), expectedClass, capture, expectedCtxBnd);
    }
    
    static TypeArgument check(TypeArgument typeArgument, int index, Class<?> expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        TypeMetadata cm = (TypeMetadata) typeArgument.getType();
        return check(cm, index, expectedClass, capture, expectedCtxBnd);
    }

    static TypeArgument check(TypeArgument typeArgument, String expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        Assert.assertEquals(typeArgument.getContextBoundTypeParameter(), expectedCtxBnd);
        Assert.assertEquals(typeArgument.getCapture(), capture);
        if ( expectedClass == null ) {
            Assert.assertNull(typeArgument.getType());
        } else {
            Assert.assertEquals(typeArgument.getType().getName(), expectedClass);
        }
        return typeArgument;
    }

    static TypeArgument check(TypeArgument typeArgument, Class<?> expectedClass,
            GenericCapture capture, String expectedCtxBnd) {
        return check(typeArgument, expectedClass == null ? null : expectedClass.getCanonicalName(),
                capture, expectedCtxBnd);
    }

    static GenericType check(MethodMetadata mm, int paramIndex, Class<?> cls,
            String expectedCzxBnd) {
        ParameterMetadata pm = mm.getParameters().get(paramIndex);
        GenericType type = pm.getParameterType();
        if ( cls == null && expectedCzxBnd == null )
            Assert.assertNull(type, String.valueOf(type));
        else {
            if ( cls == null ) {
                Assert.assertNull(type.getType());
            } else
                Assert.assertEquals(type.getType().getName(), cls.getCanonicalName());
            Assert.assertEquals(type.getContextBoundTypeParameter(), expectedCzxBnd);
        }
        return type;
    }
    
    static GenericType check( GenericType t , String name ) {
        return check(t, (String) null, name);
    }

    static TypeParameter check(TypeParameter p, String name) {
        return check(p, (String) null, name);
    }
    
    static GenericType check(GenericType t, Class<?> type, String name) {
        return check(t, (type == null ? null : type.getCanonicalName()), name);
    }

    static TypeParameter check(TypeParameter p, Class<?> type, String name) {
        return check(p, (type == null ? null : type.getCanonicalName()), name);
    }
    
    static GenericType check(GenericType t, String type, String name) {
        if ( type == null && name == null ) {
            Assert.assertNull(t, "GenericType");
            return t;
        }
        if ( type == null ) {
            Assert.assertNull(t.getType(), "GenericType.type");
        } else
            Assert.assertEquals(t.getType().getName(), type, "GenericType.type");
        Assert.assertEquals(t.getContextBoundTypeParameter(), name, "GenericType.contextBoundParameterName");
        return t;
    }

    static TypeParameter check(TypeParameter p, String type, String name) {
        if ( type == null && name == null ) {
            Assert.assertNull(p, "type parameter");
            return p;
        }
        if ( type == null ) {
            Assert.assertNull(p.getType(), "TypeParameter.type");
        } else
            Assert.assertEquals(p.getType().getName(), type, "TypeParameter.type");
        Assert.assertEquals(p.getName(), name, "TypeParameter.name");
        return p;
    }

    static ClassMetadata innerOf(GenericType tp, String outer) {
        ClassMetadata cm = tp.getType();
        return innerOf(cm, outer);
    }

    static ClassMetadata innerOf(ClassMetadata cm, String outer) {
        Assert.assertTrue(cm instanceof TypeMetadata, "TypeParameter.type is TypeMetadata");
        TypeMetadata tm = (TypeMetadata) cm;
        ClassMetadata outerMd = tm.getOuterType();
        Assert.assertNotNull(outerMd, "Outer type");
        Assert.assertEquals(outerMd.getName(), outer);
        return outerMd;
    }
    
    /** 
     * Check that the given class metadata is actually
     * a TypeMetadata and has args type parameters.
     * @param type
     * @param args
     */
    public static void check(ClassMetadata type, int args ) {
        Assert.assertTrue( type instanceof TypeMetadata , "is type metadata");
        Assert.assertEquals( ((TypeMetadata)type).getTypeArguments().size(), args );
    }

    public static void checkTypeParameter(List<TypeParameter> typeParameters, int index, Class type, String name) {
        check( typeParameters.get( index ) , type , name );
    }

}
