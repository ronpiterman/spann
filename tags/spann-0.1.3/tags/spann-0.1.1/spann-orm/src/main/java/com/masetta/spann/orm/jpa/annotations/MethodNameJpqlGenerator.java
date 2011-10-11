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

package com.masetta.spann.orm.jpa.annotations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Used internally by {@link ByMethodNameVisitor} to create a JPQL Query from 
 * a method name.
  <p>
 * Suppoerted syntax is:
 * <p><pre>
 * 
 * methodname : finder | func
 * 
 * finder     : 'findAll' | 'findBy' fields | 'getBy' fields | 'count' by
 * func       : funcname &lt;Fieldname> by
 * 
 * by         : '' | 'By' fields
 * fields     : &lt;Fieldname> ( 'And' fields )?
 * 
 * funcname   : 'sum' | 'avg' | 'max' | 'min' | 'distinct' | 'countDistinct'
 * 
 * </pre>
 * 
 * For example:
 * <pre>
 * findByName, getByEmail, countByName avgAgeByNameAndYear,
 * maxAgeByCountry, minSallaryByCounty, sumAmountByAccountId
 * distinctAgeByCountry, countDistinctNameByCountry
 * </pre>
 * 
 * And also without a where clause:
 * 
 * <pre>
 * count, avgSallary, maxSallary, minSalary, distinctPosition,
 * sumAmount
 * </pre>
 * @author Ron Piterman
 *
 */
public class MethodNameJpqlGenerator {
	
	private String methodName;
	
	private String entityClassname;
	
	private String[] namedParameters;
	
	private int pos = 0;

	public MethodNameJpqlGenerator( String methodName, String entityClassname,
			String[] namedParameters ) {
		super();
		this.methodName = methodName;
		this.entityClassname = entityClassname;
		this.namedParameters = namedParameters;
		if ( namedParameters != null && namedParameters.length == 0 )
			this.namedParameters = null;
	}
	
	public String getEql() throws ParseException {
		if ( methodName.equals("findAll") )
			return "FROM " + entityClassname;
		
		List<String> fields = new ArrayList<String>();
		
		String select = createSelectClause();
		if ( select == null )
			return null;
		
		while ( pos < methodName.length() ) {
			String field = consumeNextField( "And" );
			fields.add( StringUtils.uncapitalize( field ) );
		}
		
		if ( namedParameters != null && namedParameters.length != fields.size() )
			throw new ParseException( methodName , 0 );
		
		StringBuilder sb = new StringBuilder( select );
		sb.append( "FROM ").append( entityClassname ).append(" AS e ");
		if ( ! fields.isEmpty() ) {
			sb.append( "WHERE ");
			for ( int i = 0; i < fields.size(); i++ ) {
				sb.append("e.").append( fields.get(i) ).append( " = ");
				if ( namedParameters != null ) 
					sb.append( ":" ).append( namedParameters[i]);
				else
					sb.append("?");
				if ( i < fields.size() - 1 )
					sb.append( " AND ");
			}
		}
		
		return sb.toString();
			
	}

	private String createSelectClause() {
		if ( consume( "findBy" ) )
			return "";
		if ( consume( "getBy" ) )
			return "";
		if ( consume( "countBy" ) || ( methodName.equals( "count" ) && consume( "count" ) ) )
			return "SELECT count(e) ";
		if ( consume( "countDistinct" ) ) 
			return func( "count( distinct " , false ) + ") ";
		if ( consume( "sum" ) ) {
			return func( "sum" , true );
		}
		if ( consume( "avg" ) ) {
			return func( "avg" , true );
		}
		if ( consume( "min" ) ) {
			return func( "min" , true );
		}
		if ( consume( "max" ) ) {
			return func( "max" , true );
		}
		if ( consume( "distinct" ) ) {
			return func( "distinct " , false);
		}
		return null;
	}

	private String func(String func, boolean para) {
		String field = consumeNextField( "By" );
		return "SELECT " + func + ( para ? "(" : "" ) + "e." + StringUtils.uncapitalize( field ) + 
		 	(para ? ") " : " ");
	}

	private boolean consume(String string) {
		if ( methodName.substring( pos ).startsWith( string ) ) {
			pos += string.length();
			return true;
		}
		return false;
	}

	private String consumeNextField( String delimiter ) {
		int nextPos = pos;
		
		while ( nextPos < methodName.length() ) {
			if ( methodName.substring( nextPos ).length() < delimiter.length() + 1 ) {
				String result =  methodName.substring( pos );
				pos = methodName.length();
				return result;
			}
			if ( methodName.substring( nextPos ).matches( "^" + delimiter + "[A-Z].*") ) {
				String result = methodName.substring( pos , nextPos );
				pos = nextPos + delimiter.length();
				return result;
			}
			nextPos++;
		}
		
		throw new RuntimeException("unreachable");
		
	}

}
