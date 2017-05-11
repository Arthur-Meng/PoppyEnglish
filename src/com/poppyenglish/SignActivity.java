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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignActivity extends Activity implements Button.OnClickListener{
	private EditText sign_username, sign_password;
	private Button sign_bt_username_clear;
	private Button sign_bt_pwd_clear;
	private Button sign_bt_pwd_eye;
	private Button sign_login;
	private Button sign_register;
	private boolean sign_isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);//通知栏所需颜色
		initSignView();
	}

	private void initSignView() {
		sign_username = (EditText) findViewById(R.id.sign_username);
		sign_username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String user = sign_username.getText().toString().trim();
				if ("".equals(user)) {
					sign_bt_username_clear.setVisibility(View.INVISIBLE);
				} else {
					sign_bt_username_clear.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sign_password = (EditText) findViewById(R.id.sign_password);
		sign_password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String pwd = sign_password.getText().toString().trim();
				if ("".equals(pwd)) {
					sign_bt_pwd_clear.setVisibility(View.INVISIBLE);
				} else {
					sign_bt_pwd_clear.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sign_bt_username_clear = (Button) findViewById(R.id.sign_bt_username_clear);
		sign_bt_username_clear.setOnClickListener(this);
		sign_bt_pwd_clear = (Button) findViewById(R.id.sign_bt_pwd_clear);
		sign_bt_pwd_clear.setOnClickListener(this);

		sign_bt_pwd_eye = (Button) findViewById(R.id.sign_bt_pwd_eye);
		sign_bt_pwd_eye.setOnClickListener(this);
		sign_login = (Button) findViewById(R.id.sign_login);
		sign_login.setOnClickListener(this);
		sign_register = (Button) findViewById(R.id.sign_register);
		sign_register.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_bt_username_clear:
			sign_username.setText("");
			break;
		case R.id.sign_bt_pwd_clear:
			sign_password.setText("");
			break;
		case R.id.sign_bt_pwd_eye:
			if (sign_isOpen) {
				sign_isOpen = false;
			} else {
				sign_isOpen = true;
			}
			changePwdOpenOrClose(sign_isOpen);
			break;
		case R.id.sign_login:
			Intent toLogin = new Intent(SignActivity.this, LoginActivity.class);
			startActivity(toLogin);
			break;
		case R.id.sign_register:
			// ע�ᰴť
			break;
		default:
			break;
		}
	}

	private void changePwdOpenOrClose(boolean flag) {
		if (flag) {
			sign_bt_pwd_eye.setBackgroundResource(R.drawable.eye);
			sign_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			sign_bt_pwd_eye.setBackgroundResource(R.drawable.eye);
			sign_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
