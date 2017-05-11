package com.poppyenglish;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int splashTime = 2000;
	private static final String spFileName = "sp";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		SharedPreferences sp = getSharedPreferences(spFileName, Context.MODE_PRIVATE);

		if (sp.getBoolean("isFirst", true)) {
			Editor editor = sp.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
		}

	}

	@Override
	protected void onResume() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(MainActivity.this, StartActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
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
