package com.masetta.spann.spring.integration.beanconfig.wire;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.spring.integration.BaseIntegrationTest;

public class TestWire extends BaseIntegrationTest {
	
	@Test
	public void testWire() {
		Map<String,WiredBean> confBeans = getApplicationContext().getBeansOfType( WiredBean.class );
		
		Assert.assertEquals( confBeans.size(), 3 );
		Set<String> metadataName = new HashSet<String>();
		
		for ( WiredBean wb : confBeans.values() ) {
			Assert.assertEquals( wb.getBeanClassName(), AnnotatedBean.class.getCanonicalName() , "beanClassName");
			Assert.assertEquals( wb.getBeanName(), "wireTestBean" , "beanName");
			metadataName.add( wb.getMetadataName() );
		}
		
		Assert.assertEquals( metadataName.size(), 3 );
		Assert.assertTrue( metadataName.contains( "getFoo" ) );
		Assert.assertTrue( metadataName.contains( "getBar" ) );
		Assert.assertTrue( metadataName.contains( AnnotatedBean.class.getCanonicalName() ) );
		
	}

}
