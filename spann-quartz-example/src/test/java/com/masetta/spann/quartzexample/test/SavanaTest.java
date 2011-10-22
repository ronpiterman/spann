package com.masetta.spann.quartzexample.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.masetta.spann.spring.test.AbstractSpringTest;

public class SavanaTest extends AbstractSpringTest {
	
	private Savana savana;

	public void setSavana(Savana savana) {
		this.savana = savana;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "META-INF/applicationContext.xml" };
	} 
	
	@Test
	public void waitTenSeconds() {
		try {
			synchronized( this ) {
				this.wait( 10000 );
			}
		}
		catch ( InterruptedException ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	@Test(dependsOnMethods="waitTenSeconds")
	public void testElephantsCount() {
		int e = savana.getNewElephants();
		Assert.assertTrue( e >= 5 );
	}
	
	@Test(dependsOnMethods="waitTenSeconds")
	public void testZebrasCount() {
		int z = savana.getNewZebras();
		Assert.assertTrue( z >= 50 );
	}


}
