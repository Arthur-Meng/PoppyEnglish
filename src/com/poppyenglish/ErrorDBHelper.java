package com.poppyenglish;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ErrorDBHelper extends SQLiteOpenHelper{
	private static final String DB_NAME = "con1.db";

	private static final String TBL_NAME = "ErrorInfor";

	private SQLiteDatabase db;


	ErrorDBHelper(Context c) {
		super(c, DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		String CREATE_TBL = " create table " + "ErrorInfor(error int primary key,name text,errdate DATETIME) ";
		db.execSQL(CREATE_TBL);
	}


	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TBL_NAME, null, values);
		db.close();
	}

	public void update(ContentValues values,Integer error) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(TBL_NAME, values,"error=?", new String[] { String.valueOf(error) });
		db.close();
	}
	public Cursor query() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}


	public Cursor query(int error) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, "error=?", new String[] { String.valueOf(error) }, null, null, null);
		return c;
	}


	public void del(int error) {
		if (db == null)
			db = getWritableDatabase();
		db.delete(TBL_NAME, "error=?", new String[] { String.valueOf(error) });
	}


	public void close() {
		if (db != null)
			db.close();
	}

	public Boolean ifhave(int name) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				if (c.getInt(c.getColumnIndex("error"))==name) {
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
