package com.masetta.spann.quartzexample.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.SpannReference;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
// Configure a Bean Definition
@BeanConfig(create=SimpleTriggerBean.class,
		attached=@Attached(role="quartzTrigger",scope=Artifact.METHOD),explicit=true,
		references=@SpannReference(property="jobDetail",role="quartzJob", scope=Artifact.METHOD))
public @interface Interval {
	
	/**
	 * @see SimpleTriggerBean#setStartDelay(long)
	 */
	long startDelay() default 0;
	
	/**
	 * @see SimpleTriggerBean#set
	 */
	long repeatInterval() default 0;
	
	/**
	 * @see CronTriggerBean#setMisfireInstructionName(String)
	 */
	String misfireInstructionName() default "";
	
	/**
	 * @see CronTriggerBean#setTriggerListenerNames(String[])
	 */
	String[] triggerListenerNames() default {};

}
