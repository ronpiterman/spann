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

package com.masetta.spann.metadata.util;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.metadata.util.Pair;

public class PairTest {
	
	@Test(dataProvider="pairs")
	public void testPairs( Pair<?,?> p1 , Pair<?,?> p2 , boolean eq ) {
		if ( eq ) {
			Assert.assertEquals( p1, p2 );
			Assert.assertEquals( p2, p1 );
			Assert.assertEquals( p1.hashCode(), p2.hashCode() );
		}
		else {
			Assert.assertFalse( p1.equals( p2 ) );
			Assert.assertFalse( p2.equals( p1 ) );
			p1.hashCode();
			p2.hashCode();
		}
	}
	
	@DataProvider(name="pairs")
	public Object[][] getPairs() {
		
		Pair<Object,Object> pn = new Pair<Object, Object>(null,null);
		Pair<Object,Object> pn2 = new Pair<Object, Object>(null,null);
		
		Pair<Object,Object> pnl = new Pair<Object, Object>(null,"");
		Pair<Object,Object> pnl2 = new Pair<Object, Object>(null,"");
		Pair<Object,Object> pnl3 = new Pair<Object, Object>(null,"s");
		
		Pair<Object,Object> pnr = new Pair<Object, Object>("",null);
		Pair<Object,Object> pnr2 = new Pair<Object, Object>("",null);
		Pair<Object,Object> pnr3 = new Pair<Object, Object>("s",null);
		
		Pair<Object,Object> p1 = new Pair<Object, Object>("a","s");
		Pair<Object,Object> p2 = new Pair<Object, Object>("a","s");
		
		Pair<Object,Object> p3 = new Pair<Object, Object>(1,2l);
		Pair<Object,Object> p4 = new Pair<Object, Object>(1,2l);
		
		return new Object[][] {
				{ pn , pn2 , true },
				{ pnl, pnl2 , true },
				{ pnr , pnr2 , true },
				{ p1 , p2 , true },
				{ p3 , p4 , true },
				
				{ pn , pnl , false },
				{ pn , pnl3 , false },
				{ pn , pnr , false },
				{ pn , pnr3 , false },
				{ pn , p1 , false },
				{ pn , p3 , false },
				{ pnl , pnr , false },
				{ pnl , pnl3 , false },
				{ pnl , pnr3 , false },
				{ pnl , p1 , false },
				{ pnl , p3 , false },
				{ pnr , pnr3 , false },
				{ pnr , p1 , false },
				{ pnr , p3 , false },
				{ p1 , p3 , false }
		};
	}

}
