package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class SignActivity extends Activity implements Button.OnClickListener {
	private EditText sign_username, sign_password, sign_text;
	private Button sign_bt_username_clear;
	private Button sign_bt_username_get;
	private Button sign_bt_pwd_clear;
	private Button sign_bt_pwd_eye;
	private Button sign_login;
	private Button sign_register;
	private boolean sign_isOpen = false;
	String user;
	String pwd;
	String code;
	String turecode;
	String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色
		initSignView();
	}

	private void initSignView() {
		sign_username = (EditText) findViewById(R.id.sign_username);
		sign_text = (EditText) findViewById(R.id.sign_text);
		sign_username.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 获得焦点
					sign_bt_username_clear.setVisibility(View.VISIBLE);
				} else {
					// 失去焦点
					sign_bt_username_clear.setVisibility(View.INVISIBLE);
				}
			}
		});
		sign_username.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				user = sign_username.getText().toString().trim();
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
		sign_bt_username_get = (Button) findViewById(R.id.sign_bt_username_get);
		sign_bt_username_get.setOnClickListener(this);
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
		case R.id.sign_bt_username_get:
			if (isChinaPhoneLegal(user)) {
				new Thread() {
					// 采用get方式访问J2EE服务器
					String strUrl = "http://www.arthurmeng.cn/PoppyEnglish/sign?tel=" + user;
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
							System.out.println("myhttptestlog-" + result);
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
					};
				}.start();
			} else {
				Toast.makeText(SignActivity.this, "请输入正确的电话号码", 0).show();
			}
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
			code = sign_text.getText().toString().trim();
			pwd = sign_password.getText().toString().trim();
			System.out.println("log" + code);
			if (!user.equals("") && !code.equals("") && !pwd.equals("")) {
				new Thread() {
					// 采用get方式访问J2EE服务器

					String strUrl = "http://www.arthurmeng.cn/PoppyEnglish/sign?tel=" + user + "&" + "password=" + pwd;
					URL url = null;

					public void run() {
						if (code.equals(turecode)) {
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
								System.out.println("myhttptestlog-" + result);
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
						} else {
							Toast.makeText(SignActivity.this, "验证码错误", 0).show();
						}
					}
				}.start();

			} else {
				Toast.makeText(SignActivity.this, "请填写完整的信息", 0).show();
			}

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

	Handler handler = new Handler() {
		/**
		 * Subclasses must implement this to receive messages.
		 *
		 * @param msg
		 */
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				if (result.equals("Yes")) {
					Toast.makeText(SignActivity.this, "注册成功，请登陆。", 0).show();
					Intent toLogin = new Intent(SignActivity.this, LoginActivity.class);
					startActivity(toLogin);
				} else if (result.equals("No")) {
					Toast.makeText(SignActivity.this, "注册失败", 0).show();
				} else if (result.equals("error")) {
					Toast.makeText(SignActivity.this, "发送短信失败", 0).show();
				}else {
					turecode = result;
					Toast.makeText(SignActivity.this, "已发送成功，请查收", 0).show();
				}

			}
		}
	};

	public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
		String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
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
