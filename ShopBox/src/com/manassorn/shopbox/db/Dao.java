package com.manassorn.shopbox.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.manassorn.shopbox.utils.SQLExceptionUtil;

public class Dao<T, ID> {
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
	private static final String TAG = "Dao";
	protected DbHelper dbHelper;
	protected Class<T> clazz;
	protected TableInfo<T, ID> tableInfo;
	protected QueryBuilder queryBuilder;

	public static <T, ID> Dao<T, ID> createDao(DbHelper dbHelper, Class<T> clazz) {
		return new Dao<T, ID>(dbHelper, clazz);
	}

	public Dao(DbHelper dbHelper, Class<T> clazz) {
		this.dbHelper = dbHelper;
		this.clazz = clazz;
		this.tableInfo = new TableInfo<T, ID>(clazz);
	}

	public Cursor queryForId(ID id) throws SQLException {
		if (tableInfo.getIdField() == null) {
			throw new SQLException("Not found @DatabaseField(id=true) in " + clazz);
		}
		return queryForEq(tableInfo.getIdField(), id);
	}

	public T getForId(ID id) throws SQLException {
		Cursor cursor = queryForId(id);
		if (cursor != null && cursor.moveToFirst()) {
			return mapRow(cursor);
		}
		return null;
	}

	protected Object getFieldValue(Field field, T data) throws IllegalArgumentException,
			IllegalAccessException {
		Object val = field.get(data);
		if (field.getType() == Date.class) {
			SimpleDateFormat fmt = new SimpleDateFormat(DATETIME_FORMAT);
			return fmt.format((Date) val);
		}
		return val;
	}

	protected void setField(Field field, Object instance, Cursor cursor, String columnName)
			throws IllegalArgumentException, IllegalAccessException {
		int columnIndex = cursor.getColumnIndex(columnName);
		Class<?> fieldType = field.getType();
		if (fieldType == int.class) {
			field.set(instance, cursor.getInt(columnIndex));
		} else if (fieldType == long.class) {
			field.set(instance, cursor.getLong(columnIndex));
		} else if (fieldType == double.class) {
			field.set(instance, cursor.getDouble(columnIndex));
		} else if (fieldType == String.class) {
			field.set(instance, cursor.getString(columnIndex));
		} else if (fieldType == Date.class) {
			SimpleDateFormat fmt = new SimpleDateFormat(DATETIME_FORMAT);
			Date date = null;
			try {
				date = fmt.parse(cursor.getString(columnIndex));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			field.set(instance, date);
		} else if (fieldType.getEnumConstants() != null) {
			String enumName = cursor.getString(columnIndex);
			Enum<?>[] constants = (Enum<?>[]) fieldType.getEnumConstants();
			for (Enum<?> enumVal : constants) {
				if (enumVal.name().equals(enumName)) {
					field.set(instance, enumVal);
				}
			}
		}
	}

	public Cursor queryForEq(String columnName, Object val) {
		Field field = tableInfo.getFieldTypeByColumnName(columnName);
		return queryForEq(field, val);
	}

	public Cursor queryForEq(Field field, Object val) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ").append(tableInfo.getTableName());
		sb.append(" where " + field.getName()).append("=?");
		return query(sb.toString(), new String[] { val.toString() });
	}

	public List<T> getForEq(String columnName, Object val) {
		return mapRows(queryForEq(columnName, val));
	}

	public T queryForFirst(/* PreparedQuery<T> preparedQuery */String sql) {
		return null;
	}

	public Cursor query(String sql, String[] args) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public List<T> get(String sql, String[] args) {
		return mapRows(query(sql, args));
	}

	protected List<T> mapRows(Cursor cursor) {
		if (cursor == null)
			return null;
		ArrayList<T> list = new ArrayList<T>(cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				T obj = mapRow(cursor);
				list.add(obj);
			} while (cursor.moveToNext());
		}
		return list;
	}

	protected T mapRow(Cursor cursor) {
		T instance = null;
		try {
			instance = tableInfo.getConstructor().newInstance();
		} catch (IllegalArgumentException e) {
			Log.e(TAG,
					String.format("Class %s does'nt has empty constructor", clazz.getSimpleName()),
					e);
		} catch (InstantiationException e) {
			Log.e(TAG, String.format("Constructor of %s isn't accessible", clazz.getSimpleName()),
					e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, String.format("Constructor of %s isn't accessible", clazz.getSimpleName()),
					e);
		} catch (InvocationTargetException e) {
			Log.e(TAG,
					String.format("Something has error when create instance of %s",
							clazz.getSimpleName()), e);
		}
		String[] columnNames = cursor.getColumnNames();
		for (String columnName : columnNames) {
			Field field = tableInfo.getFieldTypeByColumnName(columnName);
			try {
				setField(field, instance, cursor, columnName);
			} catch (IllegalArgumentException e) {
				Log.e(TAG,
						String.format("Unknown %s field in %s class", field.getName(),
								clazz.getSimpleName()), e);
			} catch (IllegalAccessException e) {
				Log.e(TAG, String.format("Field %s isn't accessible", field.getName()), e);
			}
		}
		return instance;
	}

	public Cursor queryForAll() {
		return query("select * from " + tableInfo.getTableName(), null);
	}

	public List<T> getForAll() {
		return mapRows(queryForAll());
	}

	public int insert(List<T> list) throws SQLException {
		int rows = 0;
		for (T data : list) {
			insert(data);
			rows++;
		}
		return rows;
	}

	public int insert(T data) throws SQLException {
		if (data == null) {
			return 0;
		}
		ContentValues values = new ContentValues();
		for (Field field : tableInfo.getFields()) {
			try {
				DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
				if (!databaseField.generatedId()) {
					values.put(field.getName(), getFieldValue(field, data).toString());
				}
			} catch (IllegalArgumentException e) {
				throw SQLExceptionUtil.create("Could not assign object '" + data + "' to field "
						+ field.getName(), e);
			} catch (IllegalAccessException e) {
				throw SQLExceptionUtil.create("Could not assign object '" + data + "' to field "
						+ field.getName(), e);
			}
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			long id = db.insert(tableInfo.getTableName(), null, values);
			if (id == -1) {
				throw SQLExceptionUtil.create("Could not insert table: " + tableInfo.getTableName()
						+ " values: " + values);
			}
			return (int) id;
		} finally {
			db.close();
		}
	}

	public QueryBuilder queryBuilder() {
		// if(queryBuilder == null) {
		// queryBuilder = new QueryBuilder();
		// }
		return new QueryBuilder();
	}

	public class QueryBuilder {
		private String cursorIdColumnName;
		private List<String> columnNames = new ArrayList<String>();
		private Where where;
		private String groupBy;
		private String orderBy;
		private String desc;

		public QueryBuilder selectCursorId(String columnName) {
			cursorIdColumnName = columnName;
			return this;
		}

		public QueryBuilder select(String[] columnNames) {
			this.columnNames.addAll(Arrays.asList(columnNames));
			return this;
		}

		public QueryBuilder select(String columnName) {
			columnNames.add(columnName);
			return this;
		}

		public Where where() {
			if (where == null) {
				where = new Where();
			}
			return where;
		}

		public QueryBuilder where(String sql, String[] args) {
			where().raw(sql, args);
			return this;
		}

		public QueryBuilder like(String columnName, String args) {
			where().like(columnName, args);
			return this;
		}

		public QueryBuilder groupBy(String columnName) {
			this.groupBy = columnName;
			return this;
		}

		public QueryBuilder orderBy(String columnName, String desc) {
			if (desc != null && desc.length() > 0 && !desc.equalsIgnoreCase("desc")
					&& !desc.equalsIgnoreCase("asc")) {
				throw new RuntimeException("orderBy desc isn't match 'desc' or 'asc'");
			}
			this.orderBy = columnName;
			this.desc = desc;
			return this;
		}

		public Cursor query() {
			String sql = build();
			return Dao.this.query(sql, args());
		}

		public List<T> get() {
			String sql = build();
			return Dao.this.get(sql, args());
		}

		public String[] args() {
			return where().args();
		}

		public String build() {
			StringBuilder sb = new StringBuilder();
			sb.append("select ");
			appendColumnNames(sb);
			sb.append(" from ").append(tableInfo.getTableName());
			if (where != null) {
				sb.append(" ").append(where);
			}
			if (groupBy != null && groupBy.length() > 0) {
				sb.append(" group by ").append(groupBy);
			}
			if (orderBy != null && orderBy.length() > 0) {
				sb.append(" order by ").append(orderBy);
				if (desc != null && desc.length() > 0) {
					sb.append(" ").append(desc);
				}
			}
			return sb.toString();
		}

		protected void appendColumnNames(StringBuilder sb) {
			StringBuilder columnNameSb = new StringBuilder();
			if (cursorIdColumnName != null) {
				columnNameSb.append(cursorIdColumnName).append(" as _id");
			}
			if (columnNames.size() == 0) {
				columnNames.add("*");
			}
			for (String columnName : columnNames) {
				if (columnNameSb.length() > 0) {
					columnNameSb.append(",");
				}
				columnNameSb.append(columnName);
			}
			sb.append(columnNameSb);
		}

		public class Where {
			StringBuilder sb = new StringBuilder();
			List<String> args = new ArrayList<String>();

			public Where raw(String sql, String[] args) {
				sb.append(sql);
				if(args != null) {
					this.args.addAll(Arrays.asList(args));
				}
				return this;
			}

			public Where like(String columnName, String arg) {
				sb.append(columnName).append(" like ?");
				this.args.add(arg);
				return this;
			}

			public String[] args() {
				return args.toArray(new String[args.size()]);
			}

			@Override
			public String toString() {
				if (sb.length() > 0) {
					sb.insert(0, "where ");
				}
				return sb.toString();
			}
		}
	}
}
