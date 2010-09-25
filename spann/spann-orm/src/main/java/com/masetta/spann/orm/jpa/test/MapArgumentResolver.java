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

package com.masetta.spann.orm.jpa.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


import com.masetta.spann.orm.jpa.support.QueryCount;
import com.masetta.spann.orm.jpa.support.QueryPosition;
import com.masetta.spann.orm.jpa.support.QueryPositionCount;
import com.masetta.spann.spring.util.Resolver;

public class MapArgumentResolver extends HashMap<Class<?>,Object> implements Resolver<Object,Class<?>>{

	private static final long serialVersionUID = 1L;
	
	public MapArgumentResolver() {
		initTypes();
	}
	
	public Object resolve(Class<?> param) {
		return get( param );
	}
	
	protected void initTypes() {
		put( Long.class , 1l );
		put( Long.TYPE , 1l );
		put( Integer.class , 1);
		put( Integer.TYPE , 1 );
		put( Short.class , (short) 1 );
		put( Short.TYPE , (short) 1 );
		put( Byte.class , (byte) 1 );
		put( Byte.TYPE , (byte) 1 );
		put( Character.class , 'a' );
		put( Character.TYPE , 'a' );
		put( Double.class , 1d );
		put( Double.TYPE , 1d );
		put( Float.class , 1f );
		put( Float.TYPE , 1f );
		put( Boolean.class , true );
		put( Boolean.TYPE , true );
		put( BigDecimal.class , new BigDecimal( 1d ) );
		put( BigInteger.class , new BigInteger("1") );
		put( java.util.Date.class , new java.util.Date() );
		put( java.sql.Date.class , new java.sql.Date( System.currentTimeMillis() ) );
		put( Calendar.class , GregorianCalendar.getInstance() );
		put( String.class , "" );
		
		put( QueryPosition.class, DaoTestSupport.NULL );
		put( QueryPositionCount.class, new QueryPositionCount( null , null ) );
		put( QueryCount.class , new QueryPositionCount( null , null ) );
		
	}

}
