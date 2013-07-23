package com.manassorn.shopbox.utils;

import java.sql.SQLException;

public class SQLExceptionUtil {

	private SQLExceptionUtil() {
	}
	
	public static SQLException create(String message, Throwable cause) {
		SQLException sqlException = new SQLException(message);
		sqlException.initCause(cause);
		return sqlException;
	}
}
