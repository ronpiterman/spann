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

package com.masetta.spann.spring.integration.replace;

import org.springframework.stereotype.Component;

import com.masetta.spann.spring.base.method.Replace;
import com.masetta.spann.spring.core.annotations.VisitMethods;

@Component
@VisitMethods
public class BeanClass implements ReplacedMethods {
	
	public String notReplacedImplemented(String string, int i, float f) {
		return string;
	}

	public String notReplacedNotImplemented(String string, int i, float f) {
		throw new AbstractMethodError();
	}

	@Replace(methodReplacer=MethodReplacerImpl.class)
	public String replacedByClass(String string, int i, float f) {
		return "#";
	}

	@Replace( methodReplacerBean="methodReplacerBean")
	public String replacedBySpringBean(String string, int i, float f) {
		return "!";
	}

	@Replace( methodReplacerBean="methodReplacerBean")
	public String overloaded(String string , Long l ) {
		return "string";
	}

	@Replace( methodReplacerBean="methodReplacerBean2")
	public String overloaded(String string , Integer i) {
		return "long";
	}

}
