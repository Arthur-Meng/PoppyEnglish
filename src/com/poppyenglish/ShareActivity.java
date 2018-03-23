package com.poppyenglish;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShareActivity extends Activity {
	String[] shareInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色
		// 个人信息
		SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		String tel = preferences.getString("tel", "");
		// 接收消息
		if (null != tel && !tel.equals("")) {
			ShareURL shareURL = new ShareURL();
			shareInfo = shareURL.getinfo(tel);
		}
		for (int i = 0; i < shareInfo.length / 4; i += 4) {
			if (null != shareInfo[i] && !shareInfo[i].equals("")) {
				show(shareInfo[i], shareInfo[i + 1].substring(0, 19), shareInfo[i + 2], shareInfo[i + 3]);
			} else {
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share, menu);
		return true;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void show(final String name, String time, String number, String up) {
		LinearLayout relative = (LinearLayout) findViewById(R.id.showshare);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View sharequestion = layoutInflater.inflate(R.layout.sharequestion, null);

		TextView share_name = (TextView) sharequestion.findViewById(R.id.share_name);
		share_name.setText(name);
		TextView share_time = (TextView) sharequestion.findViewById(R.id.share_time);
		share_time.setText(time);
		final SetQuestion setQuestion = new SetQuestion(getApplicationContext());
		setQuestion.setPkQuestionByNum(Integer.parseInt(number));
		TextView share_content = (TextView) sharequestion.findViewById(R.id.share_content);
		share_content.setText(setQuestion.CONTENT);

		final RadioButton share_ButtonA = (RadioButton) sharequestion.findViewById(R.id.share_ButtonA);
		share_ButtonA.setText(setQuestion.A);
		final RadioButton share_ButtonB = (RadioButton) sharequestion.findViewById(R.id.share_ButtonB);
		share_ButtonB.setText(setQuestion.B);
		final RadioButton share_ButtonC = (RadioButton) sharequestion.findViewById(R.id.share_ButtonC);
		share_ButtonC.setText(setQuestion.C);
		final RadioButton share_ButtonD = (RadioButton) sharequestion.findViewById(R.id.share_ButtonD);
		share_ButtonD.setText(setQuestion.D);
		View.OnClickListener listenerA = new View.OnClickListener() {
			@Override
			public void onClick(View v) { // TODO Auto-generated method
				if (setQuestion.RESULT.equals("A")) {
					// 成功
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("恭喜你答对了！").show();
				} else {
					share_ButtonA.setChecked(false);
					if (setQuestion.RESULT.equals("B")) {
						share_ButtonB.setChecked(true);
					}
					if (setQuestion.RESULT.equals("C")) {
						share_ButtonC.setChecked(true);
					}
					if (setQuestion.RESULT.equals("D")) {
						share_ButtonD.setChecked(true);
					}
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("不好意思，正确答案是" + setQuestion.RESULT).show();
				}
				share_ButtonA.setClickable(false);
				share_ButtonB.setClickable(false);
				share_ButtonC.setClickable(false);
				share_ButtonD.setClickable(false);
			}
		};
		share_ButtonA.setOnClickListener(listenerA);

		View.OnClickListener listenerB = new View.OnClickListener() {
			@Override
			public void onClick(View v) { // TODO Auto-generated method
				if (setQuestion.RESULT.equals("B")) {
					// 成功
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("恭喜你答对了！").show();
				} else {
					share_ButtonB.setChecked(false);
					if (setQuestion.RESULT.equals("A")) {
						share_ButtonA.setChecked(true);
					}
					if (setQuestion.RESULT.equals("C")) {
						share_ButtonC.setChecked(true);
					}
					if (setQuestion.RESULT.equals("D")) {
						share_ButtonD.setChecked(true);
					}
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("不好意思，正确答案是" + setQuestion.RESULT).show();
				}
				share_ButtonA.setClickable(false);
				share_ButtonB.setClickable(false);
				share_ButtonC.setClickable(false);
				share_ButtonD.setClickable(false);
			}
		};
		share_ButtonB.setOnClickListener(listenerB);

		View.OnClickListener listenerC = new View.OnClickListener() {
			@Override
			public void onClick(View v) { // TODO Auto-generated method
				if (setQuestion.RESULT.equals("C")) {
					// 成功
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("恭喜你答对了！").show();
				} else {
					share_ButtonC.setChecked(false);
					if (setQuestion.RESULT.equals("B")) {
						share_ButtonB.setChecked(true);
					}
					if (setQuestion.RESULT.equals("A")) {
						share_ButtonA.setChecked(true);
					}
					if (setQuestion.RESULT.equals("D")) {
						share_ButtonD.setChecked(true);
					}
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("不好意思，正确答案是" + setQuestion.RESULT).show();
				}
				share_ButtonA.setClickable(false);
				share_ButtonB.setClickable(false);
				share_ButtonC.setClickable(false);
				share_ButtonD.setClickable(false);
			}
		};
		share_ButtonC.setOnClickListener(listenerC);

		View.OnClickListener listenerD = new View.OnClickListener() {
			@Override
			public void onClick(View v) { // TODO Auto-generated method
				if (setQuestion.RESULT.equals("D")) {
					// 成功
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("恭喜你答对了！").show();
				} else {
					share_ButtonD.setChecked(false);
					if (setQuestion.RESULT.equals("B")) {
						share_ButtonB.setChecked(true);
					}
					if (setQuestion.RESULT.equals("C")) {
						share_ButtonC.setChecked(true);
					}
					if (setQuestion.RESULT.equals("A")) {
						share_ButtonA.setChecked(true);
					}
					new SweetAlertDialog(ShareActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("不好意思，正确答案是" + setQuestion.RESULT).show();
				}
				share_ButtonA.setClickable(false);
				share_ButtonB.setClickable(false);
				share_ButtonC.setClickable(false);
				share_ButtonD.setClickable(false);
			}
		};
		share_ButtonD.setOnClickListener(listenerD);
		relative.addView(sharequestion);
	}
}
