package com.masetta.spann.spring.base.beanconfig.factories;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.WireMetaValueFactory;
import com.masetta.spann.spring.core.visitor.VisitorSupport;
import com.masetta.spann.spring.exceptions.IllegalConfigurationException;

public class BeanNameFactory implements WireMetaValueFactory {

	public Object create(Metadata metadata, ScanContext context, AnnotationPath path) {
		if ( metadata instanceof ClassMetadata )
			return VisitorSupport.findBeanName(context,  (ClassMetadata) metadata );
		else
			throw new IllegalConfigurationException( BeanConfig.class.getCanonicalName(), metadata, "expected ClassMetadata, but was " + metadata );
		
	}

}
