package com.masetta.spann.orm.jpa.beans.factories;

import java.text.MessageFormat;

public class NativeDynamicQueryFactory extends NativeQueryFactory {

	@Override
	public String getQuery(Object[] param) {
		return new MessageFormat( super.getQuery( param ) ).format( param );
	}
	
	

}
