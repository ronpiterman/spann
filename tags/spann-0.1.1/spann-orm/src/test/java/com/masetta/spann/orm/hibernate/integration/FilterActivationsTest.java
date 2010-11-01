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

package com.masetta.spann.orm.hibernate.integration;

import java.util.ArrayList;

import javax.persistence.Query;

import org.easymock.EasyMock;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.testng.annotations.Test;

import com.masetta.spann.orm.hibernate.integration.application.FiltersDao;
import com.masetta.spann.orm.hibernate.support.FilterActivation;
import com.masetta.spann.orm.hibernate.support.FilterActivations;


public class FilterActivationsTest extends BaseHibIntegrationTest {
	
	private FiltersDao filtersDao;

	@Test
	public void testFiltersNull() {
		expectJpqlPositional( "FROM Author AS e WHERE e.name = ?", null, new ArrayList(), "a" );
		replay();
		filtersDao.findByName( "a", null );
		verify();
	}
	
	@Test
	public void testFiltersNoParams() {
		Query q = expectJpql( "FROM Author AS e WHERE e.name = ?" );
		Session s = expectFilterEnable( "a" , false );
		expectPositional( q, "x" );
		expectResult( q, new ArrayList() );
		expectFilterDisable(s, "a" );
		
		replay();
		
		FilterActivations fa = new FilterActivations( new FilterActivation( "a" ) );
		
		filtersDao.findByName( "x", fa );
		verify();
	}
	
	@Test
	public void testDynamicFilter() {
		Query q = expectJpql( "FROM Author AS e WHERE e.name = ?" );
		Session s = expectFilterEnable( "age" , false , "age" , 37 );
		expectPositional( q, "x" );
		expectResult( q, new ArrayList() );
		expectFilterDisable(s, "age" );
		
		replay();
		
		filtersDao.findByNameAndAge("x", 37 );
		
		verify();
	}


	private Session expectFilterEnable(String filter , boolean alreadyEnabled, Object ...params  ) {
		Session session = expectSessionAccess();
		EasyMock.expect( session.getEnabledFilter( filter ) ).andReturn(
				alreadyEnabled ? createMock( Filter.class ) : null );
		
		Filter filterMock = createMock( Filter.class );
		EasyMock.expect( session.enableFilter( filter ) ).andReturn(
				filterMock );
		
		for ( int i = 0; i < params.length; ) {
			filterMock.setParameter( (String)params[i++], params[i++]);
			EasyMock.expectLastCall().andReturn( filterMock );
		}
		
		filterMock.validate();
		EasyMock.expectLastCall();
		
		return session;
	}
	
	public void expectFilterDisable( Session session, String filter ) {
		session.disableFilter( filter );
		EasyMock.expectLastCall();
	}

	public void setFiltersDao(FiltersDao filtersDao) {
		this.filtersDao = filtersDao;
	}
}
