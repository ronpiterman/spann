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

package com.masetta.spann.spring.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.masetta.spann.metadata.ClassMetadataSource;
import com.masetta.spann.metadata.common.RuntimeIOException;
import com.masetta.spann.metadata.common.UrlResource;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.util.SpannLog;
import com.masetta.spann.metadata.util.SpannLogFactory;
import com.masetta.spann.spring.MetadataVisitor;
import com.masetta.spann.spring.RuntimeSpannReference;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.ScanFinishedListener;
import com.masetta.spann.spring.core.visitor.DefaultVisitor;
import com.masetta.spann.spring.util.Reference;

/**
 * A utility object that scans the classpath and delegates the scanned metadata to
 * visitors, which, in turn, may create or manipulate existing
 * {@link BeanDefinitionHolder}s according to the given metadata and the
 * visitor's implementation.
 * <p>
 * Users <b>must</b> configure the <code>basePackages</code> property. All other
 * properties have default values.
 * <p>
 * The #set
 * 
 * <p>
 * This implementation is based on Spanns's {@link ClassMetadataSource}
 * facility, backed by an ASM ClassReader.
 * 
 * <p>
 * The implementation is based on Spring's {@link ClassPathBeanDefinitionScanner} and
 * {@link ComponentScanBeanDefinitionParser}.
 * 
 * @author Ron Piterman    
 */
public class ClassPathScanner implements ResourceLoaderAware {

    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    protected static final String BEAN_NAME_SEPARATOR = "#";

    protected final SpannLog logger = SpannLogFactory.getLog(getClass());
    
    private ScopeProxyModeResolver scopeProxyModeResolver = new ScopeProxyModeResolverImpl( ScopedProxyMode.NO );

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    
    private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
    
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
    
    private ClassMetadataSource classMetadataSource = ClassPathScannerSupport.createDefaultCms();

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    
    private List<String> basePackages;

    private List<MetadataVisitor<ClassMetadata>> metadataVisitors = 
    	ClassPathScannerSupport.createDefaultVisitors();

    /**
     * Create a ClassPathScanner.
     */
    public ClassPathScanner() {
    }

    /**
     * Should be set by the container as part of the ResourceLoaderAware contract.
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    /**
     * Return the ResourceLoader that this component provider uses.
     */
    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    /**
     * Set the resource pattern to use when scanning the classpath. This value
     * will be appended to each base package name.
     * 
     * @see #findCandidateComponents(String)
     * @see #DEFAULT_RESOURCE_PATTERN
     */
    public void setResourcePattern(String resourcePattern) {
        Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
        this.resourcePattern = resourcePattern;
    }

    /**
     * Scan the class path. All resources matching the configured 
     * {@link #setBasePackages(List) basePackages } and the {
     * @link #setResourcePattern(String) resourcePattern} will be passed to the
     * {@link ClassMetadataSource} and the result metadata passed to each configured {@link MetadataVisitor}
     * along with a {@link ScanContext} created by this Object.
     *  
     * @param registry BeanDefinitionRegistry to add beans into.
     * 
     * @see #setBasePackages(List)
     * @see #setResourcePattern(String)
     * @see #setMetadataVisitors(List)
     * @see #setClassMetadataSource(ClassMetadataSource)
     * @see MetadataVisitor
     * @see ScanContext
     */
    public void scan( final BeanDefinitionRegistry registry) {
        Registry beanNameRegistry = new RegistryImpl(registry, beanNameGenerator );
        Set<BeanDefinitionHolder> defs = doScan( beanNameRegistry );
        fireScanFinished( defs );
        boolean debug = this.logger.isDebugEnabled();
        for ( BeanDefinitionHolder source : defs ) {
        	
            String scope = source.getBeanDefinition().getScope();
            if (! ( BeanDefinition.SCOPE_PROTOTYPE.equals( scope )  || BeanDefinition.SCOPE_SINGLETON.equals( scope ) ) ) {
                ScopedProxyMode mode = this.scopeProxyModeResolver.resolveScopedProxyMode( source.getBeanDefinition() );
                if ( ! ScopedProxyMode.NO.equals( mode ) ) {
                    BeanDefinitionHolder toRegister = ScopedProxyCreator.createScopedProxy( source, registry, ScopedProxyMode.TARGET_CLASS.equals( mode ) );
                    if ( debug )
                        logger.debug( "registerring " + toRegister.toString() + " as scoped proxy (" + scope + ") of " + source.toString() );
                    BeanDefinitionReaderUtils.registerBeanDefinition(toRegister, registry);
                    continue;
                }
            
            }
            if ( debug )
                logger.debug( "registerring " + source.toString() + " in scope " + scope );
            BeanDefinitionReaderUtils.registerBeanDefinition(source, registry);
        }
        
    }

    private void fireScanFinished(Set<BeanDefinitionHolder> defs) {
        for ( BeanDefinitionHolder h : defs ) {
            maybeFireScanFinished( h );
            maybeFireScanFinished( h.getBeanDefinition() );
        }
    }

    private void maybeFireScanFinished( Object o) {
        if ( o instanceof ScanFinishedListener )
            ((ScanFinishedListener)o).scanFinished();
    }

    /**
     * Perform a scan within the specified base packages, returning the
     * all bean definitions created within the scan.
     * <p>
     * This method does <i>not</i> register any beans, but rather leaves this up to the caller.
     * 
     * @param registry a BeanNameRegistry, used to decouple this method from
     * 	the spring bean definition registry.
     * 
     * @return a set of all BeanDefinitionHolder created during the scan.
     * 
     */
    public Set<BeanDefinitionHolder> doScan( final Registry registry ) {
        final Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<BeanDefinitionHolder>();
        final Reference<Resource> resourceRef = new Reference<Resource>( null );
        ClassLoader classLoader = this.resourcePatternResolver.getClassLoader();
        ScanContext scanContext = new ScanContextImpl(beanDefinitionHolders, beanDefinitionDefaults, registry);
        
        scanContext = (ScanContext) SpannLogFactory.createTraceProxy( scanContext, logger, ScanContext.class );

        for ( String pkg : this.getBasePackages() ) {
            doScanPackage( pkg, scanContext, resourceRef , classLoader );
        }
        
        resolveReferences( beanDefinitionHolders , scanContext );
        
        return beanDefinitionHolders;
        
    }

    private void resolveReferences(Set<BeanDefinitionHolder> beanDefinitionHolders,
			ScanContext scanContext) {
    	Set<Object> visited = new HashSet<Object>();
    	BeanDefinition bd;
		for ( BeanDefinitionHolder bdh : beanDefinitionHolders ) {
			bd = bdh.getBeanDefinition();
			resolveReferences( bd , scanContext , visited );
		}
	}

	private Object resolveReferences( Object o, ScanContext scanContext, Set<Object> visited) {
		if ( o instanceof BeanDefinitionHolder ) {
			resolveReferences( ((BeanDefinitionHolder)o).getBeanDefinition() , scanContext , visited );
		}
		if ( o instanceof BeanDefinition ) {
			if ( visited.add( o ) ) 
				resolveProperties( (BeanDefinition)o , scanContext , visited );
		}
		else if ( o instanceof Map ) {
			resolveReferences( ((Map<?,?>) o).values() , scanContext , visited );
		}
		else if ( o instanceof Collection ) {
			for ( Object e : ((Collection<?>)o ) ) {
				resolveReferences( e , scanContext , visited );
			}
		}
		else if ( o instanceof RuntimeSpannReference ) {
			RuntimeSpannReference ref = (RuntimeSpannReference) o;
			BeanDefinitionHolder h = scanContext.getAttachedBean( ref.getMetadata(),
					ref.getScope(), ref.getRole() );
			return new RuntimeBeanReference( h.getBeanName() );
		}
		return o;
	}

	private void resolveProperties(BeanDefinition o, ScanContext scanContext, Set<Object> visited) {
		for ( PropertyValue propValue : new ArrayList<PropertyValue>( o.getPropertyValues().getPropertyValueList() ) ) {
			Object val = propValue.getValue();
			Object newVal = resolveReferences( val, scanContext , visited );
			if ( newVal != val ) {
				o.getPropertyValues().addPropertyValue( propValue.getName(), newVal );
			}
		}
	}

	/**
     * Scan the class path for candidate components.
     * 
     * @param basePackage
     *            the package to check for annotated classes
     * @param resourceRef 
     * @param classLoader 
     * @return a corresponding Set of autodetected bean definitions
     */
    private void doScanPackage(String basePackage, ScanContext scanContext, Reference<Resource> resourceRef, ClassLoader classLoader) {
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + resolveBasePackage(basePackage)
                    + "/"
                    + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver
                    .getResources(packageSearchPath);
            boolean debugEnabled = logger.isDebugEnabled();
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                if (debugEnabled) {
                    logger.debug("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    ClassMetadata metadata = classMetadataSource.getClassMetadata( 
                    		spannResourceFor( resource , classLoader ) );
                    resourceRef.setValue( resource );
                    visit(metadata, scanContext);
                } else {
                    if (debugEnabled) {
                        logger.debug("Ignored because not readable: "
                                + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "I/O failure during classpath scanning", ex);
        }
    }

    private com.masetta.spann.metadata.common.Resource spannResourceFor(Resource resource, ClassLoader classLoader) {
    	try {
			return new UrlResource( resource.getURL() , classLoader );
		} catch (IOException e) {
			throw new RuntimeIOException();
		}
	}

	private void visit( ClassMetadata metadata, ScanContext scanContext) {
        for (MetadataVisitor<ClassMetadata> classVisitor : metadataVisitors) {
            classVisitor.visit(metadata, scanContext);
        }
    }

    /**
     * Resolve the specified base package into a pattern specification for the
     * package search path.
     * <p>
     * The default implementation resolves placeholders against system
     * properties, and converts a "."-based package path to a "/"-based resource
     * path.
     * 
     * @param basePackage
     *            the base package as specified by the user
     * @return the pattern specification to be used for package searching
     */
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
                .resolvePlaceholders(basePackage.trim()));
    }
    
    /**
     * Resolver for the proxy mode of a bean.
     * 
     * @param scopeProxyModeResolver
     */
    public void setScopeProxyModeResolver(
            ScopeProxyModeResolver scopeProxyModeResolver) {
        this.scopeProxyModeResolver = scopeProxyModeResolver;
    }

	private static class ScanContextImpl extends AbstractScanContext {
		private final Registry registry;

		private ScanContextImpl(Set<BeanDefinitionHolder> beans,
				BeanDefinitionDefaults beanDefinitionDefaults, Registry registry) {
			super(beans, beanDefinitionDefaults);
			this.registry = registry;
		}

		public String generateBeanName( BeanDefinition beanDefinition  ) {
		    String name = registry.generateBeanName( beanDefinition );  
		    return generateBeanName( name );
		}

		public String generateBeanName( String suggestion ) {
			String name = suggestion;
		    int i = 1;
		    while ( ! nameAvailable( name ) ) {
		        name = beanName( suggestion , "" + i++ );
		    }
		    return name;
		}

		private boolean nameAvailable( String name ) {
		    return (! registry.isNameTaken( name )) && isBeanNameAvailable( name );
		}

		private String beanName(String baseName, String suffix) {
		    if ( suffix == null || suffix.length() == 0 )
		        return baseName;
		    return baseName + BEAN_NAME_SEPARATOR + suffix;
		}

		public BeanDefinition getSpringBeanDefinition(String beanName)
				throws NoSuchBeanDefinitionException {
			return registry.getBeanDefinition(beanName); 
		}

		public String[] getSpringBeanDefinitionNames() {
			return registry.getBeanDefinitionNames();
		}

	}
	
	/**
     * Inner factory class used to just introduce an AOP framework dependency
     * when actually creating a scoped proxy.
     */
    private static final class ScopedProxyCreator {
        
        private ScopedProxyCreator() {}

        public static BeanDefinitionHolder createScopedProxy(
                BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {

            return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
        }
    }
    
    private static final class RegistryImpl implements Registry {
		private final BeanDefinitionRegistry registry;
		private final BeanNameGenerator beanNameGenerator;

		private RegistryImpl(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator ) {
			this.registry = registry;
			this.beanNameGenerator = beanNameGenerator;
		}

		public String generateBeanName(BeanDefinition beanDefinition) {
		    return beanNameGenerator.generateBeanName(beanDefinition, registry);
		}

		public boolean isNameTaken(String name) {
		    return registry.containsBeanDefinition( name );
		}

		public BeanDefinition getBeanDefinition(String name)
				throws NoSuchBeanDefinitionException {
			return registry.getBeanDefinition(name);
		}

		public String[] getBeanDefinitionNames() {
			return registry.getBeanDefinitionNames();
		}

		public void register(String beanName, BeanDefinition beanDefinition) {
			this.registry.registerBeanDefinition(beanName, beanDefinition);
		}
	}

    /**
     * Set defaults for bean definitions. {@link ScanContext#createDefaultBeanDefinition(Object, String )} 
     * will use the given default for creating a BeanDefintion.
     * 
     * @param beanDefinitionDefaults
     * 
     * @see BeanDefinitionDefaults
     * @see ScanContext#createDefaultBeanDefinition(Object, String)
     */
    public void setBeanDefinitionDefaults(
            BeanDefinitionDefaults beanDefinitionDefaults) {
        this.beanDefinitionDefaults = beanDefinitionDefaults;
    }

    /**
     * Set the bean name generator.
     * 
     * Note that the name generated by this name generator is subject to be changed by
     * the scanner. Visitors may indirectly call this {@link BeanNameGenerator#generateBeanName(BeanDefinition, BeanDefinitionRegistry) generateBeanName}
     * via the {@link ScanContext#generateBeanName(BeanDefinition)}.
     * 
     * <p> <b>Note:</b> The name returned by the given generator may be changed by spann. The given generator
     * has only access to beans which are already registered with the registry; This may result in duplicate
     * bean names between different beans created by spann, but not registered yet. For this reason, spann may
     * add a #X suffix to the generated name, 
     * (where X is a counter which is incremented until an unused name is found, both in the registry and in 
     * the spann context).
     * 
     * @param beanNameGenerator
     */
    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }
    
    /**
     * A Wrapper around spring's {@link BeanNameGenerator} and Spring's {@link BeanDefinitionRegistry}.
     * 
     * Used to isolate the scan from the registry.
     * @author Ron Piterman    
     */
    public static interface Registry {
        boolean isNameTaken( String name );
		String generateBeanName( BeanDefinition beanDefinition );
        String[] getBeanDefinitionNames();
        BeanDefinition getBeanDefinition( String name ) throws NoSuchBeanDefinitionException;
    }

    /**
     * Set the {@link ClassMetadataSource}. The default {@link ClassMetadataSource} is
     * create by {@link ClassPathScannerSupport}.
     * 
     * @param classMetadataSource
     * @see ClassPathScannerSupport
     */
	public void setClassMetadataSource(ClassMetadataSource classMetadataSource) {
		this.classMetadataSource = classMetadataSource;
	}

	/** 
	 * Retrieve the names of base packages under which resources will be scanned. 
	 * <p>
	 * Each package name will be converted to a resource path and used as prefix
	 * of the {@link #setResourcePattern(String) resourcePattern}
	 * 
	 * @return
	 */
	public List<String> getBasePackages() {
		return basePackages;
	}

	/**
	 * Set the names of the base packages under which resources will be scanned.
	 * <p>
	 * Each package name will be converted to a resource path and used as prefix
	 * of the {@link #setResourcePattern(String) resourcePattern}
	 * @param basePackages List of base package names.
	 */
	public void setBasePackages(List<String> basePackages) {
		this.basePackages = basePackages;
	}
	
	/**
	 * Convenience method for setting base packages as a comma separated list.
	 * White space is allowed between package names.
	 * 
	 * @param basePackagesNames
	 */
	public void setBasePackagesNames( String basePackagesNames ) {
		setBasePackages( Arrays.asList( basePackagesNames.split(",")));
	}

	/**
	 * Set the metadata visitors to be used with the classpath scan.
	 * <p>
	 * By default, the metadataVisitors list contains only the {@link DefaultVisitor}.
	 * <p>
	 * If users configure this property, the default visitor must be explicitly added or
	 * spring should be instructed to append the value to the existing list. Otherwise, 
	 * the default visitor will not be included.
	 *  
	 * @param metadataVisitors
	 */
	public void setMetadataVisitors(List<MetadataVisitor<ClassMetadata>> metadataVisitors) {
		this.metadataVisitors = metadataVisitors;
	}

	/**
	 * Retrieve the metadata visitors configured for the scan.
	 * <p>
	 * By default, this list contains only an instance of {@link DefaultVisitor}.
	 * 
	 * @return
	 */
	public List<MetadataVisitor<ClassMetadata>> getMetadataVisitors() {
		return metadataVisitors;
	}

}
