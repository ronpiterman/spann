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
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.ParameterMetadata;
import com.masetta.spann.metadata.core.modifier.Access;

public abstract class BaseTestMethods extends BaseTestMetadata  {
    
    public BaseTestMethods(String simpleClassName ) {
        super(simpleClassName );
    }

    @Test(dataProvider="methods")
    public void testMethods( String name , String returnType , String[] args , Access access, boolean statik ,
            boolean finall , boolean abstrakt ) {
        MethodMetadata mm = findMethod( name , args );
        Assert.assertNotNull( mm , "method not found");
        if ( returnType == null )
            Assert.assertNull( mm.getReturnClass() , "return type");
        else
            Assert.assertEquals( mm.getReturnClass().getName(), returnType, "return type" );
        Assert.assertEquals( mm.getModifier().getAccess(), access );
        Assert.assertEquals( mm.getModifier().isStatic(), statik );
        Assert.assertEquals( mm.getModifier().isFinal(), finall );
        Assert.assertEquals( mm.getModifier().isAbstract(), abstrakt );
    }
    
    protected MethodMetadata findMethod(String name, String[] args) {
        for ( MethodMetadata mm : this.getMetadata().getMethods() ) {
            if ( ! mm.getName().equals( name ) )
                continue;
            if ( argumentsMatch( mm.getParameters() , args ) )
                return mm;
        }
        return null;
    }
    
    private boolean argumentsMatch(List<ParameterMetadata> arguments,
            String[] args) {
        if ( arguments.size() != args.length )
            return false;
        for ( int i = 0; i < args.length; i++ )
            if ( ! arguments.get( i ).getParameterClass().getName().equals( args[i] ) )
                return false;
        return true;
    }
}
