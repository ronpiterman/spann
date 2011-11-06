package com.masetta.spann.orm.jpa.support;

import com.masetta.spann.spring.util.Resolver;

public class SelfResolver implements Resolver {

	public Object resolve(Object param) {
		return param;
	}

}
