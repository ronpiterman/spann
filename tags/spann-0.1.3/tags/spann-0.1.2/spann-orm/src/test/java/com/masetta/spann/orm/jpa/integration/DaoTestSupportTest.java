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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.masetta.spann.orm.jpa.integration.application.Author;
import com.masetta.spann.orm.jpa.integration.application.AuthorDaoJpql;
import com.masetta.spann.orm.jpa.integration.application.DaoForTestSupport;
import com.masetta.spann.orm.jpa.integration.application.EntityManagerMockProvider;
import com.masetta.spann.orm.jpa.test.DaoMethodInvoker;
import com.masetta.spann.orm.jpa.test.DaoTestSupport;
import com.masetta.spann.orm.jpa.test.MapArgumentResolver;
import com.masetta.spann.spring.test.AbstractSpringTest;

public class DaoTestSupportTest extends AbstractSpringTest {
	
	private EntityManagerMockProvider entityManagerMockProvider;
	private List<DaoMethodInvoker> invoker;

	@BeforeClass(dependsOnMethods="setup")
	public void testAuthorDaoJpqlTests() {
		EntityManager em = EasyMock.createNiceMock( EntityManager.class );
		
		Query query = EasyMock.createNiceMock( Query.class );
		
		
		EasyMock.expect( em.createQuery( EasyMock.isA( String.class ) ) ).andReturn( query ).anyTimes();
		EasyMock.expect( em.createNamedQuery( EasyMock.isA( String.class ) ) ).andReturn( query ).anyTimes();
		
		EasyMock.expect( query.setFirstResult( EasyMock.anyInt() ) ).andReturn( query ).anyTimes();
		
		EasyMock.expect( query.setParameter( EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( query ).anyTimes();
		
		EasyMock.expect( query.setParameter( EasyMock.isA( String.class ), EasyMock.anyObject() ) ).andReturn( query ).anyTimes();
		
		EasyMock.expect( query.executeUpdate() ).andReturn( 1 ).anyTimes() ;
		EasyMock.expect( query.getResultList() ).andReturn( new ArrayList() ).anyTimes();
		EasyMock.expect( query.getSingleResult() ).andReturn( null ).anyTimes();
		
		EasyMock.replay( em , query );
		
		entityManagerMockProvider.setEntityManager( em );
	}
	
	@BeforeClass(dependsOnMethods="setup")
	public void createInvoker() {
		MapArgumentResolver r = new MapArgumentResolver();
		r.put( Comparator.class , DaoTestSupport.NULL );
		r.put( Author.class , DaoTestSupport.SKIP );
		this.invoker = DaoTestSupport.createCallbacks( getApplicationContext(), 
				 r , AuthorDaoJpql.class.getPackage() );
	}
	
	@Test(dataProvider="invoker")
	public void testDaoMethod( DaoMethodInvoker invoker ) throws NoResultException, IllegalArgumentException, InvocationTargetException {
		invoker.invoke();
	}
	
	@DataProvider(name="invoker")
	public Object[][] getInvoker() {
		return wrap( invoker );
	}
	
	@Test(dataProvider="arguments")
	public void testArguments( Class<?> dao , String methodName , Object[] expectedArgs ) {
		DaoMethodInvoker i = findInvoker( dao , methodName );
		if ( expectedArgs == null ) {
			Assert.assertNull( i );
		}
		else {
			Assert.assertEquals( i.getArguments() , expectedArgs );
		}
	}
	
	private DaoMethodInvoker findInvoker(Class<?> dao, String methodName) {
		for ( DaoMethodInvoker i : invoker ) {
			if ( i.getDaoClass().equals( dao ) && i.getMethod().getName().equals( methodName ) )
				return i;
		}
		return null;
	}

	@DataProvider(name="arguments")
	public Object[][] getArguments() {
		return new Object[][] {
				{ DaoForTestSupport.class , "findTestCollection" , new Object[] { Collections.singletonList( "" ) } },
				{ DaoForTestSupport.class , "findTestSkip" , null },
				{ DaoForTestSupport.class , "findTestSkipCollection" , null },
				{ DaoForTestSupport.class , "findTestNull" , new Object[] { null } },
				{ DaoForTestSupport.class , "findTestNullCollection" , new Object[] { Collections.singletonList( null ) } }
		};
	}
	
	private Object[][] wrap(List<?> l) {
		Object[][] o = new Object[ l.size() ][];
		for ( int i = 0; i < l.size(); i++ )
			o[i] = new Object[] { l.get( i ) };
		return o;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "META-INF/daoApplicationContext.xml" };
	}

	public void setEntityManagerMockProvider(EntityManagerMockProvider entityManagerMockProvider) {
		this.entityManagerMockProvider = entityManagerMockProvider;
	}
}
