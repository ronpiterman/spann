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

package com.masetta.spann.spring.integration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;

import com.masetta.spann.metadata.util.EmptyArrays;
import com.masetta.spann.spring.test.AbstractSpringTest;
import com.masetta.spann.spring.test.ContextInitializer;

public class BaseIntegrationTest extends AbstractSpringTest {
	
	private Resource beansXml = new ClassPathResource("integration-tests-template.xml");
	
	protected ApplicationContext createApplicationContext() {
		String packageToScan = this.getClass().getPackage().getName();
		Resource beans = new FormattedResource( beansXml , packageToScan );
        ApplicationContext ctx = ContextInitializer.setupApplicationContext(
                this, getAutowireMode(), beans );
        return ctx;
    }

	@Override
	protected String[] getConfigLocations() {
		return EmptyArrays.STRING;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T assertSingleBeanByType( Class<T> type , String name ) {
		String[] names = getApplicationContext().getBeanNamesForType( type );
		Assert.assertEquals( names.length, 1 , "Number of beans of type " + type );
		if ( name != null ) {
			Assert.assertEquals( names[0], name , "Implement name" );
		}
		return (T) getApplicationContext().getBean( names[0] );
    }
	
	private static class FormattedResource implements Resource {
		
		private Resource resource;
		
		private Object[] formatArguments;
		
		public FormattedResource(Resource resource, Object ...formatArguments) {
			super();
			this.resource = resource;
			this.formatArguments = formatArguments;
		}

		public Resource createRelative(String relativePath) throws IOException {
			return resource.createRelative(relativePath);
		}

		public boolean exists() {
			return resource.exists();
		}

		public String getDescription() {
			return resource.getDescription();
		}

		public File getFile() throws IOException {
			return resource.getFile();
		}

		public String getFilename() {
			return resource.getFilename();
		}

		public InputStream getInputStream() throws IOException {
			String asString = IOUtils.toString( resource.getInputStream() );
			String formatted = MessageFormat.format(asString, formatArguments );
			return IOUtils.toInputStream( formatted );
		}

		public URI getURI() throws IOException {
			return resource.getURI();
		}

		public URL getURL() throws IOException {
			return resource.getURL();
		}

		public boolean isOpen() {
			return resource.isOpen();
		}

		public boolean isReadable() {
			return resource.isReadable();
		}

		public long lastModified() throws IOException {
			return resource.lastModified();
		}
		
	}

}
