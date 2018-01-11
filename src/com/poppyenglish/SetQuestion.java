package com.poppyenglish;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SetQuestion {
	private SQLiteDatabase db;
	DataBaseHelper2 myDbHelper2;
	Cursor c;
	String CONTENT = "", A = "", B = "", C = "", D = "", RESULT = "";

	public SetQuestion(Context applicationContext) {
		// TODO Auto-generated constructor stub
		myDbHelper2 = new DataBaseHelper2(applicationContext);
		try {
			myDbHelper2.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPkQuestionByNum(int x) {
		db = myDbHelper2.openDataBase();
		c = db.query("ChoiceTable", null, null, null, null, null, null);
		c.moveToFirst();
		for (int i = 0; i < x; i++) {
			c.moveToNext();
		}
		CONTENT = c.getString(c.getColumnIndex("CONTENT"));
		A = c.getString(c.getColumnIndex("A"));
		B = c.getString(c.getColumnIndex("B"));
		C = c.getString(c.getColumnIndex("C"));
		D = c.getString(c.getColumnIndex("D"));
		RESULT = c.getString(c.getColumnIndex("RESULT"));
	}
}
