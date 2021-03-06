package com.poppyenglish;


import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements Button.OnClickListener {
	private Context context;
	private EditText username, password;
	private Button bt_username_clear;
	private Button bt_pwd_clear;
	private Button forgive_pwd;
	private Button bt_pwd_eye;
	private Button login;
	private Button register;
	private boolean isOpen = false;
	String user;
	String pwd;
	String[] result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色
		initView();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		username.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 获得焦点
					bt_username_clear.setVisibility(View.VISIBLE);
				} else {
					// 失去焦点
					bt_username_clear.setVisibility(View.INVISIBLE);
				}
			}
		});
		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				user = username.getText().toString().trim();
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

				pwd = password.getText().toString().trim();
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
			LoginURL loginurl = new LoginURL();
			result = loginurl.login(user, pwd);
			handleMessage(result);
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

	public void handleMessage(String[] result) {
		if (result[0].equals("NoUser")) {
			Toast.makeText(LoginActivity.this, "请先注册", 0).show();
			Intent toSign = new Intent(LoginActivity.this, SignActivity.class);
			startActivity(toSign);
		} else {
			if (result[0].equals("WrongPassword")) {
				password.setText("");
				Toast.makeText(LoginActivity.this, "密码错误", 0).show();
			} else {
				SharedPreferences preferences = LoginActivity.this.getSharedPreferences("userinfo", MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("tel", user);
				editor.putString("password", pwd);
				editor.putString("name", result[0]);
				editor.putString("gender", result[1]);
				editor.putString("honor", result[2]);
				editor.putString("comment", result[3]);
				editor.commit();
				Intent toIndex = new Intent(LoginActivity.this, IndexActivity.class);
				Bundle bundle = new Bundle();
				
				bundle.putCharSequence("tel", user);
				bundle.putCharSequence("password", pwd);
				toIndex.putExtras(bundle);
				startActivity(toIndex);
			}
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
