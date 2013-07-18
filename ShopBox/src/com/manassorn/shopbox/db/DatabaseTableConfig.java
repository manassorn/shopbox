package com.manassorn.shopbox.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTableConfig<T> {
	protected Class<T> clazz;
	protected String tableName;
	protected FieldType[] fieldTypes;
	
	public DatabaseTableConfig(Class<T> clazz) {
		this.clazz = clazz;
		this.tableName = clazz.getName();
	}
	
	public void exractFieldTypes() {
		List<FieldType> fieldTypes = new ArrayList<FieldType>();
		for (Field field : clazz.getDeclaredFields()) {
			FieldType fieldType = FieldType.createFieldType(tableName, field, clazz);
			if (fieldType != null) {
				fieldTypes.add(fieldType);
			}
		}
		this.fieldTypes = fieldTypes.toArray(new FieldType[fieldTypes.size()]);
	}
	
	public String getTableName() {
		return tableName;
	}
}
