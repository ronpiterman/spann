package com.masetta.spann.spring.base.beanconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.masetta.spann.metadata.common.Artifact;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface WireMeta {
	
	String PROPERTY = "property";
	String FACTORY = "factory";
	String SCOPE = "scope";

	Class<? extends WireMetaValueFactory> factory();
	
	String property();
	
	Artifact scope() default Artifact.UNDEFINED;

}
