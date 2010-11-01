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

import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.support.FieldMetadataSupport;
import com.masetta.spann.metadata.rules.Rules;
import com.masetta.spann.metadata.visitors.classes.Ann;
import com.masetta.spann.metadata.visitors.classes.Ann2;

public class AnnotatedClassTest extends BaseTestMetadata {

    private Set<String> attributes = new HashSet<String>();

    public AnnotatedClassTest() {
        super("AnnotatedClass");
        setLazyLoadingRulesFactory(Rules.LAZY_EAGER_FLAT_SINGLE_ARTIFACT);
    }

    @Test
    public void testRead() {
        ClassMetadata cm = getMetadata();
        Assert.assertNotNull(cm);
    }

    @Test(dataProvider = "fields")
    public void collectAttributes(String field, Class<?> ann, String attr, Object expected) {
        attributes.add(attr);
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testFieldAnnotationAttribute(String field, Class<?> ann, String attr,
            Object expected) {
        AnnotationMetadata am = checkFieldAnnAttr(field, ann, attr, expected, false);
        // check that all other attributes are empty
        for (String a : attributes) {
            if ( !a.equals(attr) ) {
                Object empty = am.getAttribute(a, false);
                Assert.assertNull(empty, a);
            }
        }
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testFieldAnn2(String field, Class<?> ann, String attr, Object expected) {
        checkFieldAnnAttr(field, Ann2.class, "value", field, false);
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testMethodAnn2(String field, Class<?> ann, String attr, Object expected) {
        checkMethodAnnAttr(field, Ann2.class, "value", field, false);
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testMethodAnnotationAttribute(String method, Class<?> ann, String attr,
            Object expected) {
        AnnotationMetadata am = checkMethodAnnAttr(method, ann, attr, expected, false);
        // check that all other attributes are empty
        for (String a : attributes) {
            if ( !a.equals(attr) ) {
                Object empty = am.getAttribute(a, false);
                Assert.assertNull(empty, a);
            }
        }
    }
    
    @Test(dataProvider = "args", dependsOnMethods = "collectAttributes")
    public void testArgAnnotationAttribute(String attr,
            Object expected) {
        AnnotationMetadata am = checkArgAnnAttr(attr, Ann.class, attr, expected, false);
        // check that all other attributes are empty
        for (String a : attributes) {
            if ( !a.equals(attr) ) {
                Object empty = am.getAttribute(a, false);
                Assert.assertNull(empty, a);
            }
        }
    }

    private AnnotationMetadata checkMethodAnnAttr(String field, Class<?> ann, String attr,
            Object expected, boolean nullsafe) {
        AnnotationMetadata am = getMethodAnnotation(field, ann.getCanonicalName());
        return checkAnnotationAttribute(attr, expected, nullsafe, am);
    }
    
    private AnnotationMetadata checkArgAnnAttr(String field, Class<?> ann, String attr,
            Object expected, boolean nullsafe) {
        AnnotationMetadata am = getArgAnnotation(field, ann.getCanonicalName());
        return checkAnnotationAttribute(attr, expected, nullsafe, am);
    }

    private AnnotationMetadata checkFieldAnnAttr(String field, Class<?> ann, String attr,
            Object expected, boolean nullsafe) {
        AnnotationMetadata am = getFieldAnnotation(field, ann.getCanonicalName());
        return checkAnnotationAttribute(attr, expected, nullsafe, am);
    }

    private AnnotationMetadata checkAnnotationAttribute(String attr, Object expected,
            boolean nullsafe, AnnotationMetadata am) {
        Assert.assertNotNull(am);
        final Object value = am.getAttribute(expectedType(expected), attr, nullsafe);
        if ( expected.getClass().isArray() ) {
            Assert.assertEquals(Array.getLength(value), Array.getLength(expected));
            for (int i = 0; i < Array.getLength(value); i++) {
                eq(Array.get(value, i), Array.get(expected, i));
            }
        } else {
            eq(value, expected);
        }
        return am;
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testMany(String field, Class<?> ann, String attr, Object expected) {
        checkFieldAnnAttr("many", Ann.class, attr, expected, false);
    }

    @Test(dataProvider = "fields", dependsOnMethods = "collectAttributes")
    public void testManyMethod(String field, Class<?> ann, String attr, Object expected) {
        checkMethodAnnAttr("many", Ann.class, attr, expected, false);
    }
    
    @Test(dataProvider = "args", dependsOnMethods = "collectAttributes")
    public void testManyArgs(String attr, Object expected) {
        checkArgAnnAttr("many", Ann.class, attr, expected, false);
    }

    @Test(dataProvider = "defaults")
    public void testDefault(String attr, Object defaultValue) {
        checkFieldAnnAttr("empty", Ann.class, attr, defaultValue, true);
    }

    @Test(dataProvider = "defaults")
    public void testDefaultMethod(String attr, Object defaultValue) {
        checkMethodAnnAttr("empty", Ann.class, attr, defaultValue, true);
    }
    
    @Test(dataProvider = "defaults")
    public void testDefaultArg(String attr, Object defaultValue) {
        checkArgAnnAttr("empty", Ann.class, attr, defaultValue, true);
    }

    private Class<?> expectedType(Object exp) {
        if ( exp instanceof Class )
            return ClassMetadata.class;
        if ( exp instanceof Enum )
            return EnumValue.class;
        if ( exp instanceof Class[] )
            return ClassMetadata[].class;
        if ( exp instanceof Enum[] )
            return EnumValue[].class;
        return exp.getClass();
    }

    private void eq(Object value, Object expected) {
        if ( expected instanceof Class ) {
            Assert.assertTrue(value instanceof ClassMetadata);
            Assert.assertEquals(((ClassMetadata) value).getName(), ((Class<?>) expected)
                    .getCanonicalName());
        } else if ( expected instanceof Enum ) {
            Assert.assertTrue(value instanceof EnumValue);
            EnumValue ev = (EnumValue) value;
            Assert.assertEquals(ev.getValue(), ((Enum) expected).name());
            Assert.assertEquals(ev.getEnumType().getName(), expected.getClass().getCanonicalName());
        } else
            Assert.assertEquals(value, expected);
    }

    @DataProvider(name = "fields")
    public Object[][] getFields() {
        return new Object[][] {
        // @Ann(i=5)
                // private int i;
                { "i", Ann.class, "i", 5 },

                // @Ann(ins={1,5})
                // private int ins;
                { "ins", Ann.class, "ins", new int[] { 1, 5 } },

                { "fs", Ann.class, "fs", new float[] { 1f, 5f } },

                { "bs", Ann.class, "bs", new boolean[] { true, false } },

                // @Ann(s="1")
                // private int s;
                { "s", Ann.class, "s", "1" },

                // @Ann(sts={"1","5"})
                // private int sts;
                { "sts", Ann.class, "sts", new String[] { "1", "5" } },

                // @Ann(cls=String.class)
                // private int cls;
                { "cls", Ann.class, "cls", String.class },

                // @Ann(clses={String.class,Integer.class})
                // private int clses;
                { "clses", Ann.class, "clses", new Class[] { String.class, Integer.class } },

                // @Ann(cls1=Object.class)
                // private int cls1;
                { "cls1", Ann.class, "cls1", Object.class },

                // @Ann(cls1es={Object.class,Integer.class})
                // private int cls1es;
                { "cls1es", Ann.class, "cls1es", new Class[] { Object.class, Integer.class } },

                // @Ann(cls2=Float.class)
                // private int cls2;
                { "cls2", Ann.class, "cls2", Float.class },

                // @Ann(cls2es={Float.class,String.class,Integer.class})
                // private int cls2es;
                { "cls2es", Ann.class, "cls2es",
                        new Class[] { Float.class, String.class, Integer.class } },

                // @Ann(en=RetentionPolicy.CLASS)
                // private int en;
                { "en", Ann.class, "en", RetentionPolicy.CLASS },

                // @Ann(ens=RetentionPolicy.CLASS)
                // private int ens;
                { "ens", Ann.class, "ens", new Enum[] { RetentionPolicy.CLASS } } };
    }

    @DataProvider(name = "defaults")
    public Object[][] getDefaults() {
        return new Object[][] {
        // int i() default 0;
                { "i", 0 },
                // int[] ins() default {1};
                { "ins", new int[] { 1 } },
                // float[] fs() default {2f};
                { "fs", new float[] { 2f } },
                // boolean[] bs() default false;
                { "bs", new boolean[] { false } },
                // String s() default "x";
                { "s", "x" },
                // String[] sts() default {"a","b","c"};
                { "sts", new String[] { "a", "b", "c" } },
                // Class cls() default Object.class;
                { "cls", Object.class },
                // Class[] clses() default {Ann.class};
                { "clses", new Class[] { Ann.class } },
                // Class<?> cls1() default String.class;
                { "cls1", String.class },
                // Class<?>[] cls1es() default { String.class };
                { "cls1es", new Class[] { String.class } },
                // Class<? extends Serializable> cls2() default Integer.class;
                { "cls2", Integer.class },
                // Class<? extends Serializable>[] cls2es() default {
                // Integer.class , Float.class };
                { "cls2es", new Class[] { Integer.class, Float.class } },
                // RetentionPolicy en() default RetentionPolicy.RUNTIME;
                { "en", RetentionPolicy.RUNTIME },
                // RetentionPolicy[] ens() default { RetentionPolicy.SOURCE,
                // RetentionPolicy.SOURCE };
                { "ens", new Enum[] { RetentionPolicy.SOURCE, RetentionPolicy.SOURCE } } };
    }

    private AnnotationMetadata getFieldAnnotation(String field, String annCanonicalName) {
        FieldMetadata f = FieldMetadataSupport.findField(getMetadata(), field);
        Assert.assertNotNull(f, "field " + field);
        return f.getAnnotation(annCanonicalName);
    }

    private AnnotationMetadata getMethodAnnotation(String method, String annCanonicalName) {
        return findMethod( method ).getAnnotation( annCanonicalName );
    }
    
    private AnnotationMetadata getArgAnnotation(String method, String annCanonicalName) {
        return findMethod( method ).getParameters().get( 0).getAnnotation( annCanonicalName );
    }
    
    private MethodMetadata findMethod( String method ) {
        for (MethodMetadata mm : getMetadata().getMethods()) {
            if ( mm.getName().equals(method) )
                return mm;
        }
        return null;
    }

    @DataProvider(name="args")
    public Object[][] getArgs() {
        return new Object[][] {
            {"i" , 6 },
            {"ins" , new int[] { 2, 6 }},
            {"fs" , new float[] { 2f, 6f }},
            {"bs" , new boolean[]{ false, true }},
            {"s" , "2"},
            {"sts" , new String[] { "2", "6" }},
            {"cls" , Object.class},
            {"clses" , new Class[] { Integer.class, String.class }},
            {"cls1" , String.class},
            {"cls1es" , new Class[]{ Integer.class, Object.class }},
            {"cls2" , Integer.class},
            {"cls2es" , new Class[]{ Integer.class, String.class, Float.class } },
            {"en", RetentionPolicy.RUNTIME },
            {"ens" , new Enum[] { RetentionPolicy.RUNTIME } }
        };
    }
}
