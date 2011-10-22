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

package com.masetta.spann.spring.integration.beanconfig.wire;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.spring.base.beanconfig.Attached;
import com.masetta.spann.spring.base.beanconfig.BeanConfig;
import com.masetta.spann.spring.base.beanconfig.WireMeta;
import com.masetta.spann.spring.base.beanconfig.factories.BeanNameFactory;
import com.masetta.spann.spring.base.beanconfig.factories.MetadataNameFactory;

@Retention(RetentionPolicy.RUNTIME)
@BeanConfig(create=WiredBean.class,attached=@Attached(role="some-role",scope=Artifact.UNKNOWN),
		wire={@WireMeta(property="beanName",factory=BeanNameFactory.class,scope=Artifact.CLASS),
		@WireMeta(property="beanClassName",factory=MetadataNameFactory.class,scope=Artifact.CLASS),
		@WireMeta(property="metadataName",factory=MetadataNameFactory.class,scope=Artifact.UNKNOWN)})
public @interface Ann {
}
