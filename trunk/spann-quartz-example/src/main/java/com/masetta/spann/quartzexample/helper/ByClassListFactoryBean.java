package com.masetta.spann.quartzexample.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ByClassListFactoryBean<T> implements FactoryBean<List<T>> , ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private Class<T> type;
	
	public Class<?> getObjectType() {
		return List.class;
	}

	public void setType(Class<T> eType) {
		this.type = eType;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public List<T> getObject() throws Exception {
		return new ArrayList<T>(this.applicationContext.getBeansOfType( type ).values());
	}
	
	public boolean isSingleton() {
		return true;
	}

}
