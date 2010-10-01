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

import java.io.Serializable;

import com.masetta.spann.spring.base.Implement;

@Implement("annotatedBean")
public abstract class AnnotatedBean {
	
	@Ann(integer=5,bool=true,someOtherProp="ignored",string="foo",type=Serializable.class)
	abstract void method();
	
	@Ann(integer=6,bool=false,someOtherProp="ignored2",string="bar",type=Comparable.class, integers={1,2,3})
	abstract void otherMehtod();

}
