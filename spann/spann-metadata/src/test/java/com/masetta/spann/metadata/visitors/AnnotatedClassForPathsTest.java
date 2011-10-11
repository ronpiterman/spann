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

import com.masetta.spann.metadata.core.AnnotatedElementMetadata;
import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.rules.Rules;
import com.masetta.spann.metadata.visitors.annotations.Child;
import com.masetta.spann.metadata.visitors.annotations.Grandchild;
import com.masetta.spann.metadata.visitors.annotations.Parent;
import com.masetta.spann.metadata.visitors.annotations.Super;

public class AnnotatedClassForPathsTest extends BaseTestMetadata {

	public AnnotatedClassForPathsTest() {
		super("AnnotatedClassForPaths");
		setLazyLoadingRulesFactory( Rules.LAZY_EAGER_ALL );
	}
	
	@Test
	public void testAnnSuper() {
		MethodMetadata mm = getMethodMetadata( "annSuper" );
		checkPaths( mm , Super.class , 1 );
		checkValue( mm , Super.class , 0 , "annSuper" );
		
		checkPaths( mm , Child.class , 2 );
		checkValue( mm , Child.class , 0 , null );
	}
	
	@Test
	public void testAnnParent() {
		MethodMetadata mm = getMethodMetadata( "annParent" );
		checkPaths( mm , Super.class , 2 );
		checkValue( mm , Super.class , 0 , "parent" );
		
		checkPaths( mm , Parent.class , 1 );
		checkValue( mm , Parent.class , 0 , "annParent" );
		
		checkPaths( mm , Child.class , 3 );
		checkValue( mm , Child.class , 0 , null );
		
	}

	@Test
	public void testAnnChild() {
		MethodMetadata mm = getMethodMetadata( "annChild" );
		checkPaths( mm , Super.class , 3 );
		checkValue( mm , Super.class , 0 , "parent" );
		
		checkPaths( mm , Parent.class , 2 );
		checkValue( mm , Parent.class , 0 , "child" );
		
		checkPaths( mm , Child.class , 1 );
		checkValue( mm , Child.class , 0 , "annChild" );
	}
	
	@Test
	public void testAnnGrandchild() {
		MethodMetadata mm = getMethodMetadata( "annGrandchild" );
		checkPaths( mm , Super.class , 4 );
		checkValue( mm , Super.class , 0 , "parent" );
		
		checkPaths( mm , Parent.class , 3 );
		checkValue( mm , Parent.class , 0 , "child" );
		
		checkPaths( mm , Child.class , 2 );
		checkValue( mm , Child.class , 0 , "grandchild" );
		
		checkPaths( mm , Grandchild.class , 1 );
		checkValue( mm , Grandchild.class , 0 , "annGrandchild" );
	}
	
	@Test
	public void testSuperParent() {
		MethodMetadata mm = getMethodMetadata( "annSuperParent" );
		checkPaths( mm , Super.class , 1 , 2 );
		checkValue( mm , Super.class , 0 , "annSuperParent" );
		checkValue( mm , Super.class , 1 , "parent" );
		
		checkPaths( mm , Parent.class , 1 , 3 );
		checkValue( mm , Parent.class , 0 , "annSuperParent" );
		
		checkPaths( mm , Child.class , 2 , 3 );
		checkValue( mm , Child.class , 0 , null );
		checkValue( mm , Child.class , 1 , null );
	}



	private void checkValue(AnnotatedElementMetadata mm, Class<?> annotation, int index, String expected ) {
		List<AnnotationPath> list = mm.findAnnotationPaths( annotation.getCanonicalName() );
		String value = list.get( index ).getAttribute( 0, String.class, "value", false );
		Assert.assertEquals(value, expected);
	}

	private void checkPaths(AnnotatedElementMetadata mm, Class<?> annotation, int ...pathsLengths ) {
		List<AnnotationPath> list = mm.findAnnotationPaths( annotation.getCanonicalName() );
		Assert.assertEquals( list.size(), pathsLengths.length );
		for ( int i = 0; i < list.size(); i++ ) {
			Assert.assertEquals( list.get( i ).getPath().length, pathsLengths[i] );
		}
	}

}
