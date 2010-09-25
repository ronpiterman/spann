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

package com.masetta.spann.spring.integration.beanconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.SpannReference;
import com.masetta.spann.spring.base.beanconfig.impl.Ignore;

@Retention(RetentionPolicy.RUNTIME)
@BeanConfig(attached=@Attached(role="bean-config-test",scope=Artifact.UNKNOWN),create=ConfBean.class,
		explicit=true,references={@SpannReference(scope=Artifact.CLASS, role="main",property="parent")})
public @interface Ann {
	
	int integer() default 0;
	
	String string() default "";
	
	Class<?> type() default Void.class;
	
	boolean bool() default false;
	
	@Ignore String someOtherProp() default "other"; 
	
	int[] integers() default {};

}
