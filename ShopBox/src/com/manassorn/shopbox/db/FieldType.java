package com.manassorn.shopbox.db;

import java.lang.reflect.Field;

public class FieldType {
	private final String tableName;
	private final Field field;
	private final String columnName;

	public FieldType(String tableName, Field field, DatabaseFieldConfig fieldConfig,
			Class<?> parentClass) {
		this.tableName = tableName;
		this.field = field;
		if (fieldConfig.getColumnName() == null) {
			this.columnName = field.getName();
		} else {
			this.columnName = fieldConfig.getColumnName();
		}
	}

	public static FieldType createFieldType(String tableName, Field field, Class<?> parentClass) {
		DatabaseFieldConfig fieldConfig = DatabaseFieldConfig.fromField(tableName, field);
		if (fieldConfig == null) {
			return null;
		} else {
			return new FieldType(tableName, field, fieldConfig, parentClass);
		}
	}
}
