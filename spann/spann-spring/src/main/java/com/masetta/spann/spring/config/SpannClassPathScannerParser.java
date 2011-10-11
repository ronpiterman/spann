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

package com.masetta.spann.spring.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.annotation.ScopedProxyMode;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.core.ClassPathScanner;
import com.masetta.spann.spring.core.ScopeProxyModeResolver;
import com.masetta.spann.spring.core.ScopeProxyModeResolverImpl;

public class SpannClassPathScannerParser implements BeanDefinitionParser {
    
    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    
    private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-proxy-mode-resolver";

    private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy-mode";
    
    private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";
    
    private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";
    
    private static final String VISITOR_ELEMENT = "visitor";
    
    private static final String VISITOR_CLASS_ATTRIBUTE = "class";
    
    private final SpannLog log = SpannLogFactory.getLog( SpannClassPathScannerParser.class ); 
    
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        ClassPathScanner scanner = configureScanner(parserContext, element);
        scanner.scan( parserContext.getRegistry() );
        return null;
    }
    
    private ClassPathScanner configureScanner(ParserContext parserContext,
            Element element) {
        XmlReaderContext readerContext = parserContext.getReaderContext();
        
        ClassPathScanner scanner = new ClassPathScanner();
        
        scanner.setResourceLoader(readerContext.getResourceLoader());
        
        scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
        
        String basePackages = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
        if ( basePackages == null ) {
        	throw new BeanDefinitionStoreException(BASE_PACKAGE_ATTRIBUTE + " attribute is required.");
        }
        scanner.setBasePackagesNames( basePackages );

        if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
            scanner.setResourcePattern(element.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
        }

        try {
            parseBeanNameGenerator(element, scanner);
        }
        catch (Exception ex) {
            readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
        }

        try {
            parseScope(element, scanner);
        }
        catch (Exception ex) {
            readerContext.error(ex.getMessage(), readerContext.extractSource(element), ex.getCause());
        }
        
        addVisitors( element , scanner );
        
        return scanner;
    }
    
	private void addVisitors(Element element, ClassPathScanner scanner) {
        
        NodeList visitorsList = element.getElementsByTagName( VISITOR_ELEMENT );
        List<MetadataVisitor<ClassMetadata>> visitors = new ArrayList<MetadataVisitor<ClassMetadata>>( visitorsList.getLength() );
        for ( int i = 0; i < visitorsList.getLength() ; i++ ) {
            Element visitorElement = (Element) visitorsList.item( i );
            visitors.add( createVisitor( visitorElement , scanner.getResourceLoader().getClassLoader() ) );
        }
        
        if ( ! visitors.isEmpty() ) {
        	scanner.setMetadataVisitors( visitors );
        }
    }


    @SuppressWarnings("unchecked")
	private MetadataVisitor<ClassMetadata> createVisitor( Element visitorElement , ClassLoader clsLoader ) {
        String className = visitorElement.getAttribute( VISITOR_CLASS_ATTRIBUTE );
        MetadataVisitor<?> v = instantiateUserDefinedStrategy(className, MetadataVisitor.class, 
                clsLoader );
        return (MetadataVisitor) SpannLogFactory.createTraceProxy( v, log, MetadataVisitor.class );
    }

    protected void parseBeanNameGenerator(Element element, ClassPathScanner scanner) {
        if (element.hasAttribute(NAME_GENERATOR_ATTRIBUTE)) {
            BeanNameGenerator beanNameGenerator = (BeanNameGenerator) instantiateUserDefinedStrategy(
                    element.getAttribute(NAME_GENERATOR_ATTRIBUTE), BeanNameGenerator.class,
                    scanner.getResourceLoader().getClassLoader());
            scanner.setBeanNameGenerator(beanNameGenerator);
        }
    }

    /**
     * Register the specified bean with the given registry.
     * <p>Can be overridden in subclasses, e.g. to adapt the registration
     * process or to register further bean definitions for each scanned bean.
     * @param definitionHolder the bean definition plus bean name for the bean
     * @param registry the BeanDefinitionRegistry to register the bean with
     */
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }
    
    protected void parseScope(Element element, ClassPathScanner scanner) {
        // Register ScopeMetadataResolver if class name provided.
        if ( element.hasAttribute(SCOPE_RESOLVER_ATTRIBUTE)) {
            if (element.hasAttribute(SCOPED_PROXY_ATTRIBUTE)) {
                throw new IllegalArgumentException(
                        "Cannot define both '" + SCOPE_RESOLVER_ATTRIBUTE + "' and '" + SCOPED_PROXY_ATTRIBUTE + "' on <scan> tag");
            }
            ScopeProxyModeResolver resolver  = (ScopeProxyModeResolver) instantiateUserDefinedStrategy(
                    element.getAttribute(SCOPE_RESOLVER_ATTRIBUTE), ScopeProxyModeResolver.class,
                    scanner.getResourceLoader().getClassLoader());
            scanner.setScopeProxyModeResolver( resolver );
        }

        if (element.hasAttribute(SCOPED_PROXY_ATTRIBUTE)) {
            String mode = element.getAttribute(SCOPED_PROXY_ATTRIBUTE);
            if ("targetClass".equals(mode)) {
                scanner.setScopeProxyModeResolver( new ScopeProxyModeResolverImpl( ScopedProxyMode.TARGET_CLASS ) );
            }
            else if ("interfaces".equals(mode)) {
                scanner.setScopeProxyModeResolver( new ScopeProxyModeResolverImpl( ScopedProxyMode.INTERFACES ) );
            }
            else if ("no".equals(mode)) {
                scanner.setScopeProxyModeResolver( new ScopeProxyModeResolverImpl( ScopedProxyMode.NO ) );
            }
            else {
                throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T instantiateUserDefinedStrategy(String className, Class<T> strategyType, ClassLoader classLoader) {
        Object result = null;
        try {
            result = classLoader.loadClass(className).newInstance();
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Class [" + className + "] for strategy [" +
                    strategyType.getName() + "] not found", ex);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Unable to instantiate class [" + className + "] for strategy [" +
                    strategyType.getName() + "]. A zero-argument constructor is required", ex);
        }

        if (!strategyType.isAssignableFrom(result.getClass())) {
            throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
        }
        return (T) result;
    }


}
