package com.poppyenglish;

import java.io.IOException;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LotteryActivity extends Activity implements Button.OnClickListener {
	private TextView title;
	private TextView questiontime, question_answer;
	private TextView question_content;
	private Button makesurebutton, upbutton, nextbutton;
	private RadioButton question_ButtonA;
	private RadioButton question_ButtonB;
	private RadioButton question_ButtonC;
	private RadioButton question_ButtonD;
	String CONTENT, A, B, C, D, RESULT, date, choose;
	int error;
	private SQLiteDatabase db;
	DataBaseHelper myDbHelper;
	final ErrorDBHelper helper = new ErrorDBHelper(this);
	public Cursor c, qc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery);
		initview();
	}

	public void initview() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		title = (TextView) findViewById(R.id.questiontitle);
		questiontime = (TextView) findViewById(R.id.questiontime);
		question_answer = (TextView) findViewById(R.id.question_answer);
		question_content = (TextView) findViewById(R.id.question_content);
		makesurebutton = (Button) findViewById(R.id.xxbutton);
		makesurebutton.setOnClickListener(this);
		question_ButtonA = (RadioButton) findViewById(R.id.question_ButtonA);
		question_ButtonB = (RadioButton) findViewById(R.id.question_ButtonB);
		question_ButtonC = (RadioButton) findViewById(R.id.question_ButtonC);
		question_ButtonD = (RadioButton) findViewById(R.id.question_ButtonD);
		upbutton = (Button) findViewById(R.id.upbutton);
		nextbutton = (Button) findViewById(R.id.nextbutton);
		upbutton.setOnClickListener(this);
		nextbutton.setOnClickListener(this);

		myDbHelper = new DataBaseHelper(getApplicationContext());
		try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db = myDbHelper.openDataBase();
		qc = db.query("ChoiceTable", null, null, null, null, null, null);
		if (qc != null) {
			qc.moveToFirst();
		}

		try {
			if (helper.query() != null) {
				c = helper.query();
				if (c != null && c.moveToFirst()) {
					showall();
				} else {
					setContentView(R.layout.activity_lottery_null);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showall() {
		// 显示错题
		error = c.getInt(c.getColumnIndex("error"));
		choose = c.getString(c.getColumnIndex("name"));
		question_ButtonA.setEnabled(false);
		question_ButtonB.setEnabled(false);
		question_ButtonC.setEnabled(false);
		question_ButtonD.setEnabled(false);
		if (choose.equals("A")) {
			question_ButtonA.setChecked(true);
			question_ButtonB.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonD.setChecked(false);
		} else if (choose.equals("B")) {
			question_ButtonB.setChecked(true);
			question_ButtonA.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonD.setChecked(false);
		} else if (choose.equals("C")) {
			question_ButtonC.setChecked(true);
			question_ButtonB.setChecked(false);
			question_ButtonA.setChecked(false);
			question_ButtonD.setChecked(false);
		} else if (choose.equals("D")) {
			question_ButtonD.setChecked(true);
			question_ButtonB.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonA.setChecked(false);
		}
		date = c.getString(c.getColumnIndex("errdate"));
		questiontime.setText("错误日期：" + date);
		// 显示错题信息
		qc.moveToFirst();
		for (int i = 1; i < error; i++) {
			qc.moveToNext();
		}
		CONTENT = qc.getString(qc.getColumnIndex("CONTENT"));
		A = qc.getString(qc.getColumnIndex("A"));
		B = qc.getString(qc.getColumnIndex("B"));
		C = qc.getString(qc.getColumnIndex("C"));
		D = qc.getString(qc.getColumnIndex("D"));
		RESULT = qc.getString(qc.getColumnIndex("RESULT"));

		question_content.setText(String.valueOf((int) (error / 10) + 1) + "." + CONTENT);
		question_ButtonA.setText(A);
		question_ButtonB.setText(B);
		question_ButtonC.setText(C);
		question_ButtonD.setText(D);
		question_answer.setText("正确答案：" + RESULT);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xxbutton:
			helper.del(error);
			initview();
			break;
		case R.id.upbutton:
			try {
				c.moveToPrevious();
				showall();
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(LotteryActivity.this, "这已经是第一个了", 0).show();
			}
			break;
		case R.id.nextbutton:
			try {
				c.moveToNext();
				showall();
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(LotteryActivity.this, "已经没有下一个了", 0).show();
			}
			break;
		default:
			break;
		}
	}

}
