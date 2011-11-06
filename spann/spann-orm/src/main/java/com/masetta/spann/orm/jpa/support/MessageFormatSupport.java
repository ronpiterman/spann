package com.masetta.spann.orm.jpa.support;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public final class MessageFormatSupport {
	
	private MessageFormatSupport() {}
	
	private static final Pattern USE_MESSAGE = Pattern.compile("\\{\\d+\\}");

	private static final Integer[] ZERO_TO_29 = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
			13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 };

	public static MessageFormat createMessageFormat(String xql) {
		if ( !USE_MESSAGE.matcher(xql).find() )
			return null;

		return new MessageFormat(xql);
	}
	
	/**
	 * Detect which arguments are used to consume them
	 * 
	 * @param messageFormat
	 * @return
	 */
	public static int[] getMessageArguments(MessageFormat messageFormat) {
		String formatted = messageFormat.format(ZERO_TO_29);
		try {
			Object[] parsed = messageFormat.parse(formatted);
			int length = countNotNull(parsed);

			// actually should never be
			if ( length == 0 )
				return null;

			int[] consumeMethodArguments = new int[length];
			int i = 0;
			for (Object string : parsed) {
				if ( string != null ) {
					consumeMethodArguments[i++] = Integer.parseInt((String) string);
				}
			}

			return consumeMethodArguments;

		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	private static int countNotNull(Object[] parsed) {
		int count = 0;
		for (Object o : parsed) {
			if ( o != null )
				count++;
		}
		return count;
	}

}
