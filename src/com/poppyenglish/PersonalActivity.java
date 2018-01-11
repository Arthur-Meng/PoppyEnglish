package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PersonalActivity extends Activity implements Button.OnClickListener {
	private Button bt_perosnal_head, perosnal_addinfo, perosnal_newinfo, perosnal_logout, personal_forgive,
			perosnal_oneday, oneday_button;
	private TextView perosnal_name, perosnal_level, perosnal_comment;
	private EditText personal_newname, personal_newpassword, personal_newgender, personal_newcomment;
	String tel, newname, newpassword, newgender, newcomment;
	String result;
	String cname, cgender, ccomment;
	String[] content;
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initView();
	}

	private void initView() {
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		tel = bundle.getString("tel");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		bt_perosnal_head = (Button) findViewById(R.id.bt_perosnal_head);
		bt_perosnal_head.setOnClickListener(this);
		perosnal_addinfo = (Button) findViewById(R.id.perosnal_addinfo);
		perosnal_addinfo.setOnClickListener(this);
		perosnal_oneday = (Button) findViewById(R.id.perosnal_oneday);
		perosnal_oneday.setOnClickListener(this);
		perosnal_logout = (Button) findViewById(R.id.perosnal_logout);
		perosnal_logout.setOnClickListener(this);

		perosnal_name = (TextView) findViewById(R.id.perosnal_name);
		perosnal_level = (TextView) findViewById(R.id.perosnal_level);
		perosnal_comment = (TextView) findViewById(R.id.perosnal_comment);
		perosnal_name.setText(preferences.getString("name", "用户名"));
		perosnal_level.setText(preferences.getString("honor", "暂无等级"));
		perosnal_comment.setText(preferences.getString("comment", "暂无评论"));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal, menu);
		return true;
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

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				if (result.equals("AddSuccess")) {
					Toast.makeText(PersonalActivity.this, "保存信息成功", 0).show();
					setContentView(R.layout.activity_personal);
					Intent intent = getIntent();
					finish();
					startActivity(intent);
				} else {
					Toast.makeText(PersonalActivity.this, "更新失败", 0).show();
					setContentView(R.layout.activity_personal);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_perosnal_head:
			new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry!")
					.setContentText("我们的服务器不太好，带宽不够传输图片~").show();
			break;
		case R.id.personal_forgive:
			setContentView(R.layout.activity_personal);
			initView();
			break;
		case R.id.perosnal_oneday:
			setContentView(R.layout.activity_oneday);
			OneDayURL oneDayURL = new OneDayURL();
			content = oneDayURL.find();
			TextView oneday_textView2 = (TextView) findViewById(R.id.oneday_textView2);
			TextView oneday_textView3 = (TextView) findViewById(R.id.oneday_textView3);
			if (null != content[0]) {
				oneday_textView2.setText(content[0]);
			}
			if (null != content[1]) {
				oneday_textView3.setText(content[1]);
			}
			oneday_button = (Button) findViewById(R.id.oneday_button);
			oneday_button.setOnClickListener(this);
			break;
		case R.id.oneday_button:
			Intent intent1 = new Intent(Intent.ACTION_SEND);
			intent1.putExtra(Intent.EXTRA_TEXT, "每日一句："+content[0]+" 翻译："+content[1]);
			intent1.setType("text/plain");
			startActivity(Intent.createChooser(intent1, "share"));
			break;
		case R.id.perosnal_addinfo:
			setContentView(R.layout.activity_personal_addinfo);
			perosnal_newinfo = (Button) findViewById(R.id.personal_newinfo);
			perosnal_newinfo.setOnClickListener(this);
			personal_newname = (EditText) findViewById(R.id.personal_newname);
			personal_newpassword = (EditText) findViewById(R.id.personal_newpassword);
			personal_newgender = (EditText) findViewById(R.id.personal_newgender);
			personal_newcomment = (EditText) findViewById(R.id.personal_newcomment);
			personal_forgive = (Button) findViewById(R.id.personal_forgive);
			personal_forgive.setOnClickListener(this);
			break;
		case R.id.perosnal_logout:
			preferences.edit().clear().commit();
			Toast.makeText(PersonalActivity.this, "退出登录成功", 0).show();
			Intent toLogin = new Intent(PersonalActivity.this, LoginActivity.class);
			startActivity(toLogin);
			break;
		case R.id.personal_newinfo:
			newname = personal_newname.getText().toString().trim();
			newpassword = personal_newpassword.getText().toString().trim();
			newgender = personal_newgender.getText().toString().trim();
			newcomment = personal_newcomment.getText().toString().trim();

			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("password", newpassword);
			editor.putString("name", newname);
			editor.putString("gender", newgender);
			editor.putString("honor", "1");
			editor.putString("comment", newcomment);
			editor.commit();
			try {
				cname = URLEncoder.encode(newname, "utf-8");
				cgender = URLEncoder.encode(newgender, "utf-8");
				ccomment = URLEncoder.encode(newcomment, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			new Thread() {
				String strUrl = "http://www.arthurmeng.cn/PoppyEnglish/addinfo?tel=" + tel + "&" + "name=" + cname + "&"
						+ "password=" + newpassword + "&" + "gender=" + cgender + "&" + "comment=" + ccomment;
				URL url = null;

				public void run() {
					System.out.println(strUrl);
					try {
						url = new URL(strUrl);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						System.out.println("myhttptest-error1");
						e.printStackTrace();
					}
					HttpURLConnection httpURLConnection = null;
					try {
						httpURLConnection = (HttpURLConnection) url.openConnection();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("myhttptest-error2");
						e.printStackTrace();
					}
					InputStreamReader inputStreamReader = null;
					try {
						inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("myhttptest-error3");
						e.printStackTrace();
					}
					BufferedReader buff = new BufferedReader(inputStreamReader);
					result = "";
					String readLine = null;
					try {
						while ((readLine = buff.readLine()) != null) {
							result += readLine;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("myhttptest-error4");
						e.printStackTrace();
					}

					try {
						inputStreamReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("myhttptest-error5");
						e.printStackTrace();
					}
					httpURLConnection.disconnect();
					handler.sendEmptyMessage(0x123);
				}

			}.start();
			break;
		default:
			break;
		}
	}
}
