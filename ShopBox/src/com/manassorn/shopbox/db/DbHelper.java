package com.manassorn.shopbox.db;

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

import com.manassorn.shopbox.value.Category;
import com.manassorn.shopbox.value.Product;
import com.manassorn.shopbox.value.Supplement;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ShopBox";
	private static final String SQL_DIR = "sql";
	private static final int DATABASE_VERSION = 2;
	private static DbHelper instance;
	private Context context;
	private Dao<Product, Integer> productDao;
	private Dao<Category, Integer> categoryDao;
	private Dao<Supplement, Integer> supplementDao;

	// private static final String CATEGORY_TABLE_NAME = "category";
	// private static final String PRODUCT_TABLE_NAME = "product";
	// private static final String SUPPLEMENT_TABLE_NAME = "supplement";
	// private static final String BILL_TABLE_NAME = "Bill";
	// private static final String BILL_PRODUCT_ITEM_NAME = "BillProductItem";
	// private static final String Bill_SUPPLEMENT_ITEM_NAME =
	// "BillSupplementItem";
	public static class ColumnName {
		public static final String ID = "Id";
		public static final String PARENT_ID = "ParentId";
		public static final String NAME = "Name";
		public static final String AMOUNT = "Amount";
		public static final String PRICE = "Price";
		public static final String CATEGORY_ID = "CategoryId";
		public static final String BARCODE = "Barcode";
		public static final String TYPE = "TYPE";
		public static final String PERCENT = "PERCENT";
		public static final String CONSTANT = "Constant";
		public static final String PRIORITY = "Priority";
		public static final String CREATED_TIME = "CreatedTime";
		public static final String RECEIVE_MONEY = "ReceiveMoney";
		public static final String BILL_ID = "BillId";
		public static final String SEQUENCE = "Sequence";
		public static final String PRODUCT_ID = "ProductId";
		public static final String PRODUCT_NAME = "ProductName";
		public static final String PRODUCT_PRICE = "ProductPrice";
		public static final String TOTAL = "Total";
		public static final String SUPPLEMENT_ID = "SupplementId";
		public static final String SUPPLEMENT_NAME = "SupplementName";
		public static final String SUPPLEMENT_PERCENT = "SupplementPercent";
		public static final String SUPPLEMENT_CONSTANT = "SupplementConstant";
		public static final String SUBTOTAL = "SubTotal";
	}
	
	public static final int ROOT_CATEGORY_ID = 0;

	public static DbHelper getHelper(Context context) {
		if (instance == null) {
			instance = new DbHelper(context);
		}
		return instance;
	}

	public DbHelper(Context context) {
		super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public Dao<Product, Integer> getProductDao() {
		if (productDao == null) {
			productDao = new Dao<Product, Integer>(this, Product.class);
		}
		return productDao;
	}

	public Dao<Category, Integer> getCategoryDao() {
		if (categoryDao == null) {
			categoryDao = new Dao<Category, Integer>(this, Category.class);
		}
		return categoryDao;
	}

	public Dao<Supplement, Integer> getSupplementDao() {
		if (supplementDao == null) {
			supplementDao = new Dao<Supplement, Integer>(this, Supplement.class);
		}
		return supplementDao;
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
