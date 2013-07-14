package com.manassorn.shopbox.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ShopBox";
	private static final String SQL_DIR = "sql";
	private static final int DATABASE_VERSION = 2;
	private Context context;

	// private static final String CATEGORY_TABLE_NAME = "category";
	// private static final String PRODUCT_TABLE_NAME = "product";
	// private static final String SUPPLEMENT_TABLE_NAME = "supplement";
	// private static final String BILL_TABLE_NAME = "Bill";
	// private static final String BILL_PRODUCT_ITEM_NAME = "BillProductItem";
	// private static final String Bill_SUPPLEMENT_ITEM_NAME =
	// "BillSupplementItem";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		upgrade(db, 0, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		upgrade(db, oldVersion, newVersion);
	}

	protected void upgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
		try {
			for (int v = fromVersion; v < toVersion; v++) {
				execSqlFile(String.format("v%d.sql", v + 1), db);
			}
		} catch (IOException e) {
			throw new RuntimeException("Database upgrade failed", e);
		}
	}

	protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
		for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile,
				context.getAssets())) {
			db.execSQL(sqlInstruction);
		}
	}

	static class SqlParser {

		public static List<String> parseSqlFile(String sqlFile, AssetManager assetManager)
				throws IOException {
			List<String> sqlIns = null;
			InputStream is = assetManager.open(sqlFile);
			try {
				sqlIns = parseSqlFile(is);
			} finally {
				is.close();
			}
			return sqlIns;
		}

		public static List<String> parseSqlFile(InputStream is) throws IOException {
			String script = removeComments(is);
			return splitSqlScript(script, ';');
		}

		private static String removeComments(InputStream is) throws IOException {

			StringBuilder sql = new StringBuilder();

			InputStreamReader isReader = new InputStreamReader(is);
			try {
				BufferedReader buffReader = new BufferedReader(isReader);
				try {
					String line;
					String multiLineComment = null;
					while ((line = buffReader.readLine()) != null) {
						line = line.trim();

						if (multiLineComment == null) {
							if (line.startsWith("/*")) {
								if (!line.endsWith("}")) {
									multiLineComment = "/*";
								}
							} else if (line.startsWith("{")) {
								if (!line.endsWith("}")) {
									multiLineComment = "{";
								}
							} else if (!line.startsWith("--") && !line.equals("")) {
								sql.append(line);
							}
						} else if (multiLineComment.equals("/*")) {
							if (line.endsWith("*/")) {
								multiLineComment = null;
							}
						} else if (multiLineComment.equals("{")) {
							if (line.endsWith("}")) {
								multiLineComment = null;
							}
						}

					}
				} finally {
					buffReader.close();
				}

			} finally {
				isReader.close();
			}

			return sql.toString();
		}

		private static List<String> splitSqlScript(String script, char delim) {
			List<String> statements = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			boolean inLiteral = false;
			char[] content = script.toCharArray();
			for (int i = 0; i < script.length(); i++) {
				if (content[i] == '\'') {
					inLiteral = !inLiteral;
				}
				if (content[i] == delim && !inLiteral) {
					if (sb.length() > 0) {
						statements.add(sb.toString().trim());
						sb = new StringBuilder();
					}
				} else {
					sb.append(content[i]);
				}
			}
			if (sb.length() > 0) {
				statements.add(sb.toString().trim());
			}
			return statements;
		}

	}
}
