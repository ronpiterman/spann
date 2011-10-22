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

package com.masetta.spann.orm.jpa.support;

/**
 * Extends QueryPosition whith QueryCount implementation.
 * <p>
 * Allows page based adjusting of the first result if count is smaller than the given offset.
 * 
 * @author Ron Piterman
 */
public class QueryPositionCount extends QueryPosition implements QueryCount {
	
	private static final long serialVersionUID = 1L;

	public static enum AdjustOffset {
		NONE,TO_ZERO,TO_LAST_PAGE
	}
	
	private Number count;
	
	private AdjustOffset adjust;
	
	public QueryPositionCount(Integer offset, Integer maxResults, AdjustOffset adjust) {
		super( offset , maxResults );
		this.adjust = adjust;
	}

	public QueryPositionCount(Integer offset, Integer max ) {
		this(offset, max ,AdjustOffset.TO_ZERO);
	}

	public void setCount(Number count) {
		this.count = count;
		Integer ofs = getOffset();
		if ( ofs == null || ofs.intValue() == 0 || ofs.intValue() < count.intValue() )
			return;

		switch ( this.adjust ) {
			case NONE:
				break;
			case TO_ZERO:
				setOffset( 0 );
				break;
			case TO_LAST_PAGE:
				setOffset( lastPage( count ) );
				break;
		}
	}

	private Integer lastPage(Number cnt ) {
		if ( getMaxResults() == null )
			return 0;
		return cnt.intValue() - ( (cnt.intValue() - 1 ) % getMaxResults().intValue() ) - 1;
	}

	public Number getCount() {
		return count;
	}

}
