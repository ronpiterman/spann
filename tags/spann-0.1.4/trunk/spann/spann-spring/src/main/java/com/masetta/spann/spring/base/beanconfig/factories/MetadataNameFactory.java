package com.masetta.spann.spring.base.beanconfig.factories;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.ScanContext;
import com.masetta.spann.spring.base.beanconfig.WireMetaValueFactory;

public class MetadataNameFactory implements WireMetaValueFactory {

	public Object create(Metadata metadata, ScanContext context, AnnotationPath path) {
		return metadata.getName();
	}

}
