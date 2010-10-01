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

package com.masetta.spann.orm.hibernate.chain;

import java.util.Collection;

import com.masetta.spann.orm.hibernate.support.FilterActivation;
import com.masetta.spann.orm.jpa.beans.QueryCallContext;
import com.masetta.spann.orm.jpa.beans.entitymanager.EntityManagerSupport;

public class StaticFilterEnabler extends AbstractFilterEnabler {

	private Collection<FilterActivation> filters;

	protected StaticFilterEnabler(EntityManagerSupport entityManagerSupport,
			Collection<FilterActivation> fa ) {
		super(entityManagerSupport);
		this.filters = fa;
	}

	@Override
	protected Collection<FilterActivation> getFilterActivations(QueryCallContext param) {
		return filters;
	}

}
