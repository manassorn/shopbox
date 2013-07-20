package com.manassorn.shopbox.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Manassorn
 *
 * @param <T> 
 * @param <ID>
 */
public class TableInfo<T, ID> {
	static final String TAG = "TableInfo";
	protected Class<T> clazz;
	protected String tableName;
	protected Field[] fields;
	protected Field idField;
	protected Constructor<T> constructor;
	protected Map<String, Field> fieldNameMap;

	public TableInfo (Class<T> clazz) {
		this.clazz = clazz;
		this.tableName = clazz.getSimpleName();
		extractFields();
		constructor = findNoArgConstructor(clazz);
	}
	
	protected void extractFields() {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
			if(databaseField != null) {
				fields.add(field);
				if(databaseField.id()) {
					this.idField = field;
				}
			}
		}
		this.fields = fields.toArray(new Field[fields.size()]);
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdFields(Field idField) {
		this.idField = idField;
	}
	
	public Constructor<T> getConstructor() {
		return constructor;
	}
	
	public Field getFieldTypeByColumnName(String columnName) {
		columnName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
		if (fieldNameMap == null) {
			// build our alias map if we need it
			Map<String, Field> map = new HashMap<String, Field>();
			for (Field field : fields) {
				map.put(field.getName(), field);
			}
			fieldNameMap = map;
		}
		Field field = fieldNameMap.get(columnName);
		// if column name is found, return it
		if (field != null) {
			field.setAccessible(true);
			return field;
		}
		throw new IllegalArgumentException("Unknown column name '" + columnName + "' in table " + tableName);
	}

	public static <T> Constructor<T> findNoArgConstructor(Class<T> dataClass) {
		Constructor<T>[] constructors;
		try {
			@SuppressWarnings("unchecked")
			Constructor<T>[] consts = (Constructor<T>[]) dataClass.getDeclaredConstructors();
			// i do this [grossness] to be able to move the Suppress inside the method
			constructors = consts;
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't lookup declared constructors for " + dataClass, e);
		}
		for (Constructor<T> con : constructors) {
			if (con.getParameterTypes().length == 0) {
				if (!con.isAccessible()) {
					try {
						con.setAccessible(true);
					} catch (SecurityException e) {
						throw new IllegalArgumentException("Could not open access to constructor for " + dataClass);
					}
				}
				return con;
			}
		}
		if (dataClass.getEnclosingClass() == null) {
			throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass);
		} else {
			throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass
					+ ".  Missing static on inner class?");
		}
	}
	
}
