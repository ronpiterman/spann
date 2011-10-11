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

package com.masetta.spann.metadata.core.support;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.EnumValue;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport;
import com.masetta.spann.metadata.core.support.AnnotationMetadataSupport.ResolutionPolicy;

public class AnnotationMetadataResolveAttributeTest {
	
	private static enum TestEnum {
		A , B , C
	}
	
	@Test(dataProvider="values")
	public void testValue( Object value , Object expected , ResolutionPolicy policy ) {
		try {
			Object result = AnnotationMetadataSupport.resolveAttributeValue( value , policy , policy );
			if ( value.getClass().isArray() ) {
				Assert.assertEquals( (Object[]) result, (Object[]) expected ); 
			}
			else {
				Assert.assertEquals( result, expected );
			}
		}
		catch ( IllegalArgumentException iae ) {
			Assert.assertNull( expected , "IllegalArgumentException thrown, but expected " + expected );
		}
	}
	
	@DataProvider(name="values")
	public Object[][] getValues() {
		return new Object[][] {
				{ 1 , 1 , ResolutionPolicy.FULL },
				{ true , true , ResolutionPolicy.FULL },
				{ "s" , "s" , ResolutionPolicy.FULL },
				{ ev( TestEnum.A ), TestEnum.A , ResolutionPolicy.FULL },
				{ ev( TestEnum.A ) , "A" , ResolutionPolicy.STRING },
				{ cm( Object.class ), Object.class , ResolutionPolicy.FULL },
				{ cm( Object.class ) , Object.class.getCanonicalName() , ResolutionPolicy.STRING },
				
				{ new Integer[] { 1 , 2 }  , new Integer[] { 1 , 2 } , ResolutionPolicy.FULL },
				{ new Integer[] {}  , new Integer[] {} , ResolutionPolicy.FULL },
				{ new Boolean[] { true , false }  , new Boolean[] { true , false } , ResolutionPolicy.FULL },
				{ new String[] { "s1" , "s2" }  , new String[] { "s1", "s2" } , ResolutionPolicy.FULL },
				{ new EnumValue[] { ev( TestEnum.A ) , ev( TestEnum.B ) } , new TestEnum[] { TestEnum.A , TestEnum.B } , ResolutionPolicy.FULL },
				{ new EnumValue[] { ev( TestEnum.A  ), ev( TestEnum.B ) } , new String[] { "A" , "B" } , ResolutionPolicy.STRING },
				{ new EnumValue[] {} , new String[] {} , ResolutionPolicy.STRING },
				{ new EnumValue[] {} , new Enum[] {} , ResolutionPolicy.FULL },
				{ new ClassMetadata[] { cm( Object.class) , cm( Integer.class ) } , new Class[] { Object.class , Integer.class } , ResolutionPolicy.FULL },
				{ new ClassMetadata[] { cm( Object.class) , cm( Integer.class ) } , new String[] { Object.class.getCanonicalName() , Integer.class.getCanonicalName() } , ResolutionPolicy.STRING },
				{ new ClassMetadata[] {} , new String[] {} , ResolutionPolicy.STRING },
				{ new ClassMetadata[] {} , new Class[] {} , ResolutionPolicy.FULL }
		};
	}
	
	public ClassMetadata cm( Class<?> cls ) {
		ClassMetadata cm = EasyMock.createNiceMock( ClassMetadata.class );
		EasyMock.expect( cm.getName() ).andReturn( cls.getName() ).anyTimes();
		EasyMock.expect( cm.getClassLoader() ).andReturn( cls.getClassLoader() ).anyTimes();
		EasyMock.replay( cm );
		return cm;
	}
	
	public EnumValue ev( Enum<?> e ) {
		return new EnumValue( cm( e.getClass() ) , e.name() );
	}

}
