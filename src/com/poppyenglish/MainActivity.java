package com.poppyenglish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends Activity {

	private int splashTime = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

	}

	@Override
	protected void onResume() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);

				if (!preferences.getString("tel", "").equals("")) {
					String[] result = null;

					result = new LoginURL().login(preferences.getString("tel", ""),
							preferences.getString("password", ""));

					if (result != null && result[0] != "") {
						if (result[0].equals("NoUser") || result[0].equals("WrongPassword")
								|| result[0].equals("Error")) {
							Intent intent = new Intent(MainActivity.this, StartActivity.class);
							startActivity(intent);
							MainActivity.this.finish();
						} else {
							Bundle bundle = new Bundle();
							bundle.putCharSequence("tel", preferences.getString("tel", ""));
							bundle.putCharSequence("password", preferences.getString("tel", ""));
							if (!preferences.getString("name", "").equals("")) {
								bundle.putCharSequence("name", preferences.getString("name", ""));
							}
							if (!preferences.getString("gender", "").equals("")) {
								bundle.putCharSequence("gender", preferences.getString("gender", ""));
							}
							if (!preferences.getString("honor", "").equals("")) {
								bundle.putCharSequence("honor", preferences.getString("honor", ""));
							}
							if (preferences.getString("comment", "") != null) {
								bundle.putCharSequence("comment", preferences.getString("comment", ""));
							}

							Intent intent = new Intent(MainActivity.this, IndexActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					} else {
						new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
								.setTitleText("Sorry!").setContentText("您未联网或者我们的服务器正在抢修，请检查网络或耐心等待~")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										Intent intent = new Intent(MainActivity.this, StartActivity.class);
										startActivity(intent);
										MainActivity.this.finish();
									}
								}).show();

					}
				} else {
					Intent intent = new Intent(MainActivity.this, StartActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}
			}
		};

		new Thread() {

			@Override
			public void run() {
				try {
					Thread.sleep(splashTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
			}

		}.start();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
