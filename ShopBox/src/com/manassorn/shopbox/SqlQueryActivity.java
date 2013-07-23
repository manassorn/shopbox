package com.manassorn.shopbox;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SqlQueryActivity extends Activity implements OnClickListener {
	private final String TAG = "SqlQueryActivity";
	OpenHelper openHelper;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sql_query);

		Button submit = (Button) findViewById(R.id.submit_button);
		submit.setOnClickListener(this);

		openHelper = new OpenHelper(this);
		db = openHelper.getWritableDatabase();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (openHelper != null) {
			openHelper.close();
		}
	}

	@Override
	public void onClick(View v) {
		EditText sqlText = (EditText) findViewById(R.id.sql_text);
		String sql = sqlText.getText().toString();

		Cursor cursor = db.rawQuery(sql, null);

		String[] from = new String[] { "DateColumn" };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter resultAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, from, to, 0);

		ListView resultList = (ListView) findViewById(R.id.result_list);
		resultList.setAdapter(resultAdapter);
	}

	class OpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "ShopBoxTest";
		private static final int DATABASE_VERSION = 2;
		private Context context;
		private String[] testDates = new String[] { "2013-07-21 22:20:00", "2013-05-21 18:10:00",
				"2013-04-21 13:20:00", "2013-03-24 10:30:00", "2013-03-21 09:40:00",
				"2012-07-21 06:50:00", "2012-07-21 22:25:00", "2012-07-21 21:24:00",
				"2011-07-21 20:23:00", "2011-07-21 21:22:00", };

		public OpenHelper(Context context) {
			super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
			Log.d(TAG, "Database version: " + DATABASE_VERSION);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "onCreate");
			String createDateTable = "create table DateTable ( DateColumn text );";
			db.execSQL(createDateTable);

			for (String strDate : testDates) {
				ContentValues values = new ContentValues();
				values.put("DateColumn", strDate);
				db.insert("DateTable", null, values);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade");
			String createDateTable = "create table DateTable ( DateColumn text );";
			db.execSQL(createDateTable);

			for (String strDate : testDates) {
				ContentValues values = new ContentValues();
				values.put("DateColumn", strDate);
				db.insert("DateTable", null, values);
			}
		}
	}
}
