package com.masetta.spann.quartzexample.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.scheduling.quartz.CronTriggerBean;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.SpannReference;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
// Configure a Bean Definition
@BeanConfig(create=CronTriggerBean.class,
		attached=@Attached(role="quartzTrigger",scope=Artifact.METHOD),explicit=true,
		references=@SpannReference(property="jobDetail",role="quartzJob", scope=Artifact.METHOD))
public @interface Cron {
	
	/**
	 * @see CronTrigger#getCronExpression()
	 */
	String cronExpression();
	
	/**
	 * @see CronTrigger#setTimeZone(java.util.TimeZone)
	 */
	String timeZone() default "";
	
	/**
	 * @see CronTriggerBean#setMisfireInstructionName(String)
	 */
	String misfireInstructionName() default "";
	
	/**
	 * @see CronTriggerBean#setTriggerListenerNames(String[])
	 */
	String[] triggerListenerNames() default {};
	
	
}
