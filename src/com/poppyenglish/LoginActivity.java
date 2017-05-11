package com.poppyenglish;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class LoginActivity extends Activity implements Button.OnClickListener {
	private EditText username, password;
	private Button bt_username_clear;
	private Button bt_pwd_clear;
	private Button forgive_pwd;
	private Button bt_pwd_eye;
	private Button login;
	private Button register;
	private boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);//通知栏所需颜色
		initView();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		
		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String user = username.getText().toString().trim();
				if ("".equals(user)) {
					bt_username_clear.setVisibility(View.INVISIBLE);
				} else {
					bt_username_clear.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		password = (EditText) findViewById(R.id.password);

		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String pwd = password.getText().toString().trim();
				if ("".equals(pwd)) {
					bt_pwd_clear.setVisibility(View.INVISIBLE);
				} else {
					bt_pwd_clear.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
		bt_username_clear.setOnClickListener(this);
		bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
		bt_pwd_clear.setOnClickListener(this);
		bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
		bt_pwd_eye.setOnClickListener(this);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(this);
		forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
		forgive_pwd.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_username_clear:
			username.setText("");
			break;
		case R.id.bt_pwd_clear:
			password.setText("");
			break;
		case R.id.bt_pwd_eye:

			if (isOpen) {
				isOpen = false;
			} else {
				isOpen = true;
			}
			changePwdOpenOrClose(isOpen);
			break;
		case R.id.login:
			Intent toIndex = new Intent(LoginActivity.this, IndexActivity.class);
			startActivity(toIndex);
			break;
		case R.id.register:
			Intent toSign = new Intent(LoginActivity.this, SignActivity.class);
			startActivity(toSign);
			break;
		case R.id.forgive_pwd:
			Toast.makeText(LoginActivity.this, "忘记密码", 0).show();
			break;
		default:
			break;
		}
	}


	private void changePwdOpenOrClose(boolean flag) {

		if (flag) {
			bt_pwd_eye.setBackgroundResource(R.drawable.eye);
			password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			bt_pwd_eye.setBackgroundResource(R.drawable.eye);
			password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
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
