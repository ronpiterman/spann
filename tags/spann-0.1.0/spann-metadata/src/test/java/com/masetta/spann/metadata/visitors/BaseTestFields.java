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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.modifier.Access;
import com.masetta.spann.metadata.core.support.FieldMetadataSupport;

public abstract class BaseTestFields extends BaseTestMetadata {
    
    public BaseTestFields(String simpleClassName ) {
        super(simpleClassName );
    }

    @Test(dataProvider="fields")
    public void testField( String name , Access access , String cls , int dim, boolean statik , boolean ffinal,
            boolean volatil , boolean transiend ) {
        FieldMetadata fm = findField( name );
        Assert.assertNotNull( fm );
        Assert.assertEquals( fm.getModifier().getAccess(), access , "field access" );
        Assert.assertEquals( fm.getFieldClass().getName(), cls , "field type");
        Assert.assertEquals( fm.getModifier().isStatic(), statik , "field static" );
        Assert.assertEquals( fm.getModifier().isFinal(), ffinal , "field final" );
        Assert.assertEquals( fm.getModifier().isVolatile(), volatil , "field volatile" );
        Assert.assertEquals( fm.getModifier().isTrasient(), transiend, "field transiend" );
    }
    
    protected FieldMetadata findField(String name) {
        return FieldMetadataSupport.findField( getMetadata(), name );
    }

}
