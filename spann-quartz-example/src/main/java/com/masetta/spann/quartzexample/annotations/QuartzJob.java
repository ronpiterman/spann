package com.masetta.spann.quartzexample.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.WireMeta;
import com.masetta.spann.spring.base.beanconfig.factories.BeanReferenceFactory;
import com.masetta.spann.spring.base.beanconfig.factories.MetadataNameFactory;


/**
 * 
 * @author Ron Piterman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
// Configure a Bean Definition
@BeanConfig(create=MethodInvokingJobDetailFactoryBean.class,
		attached=@Attached(role="quartzJob",scope=Artifact.METHOD),explicit=true,
		wire={
			@WireMeta(property="targetObject",scope=Artifact.CLASS,factory=BeanReferenceFactory.class),
			@WireMeta(property="targetMethod",scope=Artifact.METHOD,factory=MetadataNameFactory.class )
		})
public @interface QuartzJob {
	
	/**
	 * Name of the job
	 * @see MethodInvokingJobDetailFactoryBean#setName(String)
	 */
	String name();
	
	/**
	 * Group of the job
	 * @see MethodInvokingJobDetailFactoryBean#setGroup(String)
	 */
	String group();
	
	/** 
	 * see {@link MethodInvokingJobDetailFactoryBean#setConcurrent(boolean)}.
	 */
	boolean concurrent() default true;

}
