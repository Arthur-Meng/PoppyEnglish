package com.poppyenglish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "con.db";

	private static final String TBL_NAME = "GradeInfor";

	private SQLiteDatabase db;


	DBHelper(Context c) {
		super(c, DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		String CREATE_TBL = " create table " + "GradeInfor(name text primary key,grade text) ";
		db.execSQL(CREATE_TBL);
	}


	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME, null, values);
		db.close();
	}

	public void update(ContentValues values,String name) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(TBL_NAME, values,"name=?", new String[] { String.valueOf(name) });
		db.close();
	}
	public Cursor query() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}


	public Cursor query(String name) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, "name=?", new String[] { name }, null, null, null);
		return c;
	}


	public void del(String name) {
		if (db == null)
			db = getWritableDatabase();
		db.delete(TBL_NAME, "name=?", new String[] { String.valueOf(name) });
	}


	public void close() {
		if (db != null)
			db.close();
	}

	public Boolean ifhave(String name) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				if (c.getString(c.getColumnIndex("name")).equals(name)) {
					return true;
				}
			} while (c.moveToNext());

		}
		return false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
