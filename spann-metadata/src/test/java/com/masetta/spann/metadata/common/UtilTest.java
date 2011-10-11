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

package com.masetta.spann.metadata.common;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.common.ResourceUtil;

public class UtilTest {
    
    @Test(dataProvider="paths")
    public void testClassAndPackageName( String path , String packageName , String className ) {
        String cls = ResourceUtil.convertResourcePathToClassName( path );
        Assert.assertEquals( cls,  className ,"class name from path" );
    }
    
    @DataProvider(name="paths")
    public Object[][] getPaths() {
        return new Object[][] {
                new Object[] { "a/b/c/Foo" , "a.b.c" , "a.b.c.Foo" },
                new Object[] { "Foo" , "" , "Foo" }
        };
    }
    

}
