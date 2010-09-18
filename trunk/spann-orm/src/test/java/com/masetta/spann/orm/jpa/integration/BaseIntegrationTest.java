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

package com.masetta.spann.orm.jpa.integration;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.springframework.util.StringUtils;
import org.testng.annotations.BeforeMethod;

import com.masetta.spann.orm.jpa.integration.application.EntityManagerMockProvider;
import com.masetta.spann.orm.jpa.support.QueryPosition;
import com.masetta.spann.spring.test.AbstractSpringTest;

public abstract class BaseIntegrationTest extends AbstractSpringTest {

	protected EntityManagerMockProvider entityManagerMockProvider;

	protected List<Object> mocks = new ArrayList<Object>();
	
	protected EntityManager entityManager;
	
	protected Query query;
	
	private String configLocation = "META-INF/daoApplicationContext.xml";
	
	@BeforeMethod
	public void clearMocks() {
		this.mocks.clear();
		this.entityManager = em();
		this.query = null;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { configLocation };
	}
	
	protected void expectJpqlPositional( String jpql , QueryPosition qp , List<?> result, Object ...args ) {
		Query q = expectJpql( jpql );
		expect( q , qp );
		expectPositional( q , args );
		expectResult( q , result );
	}
	
	protected void expectJpqlNamed( String jpql , QueryPosition qp , List<?> result, Object ...namesAndArgs ) {
		Query q = expectJpql( jpql );
		expect( q , qp );
		expectNamed( q , namesAndArgs );
		expectResult( q , result );
	}
	
	protected void expectJpqlNamedUpdate( String jpql , int result , Object ...namesAndArgs ) {
		Query q = expectJpql( jpql );
		expectNamed( q , namesAndArgs );
		expectUpdate( q , result );
	}


	private void expectUpdate(Query q, int result) {
		EasyMock.expect( q.executeUpdate() ).andReturn( result );
	}

	private void expectNamed(Query q, Object... namesAndArgs) {
		for ( int i = 0; i < namesAndArgs.length ; i++ ) {
			q.setParameter( (String)namesAndArgs[i++], namesAndArgs[i] );
			EasyMock.expectLastCall().andReturn( q );
		}
	}

	protected void expectJpqlPositionalSingle( String jpql , QueryPosition qp , Object result, Object ...args ) {
		Query q = expectJpql( jpql );
		expect( q , qp );
		expectPositional( q , args );
		expectSingle( q , result );
	}
	
	protected void expectCountPoisitional( String jpql , QueryPosition qp , Object result, Object ...args ) {
		Query q = expectJpql( jpql );
		expect( q , qp );
		expectPositional( q , args );
		expect( q , new QueryPosition( 0 , null ) );
		expectSingle( q , result );
	}

	private void expectResult(Query q, List<?> result) {
		EasyMock.expect(q.getResultList()).andReturn( result );
	}
	
	private void expectSingle(Query q, Object result) {
		EasyMock.expect(q.getSingleResult()).andReturn( result );
	}

	private void expectPositional(Query q, Object ...args) {
		for ( int i = 0; i < args.length ; i++ ) {
			q.setParameter( i + 1, args[i] );
			EasyMock.expectLastCall().andReturn( q );
		}
	}

	private void expect(Query q, QueryPosition qp) {
		if ( qp == null )
			return;
		if ( qp.getOffset() != null ) {
			q.setFirstResult( qp.getOffset() );
			EasyMock.expectLastCall().andReturn( q );
		}
		if ( qp.getMaxResults() != null ) {
			q.setMaxResults( qp.getMaxResults() );
			EasyMock.expectLastCall().andReturn( q );
		}
	}

	private Query expectJpql(String q) {
		Query query= createMock( Query.class );
		EasyMock.expect( entityManager.createQuery( q ) ).andReturn( query );
		return query;
	}

	protected String jpql(String select, Class<?> cls, String where) {
		String q = select + " FROM " + cls.getCanonicalName() + " AS e" +
				( StringUtils.hasLength( where ) ? ( " WHERE " + where ) : "");
		return q.trim();
	}

	public void setEntityManagerMockProvider(EntityManagerMockProvider entityManagerMockProvider) {
		this.entityManagerMockProvider = entityManagerMockProvider;
	}
	
	protected void add( Object mock ) {
		this.mocks.add( mock );
	}
	
	protected EntityManager em() {
		EntityManager em = createMock( EntityManager.class );
		this.entityManagerMockProvider.setEntityManager( em );
		return em;
	}
	
	private <T> T createMock( Class<T> type ) {
		T t = EasyMock.createStrictMock( type );
		this.mocks.add( t );
		return t;
	}
	
	protected void replay() {
		for ( Object mock : mocks ) {
			EasyMock.replay( mock );
		}
	}
	
	protected void verify() {
		for ( Object mock : mocks ) {
			EasyMock.verify( mock );
		}
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

}
