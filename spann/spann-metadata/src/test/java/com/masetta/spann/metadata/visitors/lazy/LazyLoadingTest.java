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

package com.masetta.spann.metadata.visitors.lazy;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.reader.ClassReaderAdapter;
import com.masetta.spann.metadata.reader.asm3_2.AsmClassReaderAdapter;
import com.masetta.spann.metadata.reader.spring.SpringClassReaderAdapter;
import com.masetta.spann.metadata.rules.LazyLoadingRulesFactory;
import com.masetta.spann.metadata.rules.MetadataPathRules;
import com.masetta.spann.metadata.rules.MetadataPathRulesBuilder;
import com.masetta.spann.metadata.rules.Rules;
import com.masetta.spann.metadata.visitors.AnnotatedClassTest;
import com.masetta.spann.metadata.visitors.BaseTestMetadata;
import com.masetta.spann.metadata.visitors.ClassWithGenericFieldsTest;
import com.masetta.spann.metadata.visitors.ClassWithGenericIfcTest;
import com.masetta.spann.metadata.visitors.ClassWithGenericMethodsTest;
import com.masetta.spann.metadata.visitors.ClassWithGenericSuperTest;
import com.masetta.spann.metadata.visitors.ClassWithGenericTypeArgsTest;
import com.masetta.spann.metadata.visitors.ClassWithManyGenTest;
import com.masetta.spann.metadata.visitors.ClassWithTypeArgsTest;
import com.masetta.spann.metadata.visitors.SimpleClassFieldsTest;

public class LazyLoadingTest {
    
    @Factory(dataProvider="rules")
    public BaseTestMetadata[] createTests( LazyLoadingRulesFactory rules , ClassReaderAdapter a ) {
        BaseTestMetadata[] tests = getTestInstances();
        initLazy( tests , rules , a );
        return tests;
    }
    
    @DataProvider(name="rules")
    public Object[][] getRules() {
    	ClassReaderAdapter spring = new SpringClassReaderAdapter();
    	ClassReaderAdapter asm = new AsmClassReaderAdapter();
        return new Object[][] {
                { Rules.LAZY_LAZY , spring },
                { Rules.LAZY_EAGER_ALL , spring },
                { Rules.LAZY_EAGER_DEEP , spring },
                { Rules.LAZY_EAGER_DEEP_SINGLE_ARTIFACT , spring },
                { Rules.LAZY_EAGER_FLAT , spring },
                { Rules.LAZY_EAGER_FLAT_SINGLE_ARTIFACT , spring },
                
                { Rules.LAZY_LAZY , asm },
                { Rules.LAZY_EAGER_ALL , asm },
                { Rules.LAZY_EAGER_DEEP , asm },
                { Rules.LAZY_EAGER_DEEP_SINGLE_ARTIFACT , asm },
                { Rules.LAZY_EAGER_FLAT , asm },
                { Rules.LAZY_EAGER_FLAT_SINGLE_ARTIFACT , asm }
        };
    }

    private BaseTestMetadata[] getTestInstances() {
        BaseTestMetadata[] tests = {
            new ClassWithGenericFieldsTest(),
            new ClassWithGenericIfcTest(),
            new ClassWithGenericMethodsTest(),
            new ClassWithGenericSuperTest(),
            new ClassWithGenericTypeArgsTest(),
            new ClassWithManyGenTest(),
            new ClassWithTypeArgsTest(),
            new SimpleClassFieldsTest(),
            new AnnotatedClassTest()
        };
        init( tests );
        return tests;
    }

    private void initLazy(BaseTestMetadata[] tests , LazyLoadingRulesFactory rules , ClassReaderAdapter a ) {
        MetadataPathRulesBuilder builder = new MetadataPathRulesBuilder();
        builder.add( Artifact.CLASS, null );
        MetadataPathRules defaultRules = builder.build();
        for ( BaseTestMetadata btm : tests ) {
            btm.setRules( defaultRules , "lazy");
            btm.setLazyLoadingRulesFactory( rules );
            btm.setClassReaderAdapter( a );
        }
    }
    
    private void init(BaseTestMetadata[] tests) {
        for ( BaseTestMetadata btm : tests ) {
            btm.readMetadata();
        }
    }
    
    
}
