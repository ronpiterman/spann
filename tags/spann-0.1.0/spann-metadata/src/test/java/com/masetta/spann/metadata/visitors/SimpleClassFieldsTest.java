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

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.modifier.Access;

public class SimpleClassFieldsTest extends BaseTestFields {
    
    public static final String[] EMPTY = new String[0];
    
    public SimpleClassFieldsTest() {
        super("SimpleClass");
    }

    @Test
    public void testMetadataOk() {
        Assert.assertNotNull( getMetadata() );
    }
    
    @Test
    public void testSignature() {
        Assert.assertEquals( getMetadata().getModifier().getAccess() , Access.PUBLIC );
    }
    
    @Test
    public void testSuper() {
        Assert.assertEquals( getMetadata().getSuperClass( false ).getName(), getTestClassName( "PreventAccess") );
    }
    
    @Test
    public void testInterfaces() {
        Set<ClassMetadata> ifcs = getMetadata().getInterfaces(true);
        Assert.assertEquals( ifcs.size(), 1 );
        ClassMetadata cm = firstOrNull( ifcs );
        Assert.assertEquals( cm.getName(), getTestClassName( "VisitedInterface") );
    }
    
    @DataProvider(name="fields")
    public Object[][] getFields() {
        return new Object[][] {
                // name , access , className , array dim , static , final , volatile , transient
                new Object[] { "staticField" , Access.PRIVATE , Boolean.TYPE.getName() , 0, true , false , false , false },
                new Object[] { "staticFinalField" , Access.PRIVATE , Boolean.TYPE.getName() , 0, true , true , false , false },
                new Object[] { "privateField" , Access.PRIVATE , Date.class.getName() , 0, false , false , false , false },
                new Object[] { "privateFinalField" , Access.PRIVATE , Date.class.getName() , 0, false , true , false , false },
                new Object[] { "protectedField" , Access.PROTECTED , String.class.getName() , 0, false , false , false , false },
                new Object[] { "protectedFinalField" , Access.PROTECTED , String.class.getName() , 0, false , true , false , false },
                new Object[] { "defaultField" , Access.DEFAULT , Integer.TYPE.getName() , 0, false , false , false , false },
                new Object[] { "defaultFinalField" , Access.DEFAULT , Integer.TYPE.getName() , 0, false , true , false , false },
                new Object[] { "publicField" , Access.PUBLIC , List.class.getName() , 0, false , false , false , false },
                new Object[] { "publicFinalField" , Access.PUBLIC, List.class.getName() , 0, false , true , false , false },
                new Object[] { "volatileField" , Access.PRIVATE , Integer.class.getName() , 0, false , false , true, false },
                new Object[] { "transientField" , Access.PRIVATE , Float.class.getName() , 0, false , false , false , true },
                new Object[] { "transientField" , Access.PRIVATE , Float.class.getName() , 0, false , false , false , true },
                new Object[] { "i" , Access.PRIVATE , Integer.TYPE.getName() , 1, false , false , false , false },
                new Object[] { "ii" , Access.PRIVATE , Integer.TYPE.getName() , 2, false , false , false , false },
                new Object[] { "iii" , Access.PRIVATE , Integer.TYPE.getName() , 3, false , false , false , false },
                new Object[] { "s1" , Access.PRIVATE , String.class.getName() , 1, false , false , false , false },
                new Object[] { "s2" , Access.PRIVATE , String.class.getName() , 2, false , false , false , false },
                new Object[] { "s3" , Access.PRIVATE , String.class.getName() , 3, false , false , false , false },
                
        };
    }

}
