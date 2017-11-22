package com.poppyenglish;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.ProgressDialog;

public class PkActivity extends Activity implements Button.OnClickListener {
	private Button matchbutton;
	SocketServer socketServer = SocketServer.getInstance();
	String enemyTel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pk);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);

		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		enemyTel = bundle.getString("tel");
		matchbutton = (Button) findViewById(R.id.matchbutton);
		matchbutton.setOnClickListener(this);

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
		case R.id.matchbutton:
			String msg = enemyTel + ":match";
			socketServer.write(msg);
			MyContent myContent = MyContent.getInstance();
			while (true) {
				/*
				 * ProgressDialog dialog = new ProgressDialog(PkActivity.this);
				 * dialog.setMessage("正在匹配，请耐心等待"); dialog.show();
				 */
				if (myContent.getIfReady().equals(true)) {
					String[] cotent = myContent.getContent();
					if (cotent[1].equals("noothermatch")) {
					} else if (cotent[1].startsWith("tel")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(PkActivity.this);
						builder.setTitle("匹配成功");
						builder.setMessage("对手是：" + cotent[2]);
						builder.show();
						setContentView(R.layout.activity_pkquestion);
						break;
					} else {
						break;
					}
				}
			}
			break;
		default:
			break;
		}
	}
}
