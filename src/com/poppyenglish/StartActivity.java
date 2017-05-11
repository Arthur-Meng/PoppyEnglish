package com.poppyenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		Button button = (Button) findViewById(R.id.signbutton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toSign = new Intent(StartActivity.this, SignActivity.class);
				startActivity(toSign);
			}
		});
		Button button2 = (Button) findViewById(R.id.loginbutton);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLogin = new Intent(StartActivity.this, LoginActivity.class);
				startActivity(toLogin);
			}
		});

	}
}
