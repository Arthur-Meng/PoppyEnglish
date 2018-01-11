package com.poppyenglish;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class QuestionActivity extends Activity implements Button.OnClickListener {
	private TextView title;
	private TextView questiontime;
	private TextView question_content;
	private Button makesurebutton;
	private RadioButton question_ButtonA;
	private RadioButton question_ButtonB;
	private RadioButton question_ButtonC;
	private RadioButton question_ButtonD;
	private Boolean ifstop = false;
	public static SoundPool soundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
	private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
	private SQLiteDatabase db;
	int ID, score = 0, time = 300;
	String CONTENT = "", A = "", B = "", C = "", D = "", RESULT = "";
	private String result;
	DataBaseHelper myDbHelper;
	SharedPreferences preferences;
	Cursor c;
	int num = 1;
	int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		myDbHelper = new DataBaseHelper(getApplicationContext());
		try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db = myDbHelper.openDataBase();
		c = db.query("ChoiceTable", null, null, null, null, null, null);

		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		title = (TextView) findViewById(R.id.questiontitle);
		questiontime = (TextView) findViewById(R.id.questiontime);
		question_content = (TextView) findViewById(R.id.question_content);
		makesurebutton = (Button) findViewById(R.id.makesurebutton);
		question_ButtonA = (RadioButton) findViewById(R.id.question_ButtonA);
		question_ButtonB = (RadioButton) findViewById(R.id.question_ButtonB);
		question_ButtonC = (RadioButton) findViewById(R.id.question_ButtonC);
		question_ButtonD = (RadioButton) findViewById(R.id.question_ButtonD);
		question_ButtonA.setOnClickListener(this);
		question_ButtonB.setOnClickListener(this);
		question_ButtonC.setOnClickListener(this);
		question_ButtonD.setOnClickListener(this);

		title.setText("关卡" + bundle.getString("questionnum"));

		Message message = handler.obtainMessage(1); // Message
		message.what = 1;
		handler.sendMessageDelayed(message, 1000);

		playSound();
		if (c != null) {
			c.moveToFirst();
			for (int i = 1; i < Integer.parseInt(bundle.getString("questionnum")); i++) {
				for (int n = 0; n < 10; n++)
					c.moveToNext();
			}
			showquestion();
			makesurebutton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (num < 10) {
						if (c.getString(c.getColumnIndex("RESULT")).equals(result))
							score += 10;
						else {
							ErrorDBHelper errordb = new ErrorDBHelper(getApplicationContext());
							ContentValues values = new ContentValues();
							values.put("name", result);
							// name 是选出的选项，error是题号
							int qnum = Integer.parseInt(bundle.getString("questionnum")) * 10 + num - 10;
							values.put("error", qnum);
							long time = System.currentTimeMillis();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date d1 = new Date(time);
							String t1 = format.format(d1);
							values.put("errdate", t1);
							if (errordb.ifhave(qnum)) {
								errordb.update(values, qnum);
							} else {
								errordb.insert(values);
							}
						}
						question_ButtonA.setChecked(false);
						question_ButtonB.setChecked(false);
						question_ButtonC.setChecked(false);
						question_ButtonD.setChecked(false);
						result = "";
						c.moveToNext();
						showquestion();
						num++;
						soundPlayer.play(soundID.get(1), 1, 1, 0, 0, 1);

					} else {
						if (c.getString(c.getColumnIndex("RESULT")).equals(result)) {
							score += 10;
						}
						handler.removeMessages(1);

						Toast toast = Toast.makeText(QuestionActivity.this, "", Toast.LENGTH_LONG);
						if (score >= 80) {
							soundPlayer.play(soundID.get(2), 1, 1, 0, 0, 1);
							showLastToast(toast, 3000);
							// 还是没搞清楚为什么这里是3000
							toast.setGravity(Gravity.CENTER, 0, 0);
							DBHelper helper = new DBHelper(getApplicationContext());
							ContentValues values = new ContentValues();
							values.put("name", bundle.getString("questionnum"));
							values.put("grade", Integer.toString(score));

							if (helper.ifhave(bundle.getString("questionnum"))) {
								System.out.println("QuestionGrade-Uptade" + bundle.getString("questionnum"));
								helper.update(values, bundle.getString("questionnum"));
							} else {
								System.out.println("Insert" + bundle.getString("questionnum"));
								helper.insert(values);
							}
							// 记录下来
							preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							level = Integer.parseInt(preferences.getString("honor", "等级"));
							level++;
							editor.remove("honor");
							editor.putString("honor", String.valueOf(level));
							editor.commit();
							HonorChange honorChange = new HonorChange();
							if (honorChange.change(preferences.getString("tel", "手机号"), "plus", "1")) {
							}

							SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									Intent toChallenge = new Intent(QuestionActivity.this, ChallengeActivity.class);
									startActivity(toChallenge);
								}
							};
							new SweetAlertDialog(QuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
									.setTitleText("恭喜").setContentText("您的等级达到" + level + "级")
									.setConfirmClickListener(listener).show();

						} else {
							soundPlayer.play(soundID.get(3), 1, 1, 0, 0, 1);
							showLastToast(toast, 2000);
							toast.setGravity(Gravity.CENTER, 0, 0);
						}

						final Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Intent toChallenge = new Intent(QuestionActivity.this, ChallengeActivity.class);
								startActivity(toChallenge);
							}
						}, 4000);
					}
				}
			});
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			handler.removeMessages(1);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showquestion() {
		CONTENT = c.getString(c.getColumnIndex("CONTENT"));
		A = c.getString(c.getColumnIndex("A"));
		B = c.getString(c.getColumnIndex("B"));
		C = c.getString(c.getColumnIndex("C"));
		D = c.getString(c.getColumnIndex("D"));
		RESULT = c.getString(c.getColumnIndex("RESULT"));
		question_content.setText(CONTENT);
		question_ButtonA.setText(A);
		question_ButtonB.setText(B);
		question_ButtonC.setText(C);
		question_ButtonD.setText(D);

	}

	public void playSound() {
		soundID.put(1, soundPlayer.load(this, R.raw.ok, 1));
		soundID.put(2, soundPlayer.load(this, R.raw.succeed, 1));
		soundID.put(3, soundPlayer.load(this, R.raw.fail, 1));
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
		case R.id.question_ButtonA:
			question_ButtonB.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonD.setChecked(false);
			result = "A";
			break;
		case R.id.question_ButtonB:
			question_ButtonA.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonD.setChecked(false);
			result = "B";
			break;
		case R.id.question_ButtonC:
			question_ButtonB.setChecked(false);
			question_ButtonA.setChecked(false);
			question_ButtonD.setChecked(false);
			result = "C";
			break;
		case R.id.question_ButtonD:
			question_ButtonB.setChecked(false);
			question_ButtonC.setChecked(false);
			question_ButtonA.setChecked(false);
			result = "D";
			break;

		default:
			break;
		}
	}

	/*
	 * public class TimeThread extends Thread{
	 * 
	 * @Override public void run() { super.run(); do{ try { Thread.sleep(1000);
	 * Message message = new Message(); message.what = 1;
	 * handler.sendMessage(message); } catch (InterruptedException e) {
	 * e.printStackTrace(); } }while (true); } }
	 */
	public void showMyToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TextView tv = new TextView(getBaseContext());
				tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				tv.setGravity(Gravity.CENTER_VERTICAL);
				tv.setTypeface(Typeface.SERIF);
				tv.setTextColor(Color.parseColor("#009ACD"));
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
				tv.setText(Integer.toString(time % 60));
				toast.setView(tv);
				toast.show();
			}
		}, 0, 3000);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt);
	}

	public void showLastToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				LinearLayout layout = (LinearLayout) toast.getView();
				if (score > 60)
					layout.setBackgroundResource(R.drawable.goodresult);
				else
					layout.setBackgroundResource(R.drawable.badresult);
				layout.setOrientation(LinearLayout.HORIZONTAL);
				layout.setGravity(Gravity.CENTER_VERTICAL);
				TextView tv = new TextView(getBaseContext());
				tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT));
				tv.setGravity(Gravity.CENTER_VERTICAL);
				tv.setTypeface(Typeface.SERIF);
				tv.setTextColor(Color.parseColor("#FF0000"));
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
				if (score == 100)
					tv.setText("满分");
				else
					tv.setText(Integer.toString(score) + "分");
				layout.addView(tv);
				toast.show();
			}
		}, 0, 3000);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt);
	}

	final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (!ifstop) {
					time--;
				}
				if (time >= 0) {
					Message message = handler.obtainMessage(1);
					handler.sendMessageDelayed(message, 1000);
					if (time % 60 <= 10) {
						if ((int) time / 60 == 0) {
							Toast toast = Toast.makeText(QuestionActivity.this, Integer.toString(time % 60),
									Toast.LENGTH_LONG);
							showMyToast(toast, 1000);
							toast.setGravity(Gravity.CENTER, 0, 0);
							questiontime.setText(
									"剩余时间：" + Integer.toString((int) time / 60) + ":0" + Integer.toString(time % 60));
						} else
							questiontime.setText(
									"剩余时间：" + Integer.toString((int) time / 60) + ":0" + Integer.toString(time % 60));
					} else
						questiontime.setText(
								"剩余时间：" + Integer.toString((int) time / 60) + ":" + Integer.toString(time % 60));

				} else {
					Intent toChallenge = new Intent(QuestionActivity.this, ChallengeActivity.class);
					startActivity(toChallenge);
				}
			}
			super.handleMessage(msg);
		}
	};

}