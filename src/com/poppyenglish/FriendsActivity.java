package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.poppyenglish.R.layout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.R.integer;
import android.R.string;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsActivity extends Activity implements Button.OnClickListener {
	private Button searchfriend, friends_forgive, friends_search;
	private EditText friends_username;
	public Context context;
	String tel, find, result;
	String ckey;
	String[] userinfo = new String[400];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		initView();
		findfriends(2, tel);

	}

	private void initView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		searchfriend = (Button) findViewById(R.id.searchfriend);
		searchfriend.setOnClickListener(this);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		tel = bundle.getString("tel");

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
		switch (v.getId()) {
		case R.id.searchfriend:
			setContentView(R.layout.activity_friends_add);
			friends_forgive = (Button) findViewById(R.id.friends_forgive);
			friends_forgive.setOnClickListener(this);
			friends_username = (EditText) findViewById(R.id.friends_username);
			friends_search = (Button) findViewById(R.id.friends_search);
			friends_search.setOnClickListener(this);
			break;
		case R.id.friends_forgive:
			setContentView(R.layout.activity_friends);
			initView();
			userinfo=new String[400];
			findfriends(2, tel);
			break;
		case R.id.friends_search:
			setContentView(R.layout.activity_friends_add);
			friends_forgive = (Button) findViewById(R.id.friends_forgive);
			friends_forgive.setOnClickListener(this);
			friends_search = (Button) findViewById(R.id.friends_search);
			friends_search.setOnClickListener(this);
			find = friends_username.getText().toString().trim();
			userinfo=new String[400];
			try {
				ckey=URLEncoder.encode(find, "utf-8");
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			findfriends(1, tel + "&find=" + ckey);
			friends_username = (EditText) findViewById(R.id.friends_username);
			break;
		default:
			break;
		}
	}

	public void findfriends(final int x, final String key) {
		
		new Thread() {
			String strUrl = "http://www.arthurmeng.cn/PoppyEnglish/friends?tel=" + key;
			URL url = null;

			public void run() {
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
					e.printStackTrace();
				}
				InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				BufferedReader buff = new BufferedReader(inputStreamReader);
				result = "";
				int k = 1;
				;
				String readLine = null;
				try {
					while ((readLine = buff.readLine()) != null) {
						userinfo[k] = readLine;
						result += readLine;
						k++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				httpURLConnection.disconnect();
				if (x == 1)
					handler.sendEmptyMessage(0x123);
				if (x == 2)
					handler.sendEmptyMessage(0x456);
			}

			;
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				if (result.equals("NoUser")) {
					Toast.makeText(FriendsActivity.this, "没有该用户", 0).show();
				} else if (result.equals("Add")) {
					Toast.makeText(FriendsActivity.this, result, 0).show();
				} else if (result.equals("Remove")) {
					Toast.makeText(FriendsActivity.this, result, 0).show();
				} else if (result.equals("Error")) {
					Toast.makeText(FriendsActivity.this, "错误", 0).show();
				} else {
					int k = 1;
					while (userinfo[k] != null) {
						if (k % 4 == 0) {
							showfriends2(userinfo[k-3], userinfo[k -2], userinfo[k - 1], userinfo[k]);
						}
						k++;
					}
				}
			}
			if (msg.what == 0x456) {
				int k = 1;
				while (userinfo[k] != null) {
					if (k % 4 == 0) {
						showfriends(userinfo[k-3], userinfo[k -2], userinfo[k - 1], userinfo[k]);
					}
					k++;
				}
			}
		}
	};

	public void showfriends(String name, String gender, String honor, String comment) {
		LinearLayout relative = (LinearLayout) findViewById(R.id.showuser);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View userLayout = layoutInflater.inflate(R.layout.userinfo, null);
		TextView friends_user_name = (TextView) userLayout.findViewById(R.id.friends_user_name);
		friends_user_name.setText(name);
		TextView friends_user_level = (TextView) userLayout.findViewById(R.id.friends_user_level);
		friends_user_level.setText(honor);
		TextView friends_user_comment = (TextView) userLayout.findViewById(R.id.friends_user_comment);
		friends_user_comment.setText(comment);
		relative.addView(userLayout);
	}

	public void showfriends2(String name, String gender, String honor, String comment) {
		LinearLayout relative = (LinearLayout) findViewById(R.id.showuser2);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View userLayout = layoutInflater.inflate(R.layout.userinfo_add, null);
		TextView friends_user_name = (TextView) userLayout.findViewById(R.id.friends_user_name);
		friends_user_name.setText(name);
		TextView friends_user_level = (TextView) userLayout.findViewById(R.id.friends_user_level);
		friends_user_level.setText(honor);
		TextView friends_user_comment = (TextView) userLayout.findViewById(R.id.friends_user_comment);
		friends_user_comment.setText(comment);
		relative.addView(userLayout);

	}
}
