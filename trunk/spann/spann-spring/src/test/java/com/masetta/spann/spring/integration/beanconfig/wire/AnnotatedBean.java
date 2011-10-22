package com.masetta.spann.spring.integration.beanconfig.wire;

import org.springframework.stereotype.Component;

import com.masetta.spann.spring.core.annotations.VisitMethods;

@Component("wireTestBean")
@Ann
@VisitMethods
public class AnnotatedBean {
	
	@Ann
	public String getFoo() {
		return "foo";
	}
	
	@Ann
	public String getBar() {
		return "bar";
	}

}
