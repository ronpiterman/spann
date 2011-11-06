package com.masetta.spann.orm.jpa.beans.factories;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class NativeQueryFactory extends AbstractQueryCallContextFactory {
	
	public static final String RESULT_SET_MAPPING_PROPERTY = "resultSetMapping";
	
	public static final String RESULT_SET_CLASS_PROPERTY = "resultSetClass";
	
	private String resultSetMapping;
	
	private Class<?> resultSetClass;

	@Override
	protected Query createQuery(EntityManager em, String query, Object[] param) {
		if ( resultSetMapping != null ) {
			return em.createNativeQuery( query , resultSetMapping );
		}
		if ( resultSetClass != null ) {
			return em.createNativeQuery( query, resultSetClass );
		}
		return em.createNativeQuery( query );
	}

	public void setResultSetMapping(String resultSetMapping) {
		this.resultSetMapping = resultSetMapping;
	}

	public void setResultSetClass(Class<?> resultSetClass) {
		this.resultSetClass = resultSetClass;
	}

}
