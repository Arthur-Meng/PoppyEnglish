package com.poppyenglish;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeActivity extends Activity implements Button.OnClickListener {
	private Button bt_challenge_num1;
	private Button bt_challenge_num2;
	private Button bt_challenge_num3;
	private Button bt_challenge_num4;
	private Button bt_challenge_num5;
	private Button bt_challenge_num6;
	private Button bt_challenge_num7;
	private Button bt_challenge_num8;
	private TextView grade1,grade2,grade3,grade4,grade5,grade6,grade7,grade8;
	public static SoundPool soundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色
		
		initView();
	}

	private void initView() {
		bt_challenge_num1 = (Button) findViewById(R.id.bt_challenge_num1);
		bt_challenge_num1.setOnClickListener(this);
		bt_challenge_num2 = (Button) findViewById(R.id.bt_challenge_num2);
		bt_challenge_num2.setOnClickListener(this);
		bt_challenge_num3 = (Button) findViewById(R.id.bt_challenge_num3);
		bt_challenge_num3.setOnClickListener(this);
		bt_challenge_num4 = (Button) findViewById(R.id.bt_challenge_num4);
		bt_challenge_num4.setOnClickListener(this);
		bt_challenge_num5 = (Button) findViewById(R.id.bt_challenge_num5);
		bt_challenge_num5.setOnClickListener(this);
		bt_challenge_num6 = (Button) findViewById(R.id.bt_challenge_num6);
		bt_challenge_num6.setOnClickListener(this);
		bt_challenge_num7 = (Button) findViewById(R.id.bt_challenge_num7);
		bt_challenge_num7.setOnClickListener(this);
		bt_challenge_num8 = (Button) findViewById(R.id.bt_challenge_num8);
		bt_challenge_num8.setOnClickListener(this);

		grade1 = (TextView) findViewById(R.id.grade1);
		grade2 = (TextView) findViewById(R.id.grade2);
		grade3 = (TextView) findViewById(R.id.grade3);
		grade4 = (TextView) findViewById(R.id.grade4);
		grade5 = (TextView) findViewById(R.id.grade5);
		grade6 = (TextView) findViewById(R.id.grade6);
		grade7 = (TextView) findViewById(R.id.grade7);
		grade8 = (TextView) findViewById(R.id.grade8);
		
		final DBHelper helper = new DBHelper(this);
		if (helper.query()!=null) {
			Cursor c = helper.query();
			String grade = null;
			if (c != null && c.moveToFirst()) {
				do {
					grade = c.getString(c.getColumnIndex("grade"));
					if (c.getString(c.getColumnIndex("name")).equals("1")){
						grade1.setText(grade);
						bt_challenge_num2.setVisibility(View.VISIBLE);
						grade2.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("2")){
						grade2.setText(grade);
						bt_challenge_num3.setVisibility(View.VISIBLE);
						grade3.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("3")){
						grade3.setText(grade);
						bt_challenge_num4.setVisibility(View.VISIBLE);
						grade4.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("4")){
						grade4.setText(grade);
						bt_challenge_num5.setVisibility(View.VISIBLE);
						grade5.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("5")){
						grade5.setText(grade);
						bt_challenge_num6.setVisibility(View.VISIBLE);
						grade6.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("6")){
						grade6.setText(grade);
						bt_challenge_num7.setVisibility(View.VISIBLE);
						grade7.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("7")){
						grade7.setText(grade);
						bt_challenge_num8.setVisibility(View.VISIBLE);
						grade8.setVisibility(View.VISIBLE);
					}
					if (c.getString(c.getColumnIndex("name")).equals("8")){
						grade8.setText(grade);
						grade8.setVisibility(View.VISIBLE);
					}
					
				} while (c.moveToNext());
			}

		}
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

	public void playSound() {
		soundPlayer.load(this, R.raw.ok, 1);
		soundPlayer.play(1, 1, 1, 0, 0, 1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		Intent toQuestion = new Intent(ChallengeActivity.this, QuestionActivity.class);
		switch (v.getId()) {
		case R.id.bt_challenge_num1:
			bundle.putCharSequence("questionnum", "1");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num2:
			bundle.putCharSequence("questionnum", "2");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num3:
			bundle.putCharSequence("questionnum", "3");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num4:
			bundle.putCharSequence("questionnum", "4");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num5:
			bundle.putCharSequence("questionnum", "5");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num6:
			bundle.putCharSequence("questionnum", "6");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num7:
			bundle.putCharSequence("questionnum", "7");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		case R.id.bt_challenge_num8:
			bundle.putCharSequence("questionnum", "8");
			toQuestion.putExtras(bundle);
			playSound();
			startActivity(toQuestion);
			break;
		default:
			break;
		}
	}
}
