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

package com.masetta.spann.orm.jpa;

import java.text.MessageFormat;
import java.text.ParseException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MessageFormatTest {
	
	private static enum TestEnum {
		enumValue;
	}
	
	@Test
	public void testGetArgs() throws ParseException {
		String pattern = "some string before {2} some string after";
		MessageFormat mf = new MessageFormat( pattern );
		String formatted = mf.format( new Object[] { 0 , 1 , 2} );
		Object[] parsed = mf.parse( formatted );
		Assert.assertEquals( parsed, new Object[] { null , null , "2" });
	}
	
	@Test
	public void testEnum() throws ParseException {
		String pattern = "abc {0} def";
		MessageFormat mf = new MessageFormat( pattern );
		String formatted = mf.format( new Object[] { TestEnum.enumValue } );
		Assert.assertEquals( formatted, "abc enumValue def");
	}
	
	@Test
	public void testClass() throws ParseException {
		String pattern = "abc {0} def";
		MessageFormat mf = new MessageFormat( pattern );
		String formatted = mf.format( new Object[] { String.class } );
		Assert.assertEquals( formatted, "abc class java.lang.String def");
	}

}
