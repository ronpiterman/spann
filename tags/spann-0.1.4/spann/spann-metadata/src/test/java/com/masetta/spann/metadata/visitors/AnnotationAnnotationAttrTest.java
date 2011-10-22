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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.AnnotationMetadata;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.visitors.classes.AnnAttr;
import com.masetta.spann.metadata.visitors.classes.AnnAttr2;

public class AnnotationAnnotationAttrTest extends BaseTestMetadata {

	public AnnotationAnnotationAttrTest() {
		super("AnnotatedClassForAnnAttr");
	}
	
	@Test
	public void testSingleAnnAttr() {
		ClassMetadata cm = getMetadata();
		AnnotationMetadata am = cm.getAnnotation( AnnAttr.class.getCanonicalName() );
		Assert.assertNotNull( am );
		
		assertAnnAttr(am, RetentionPolicy.RUNTIME , RetentionPolicy.SOURCE , RetentionPolicy.CLASS );
	}
	
	private void assertAnnAttr( AnnotationMetadata am , RetentionPolicy retention,
			RetentionPolicy ...retentions  ) {
		assertAnnAttrRetentionAttr( am, retention );
		assertRetentions(am, retentions );
	}

	private void assertAnnAttrRetentionAttr(AnnotationMetadata am, final RetentionPolicy type) {
		AnnotationMetadata ret = am.getAttribute( AnnotationMetadata.class, "retention", false );
		assertEnumValue( ret , "value" , type );
	}
	
	private void assertRetentions(AnnotationMetadata am, RetentionPolicy ...rp) {
		AnnotationMetadata[] ret = am.getAttribute( AnnotationMetadata[].class, 
				"retentions", false );
		Assert.assertNotNull( ret );
		Assert.assertEquals( ret.length, rp.length );
		for ( int i = 0; i < rp.length; i++ ) {
			assertEnumValue( ret[i] , "value" , rp[i] );
		}
	}
	
	@Test
	public void test2Levels() {
		AnnotationMetadata am = getMetadata().getAnnotation( AnnAttr2.class.getCanonicalName() );
		
		AnnotationMetadata am2 = am.getAttribute( AnnotationMetadata.class, "attr" , false );
		assertAnnAttr(am2, RetentionPolicy.RUNTIME, RetentionPolicy.CLASS , RetentionPolicy.SOURCE );
		
		AnnotationMetadata[] am3 = am.getAttribute( AnnotationMetadata[].class, "attrs" , false );
		assertAnnAttr(am3[0], RetentionPolicy.SOURCE, RetentionPolicy.SOURCE );
		assertAnnAttr(am3[1], RetentionPolicy.RUNTIME, RetentionPolicy.SOURCE , RetentionPolicy.CLASS);
		assertAnnAttr(am3[2], RetentionPolicy.CLASS, RetentionPolicy.SOURCE, RetentionPolicy.CLASS , RetentionPolicy.RUNTIME);
		
	}



	private void assertEnumValue(AnnotationMetadata ret, String name, Enum value ) {
		Assert.assertNotNull( ret );
		EnumValue v = ret.getAttribute( EnumValue.class, name, false );
		Enum res = v.resolve( value.getClass() );
		Assert.assertEquals( res , value );
	}

}
