package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.R.string;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class FriendsActivity extends Activity {
	private TextView httptest;
	String result;

	/*Handler handler = new Handler() {
		*//**
		 * Subclasses must implement this to receive messages.
		 *
		 * @param msg
		 *//*
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				httptest = (TextView) findViewById(R.id.httptest);
				httptest.setText(result);
			}
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		/*new Thread() {
			// 采用get方式访问J2EE服务器
			String strUrl = "http://www.arthurmeng.cn/PoppyEnglish/test";
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

			;
		}.start();*/
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
}
