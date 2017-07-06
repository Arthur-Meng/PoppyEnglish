package com.poppyenglish;



import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class JggActivity extends Activity implements Button.OnClickListener {
	private Game game = new Game();
	private Button yi;
	private Button er;
	private Button san;
	private Button si;
	public int g;
	private int q;
	public MyView view;
	private MyApplication app;
	private Button wu;
	private Button liu;
	private Button qi;
	private Button ba;

	@Override
	protected void onResume() {
		super.onResume();
		onCreate(null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jgg);
		app =new MyApplication();
		lookview();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);//通知栏所需颜色
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
	public void lookview() {

		yi = (Button) findViewById(R.id.yi);
		yi.setOnClickListener(this);
		er = (Button) findViewById(R.id.er);
		er.setOnClickListener(this);
		san = (Button) findViewById(R.id.san);
		san.setOnClickListener(this);
		si = (Button) findViewById(R.id.si);
		si.setOnClickListener(this);
		wu = (Button) findViewById(R.id.wu);
		wu.setOnClickListener(this);
		liu = (Button) findViewById(R.id.liu);
		liu.setOnClickListener(this);
		qi = (Button) findViewById(R.id.qi);
		qi.setOnClickListener(this);
		ba = (Button) findViewById(R.id.ba);
		ba.setOnClickListener(this);
		if (1 < app.getGq()) {

			if (2 <= app.getGq()) {
				er.setVisibility(View.VISIBLE);

			}
			if (3 <= app.getGq()) {
				san.setVisibility(View.VISIBLE);

			}
			if (4 <= app.getGq()) {
				si.setVisibility(View.VISIBLE);
			}

			if (5 <= app.getGq()) {
				wu.setVisibility(View.VISIBLE);
			}
			if (6 <= app.getGq()) {
				liu.setVisibility(View.VISIBLE);
			}
			if (7 <= app.getGq()) {
				qi.setVisibility(View.VISIBLE);
			}
			if (8 <= app.getGq()) {
				ba.setVisibility(View.VISIBLE);
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = getIntent();
			final Bundle bundle = intent.getExtras();
			Intent toIndex = new Intent(JggActivity.this, IndexActivity.class);
			if (bundle != null)
				toIndex.putExtras(bundle);
			else {
				Bundle newbundle = new Bundle(); 
				toIndex.putExtras(newbundle);
			}
			startActivity(toIndex);
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {

		Intent toQuestion = new Intent(JggActivity.this, JggQuestionActivity.class);
		switch (v.getId()) {

		case R.id.yi:
			app.setQuestion(1);
			startActivity(toQuestion);
			break;

		case R.id.er:
			app.setQuestion(2);
			startActivity(toQuestion);
			break;

		case R.id.san:
			app.setQuestion(3);
			startActivity(toQuestion);
			break;
		case R.id.si:
			app.setQuestion(4);
			startActivity(toQuestion);
			break;
		case R.id.wu:
			app.setQuestion(5);
			startActivity(toQuestion);
			break;
		case R.id.liu:
			app.setQuestion(6);
			startActivity(toQuestion);
			break;
		case R.id.qi:
			app.setQuestion(7);
			startActivity(toQuestion);
			break;
		case R.id.ba:
			app.setQuestion(8);
			startActivity(toQuestion);
			break;
		}
	}
}
