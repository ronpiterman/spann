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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.TypeMetadata;
import com.masetta.spann.metadata.core.support.ClassMetadataSupport;
import com.masetta.spann.metadata.rules.Rules;
import com.masetta.spann.metadata.visitors.classes.GenericTypeFind;

public class GenericTypeFindTest extends BaseTestMetadata {
	
	private static final String SUPER = GenericTypeFind.class.getCanonicalName() + "$Super";
	
	private static final String AUX = GenericTypeFind.class.getCanonicalName() + "$Aux";
	
	private static final String AUX_IMPL = GenericTypeFind.class.getCanonicalName() + "$AuxImpl";

	public GenericTypeFindTest() {
		super("GenericTypeFind");
		setLazyLoadingRulesFactory( Rules.LAZY_EAGER_ALL );
	}
	
	@Test
	public void testSame() {
		ClassMetadata cm = readClassMetadata( "GenericTypeFind$Super" );
		ClassMetadata generic = ClassMetadataSupport.findTypeParameterCapture( cm, SUPER, 
				0 );
		Assert.assertNull( generic );
	}
	
	@Test
	public void testSub() {
		ClassMetadata cm = readClassMetadata( "GenericTypeFind$Sub" );
		ClassMetadata generic = ClassMetadataSupport.findTypeParameterCapture( cm, SUPER, 
				0 );
		Assert.assertEquals( "java.lang.Object", generic.getName() );
	}
	
	@Test
	public void testMix() {
		ClassMetadata cm = readClassMetadata( "GenericTypeFind$Mix" );
		ClassMetadata generic = ClassMetadataSupport.findTypeParameterCapture( cm, SUPER , 
				0 );
		Assert.assertEquals( generic.getName() , AUX );
		Assert.assertTrue( generic instanceof TypeMetadata );
		Assert.assertEquals( ((TypeMetadata)generic).getTypeArguments().get( 0 ).getType().getName(), 
				"java.lang.Boolean" );
	}
	
	@Test
	public void testSubmix() {
		ClassMetadata cm = readClassMetadata( "GenericTypeFind$Submix" );
		ClassMetadata generic = ClassMetadataSupport.findTypeParameterCapture( cm, SUPER , 
				0 );
		Assert.assertEquals( generic.getName() , AUX_IMPL );
		Assert.assertTrue( generic instanceof TypeMetadata );
		Assert.assertEquals( ((TypeMetadata)generic).getTypeArguments().get( 0 ).getType().getName(), 
				"java.lang.Boolean" );
	}


}
