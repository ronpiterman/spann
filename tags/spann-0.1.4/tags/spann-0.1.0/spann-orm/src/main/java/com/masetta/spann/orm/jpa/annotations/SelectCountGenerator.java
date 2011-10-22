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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.masetta.spann.spring.util.Resolver;

/**
 * Generate a count jpql query from a jpql select/find query.
 * <p>
 * Supported are the following source jpql queries (case insensitive):
 * <dl>
 *  <dt><code>FROM Entity .... </code>
 * 	<dd>will produce <code> SELECT count(*) FROM ...</code>
 * 
 *  <dt><code> SELECT e FROM ...</code>
 * 	<dd>will produce <code> SELECT count(e.id) FROM ...</code>
 * 
 *  <dt><code> SELECT e.id FROM ...</code>
 * 	<dd>will produce <code> SELECT count(*) FROM ...</code>
 * 
 *  <dt><code> SELECT e.id, otherfields... FROM ...</code>
 * 	<dd>will produce <code> SELECT count(e.id) FROM ...</code>
 *  
 *  <dt><code> SELECT distinct e.foo, ... FROM ...</code>
 * 	<dd>will produce <code> SELECT count(distinct e.foo) FROM ...</code>
 *  
 *  <dt><code> SELECT new java.object.Name( e.id , ...), .... FROM ...</code>
 * 	<dd>will produce <code> SELECT count(e.id) FROM ....</code>
 * </dl>
 * 
 * @see Count
 * 
 * @Author Ron Piterman
 */
public class SelectCountGenerator implements Resolver<String, String> {
	
	private final static Pattern ORDER_BY = Pattern.compile( "\\s+order\\s+by\\s+.*$" );
	
	private final static Pattern SIMPLE = Pattern.compile( "select\\s+([\\{\\}0-9a-zA-Z\\.]+)(\\s*,.*)?(\\s+from\\s+.*)$" );
	private final static Pattern DISTINCT = Pattern.compile( "select\\s+(distinct\\s+[\\{\\}0-9a-zA-Z\\.]+)(\\s*,.*)?(\\s+from\\s+.*)$" );
	private final static Pattern OBJ = Pattern.compile( "select\\s+new\\s+[a-zA-Z\\.\\$]+\\(\\s*([\\{\\}0-9a-zA-Z\\.]+)\\s*(,.*)?\\)(\\s*,.*)?(\\s+from\\s+.*)$" );
	
	private ExtractPattern[] PATTERN_AND_GROUP = {
			new ExtractPattern( SIMPLE , 1 , 3 ),
			new ExtractPattern( DISTINCT , 1 , 3 ),
			new ExtractPattern( OBJ , 1 , 4 )
	};
	
	public String resolve(String jpql) {
		String trJpql = jpql.trim();
		String lcJpql = trJpql.toLowerCase();
		
		// strip order by
		final Matcher matcher = ORDER_BY.matcher( lcJpql );
		if ( matcher.find() ) {
			lcJpql = lcJpql.substring( 0 , matcher.start() );
		}
		
		if ( lcJpql.startsWith("from ") ) {
			return "SELECT count(*) " + trJpql.substring( 0 , lcJpql.length() );
		}
		if ( ! lcJpql.startsWith("select" ) ) {
			throw new IllegalArgumentException("Jpql does not start with 'select ' nor with 'from ', can not create count." );
		}
		
		return createSelectJpql(lcJpql, trJpql);
	}

	private String createSelectJpql(String lcJpql, String jpql) {
		String result;
		for ( ExtractPattern p : PATTERN_AND_GROUP ) {
			result = p.create(lcJpql, jpql);
			if ( result != null )
				return result;
		}
		throw new IllegalArgumentException("Could not extract count field from '" + jpql + "'");
	}
	
	
	private static class ExtractPattern {
		private Pattern pattern;
		private Integer fieldGroup;
		private Integer fromGroup;
		private ExtractPattern(Pattern pattern, Integer fieldGroup, Integer fromGroup) {
			super();
			this.pattern = pattern;
			this.fieldGroup = fieldGroup;
			this.fromGroup = fromGroup;
		}
		private String create( String lcJpql , String jpql ) {
			Matcher m = pattern.matcher( lcJpql );
			if ( m.matches() ) {
				String field = getGroup( m, fieldGroup , jpql );
				String from = getGroup( m, fromGroup , jpql );
				return "SELECT count(" + field.trim() + ") " + from.trim() ;
			}
			return null;
		}
		private String getGroup(Matcher m, Integer group, String source) {
			return source.substring( m.start( group ) , m.end( group ) );
		}
	}

}
