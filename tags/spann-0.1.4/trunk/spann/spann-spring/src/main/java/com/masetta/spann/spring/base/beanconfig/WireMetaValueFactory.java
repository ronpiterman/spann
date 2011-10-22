package com.masetta.spann.spring.base.beanconfig;

import com.masetta.spann.metadata.core.AnnotationPath;
import com.masetta.spann.metadata.core.Metadata;
import com.masetta.spann.spring.ScanContext;

public interface WireMetaValueFactory {

	Object create( Metadata metadata, ScanContext context, AnnotationPath path );

}
