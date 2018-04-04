package com.poppyenglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FriendsActivity extends Activity implements Button.OnClickListener {
	private Button searchfriend, friends_forgive, friends_search;
	private EditText friends_username;
	public Context context;
	String tel, find, result;
	String ckey;
	String[] userinfo = new String[500];
	SocketServer socketServer = SocketServer.getInstance();

	MyContent myContent = MyContent.getInstance();
	final HonorChange honorChange = new HonorChange();
	SetQuestion setQuestion;
	String[] content;
	String[] queIDS;
	String enemyTel;
	String enemyName;
	String trueResult;
	String[] newResult;
	int onlytwo = 0, onlythree = 0;
	int num = 0;
	int prenum = 0;

	boolean myReady = false;
	boolean enemyReady = false;
	int my_score = 0;
	int enemy_score = 0;
	SharedPreferences preferences;
	// PkQuestion
	private TextView title;
	private TextView questiontime;
	private TextView question_content;
	private TextView pk_enemy_name;
	private TextView pk_enemy_score;
	private TextView pk_enemy_now;
	private TextView pk_my_name;
	private TextView pk_my_score;
	private TextView pk_my_now;
	private RadioButton question_ButtonA;
	private RadioButton question_ButtonB;
	private RadioButton question_ButtonC;
	private RadioButton question_ButtonD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		initView();
		findfriends(2, tel);
	}

	@Override
	public void onResume() {
		super.onResume();
		// int全部初始化
		onlytwo = 0;
		onlythree = 0;
		num = 0;
		prenum = 0;
		my_score = 0;
		enemy_score = 0;
		myReady = false;
		enemyReady = false;
	}

	private void initView() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);// 通知栏所需颜色

		searchfriend = (Button) findViewById(R.id.searchfriend);
		searchfriend.setOnClickListener(this);
		Intent intent = getIntent();
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		tel = preferences.getString("tel", "");

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
		switch (v.getId()) {
		case R.id.searchfriend:
			setContentView(R.layout.activity_friends_add);
			friends_forgive = (Button) findViewById(R.id.friends_forgive);
			friends_forgive.setOnClickListener(this);
			friends_username = (EditText) findViewById(R.id.friends_username);
			friends_search = (Button) findViewById(R.id.friends_search);
			friends_search.setOnClickListener(this);
			break;
		case R.id.friends_forgive:
			setContentView(R.layout.activity_friends);
			initView();
			userinfo = new String[500];
			findfriends(2, tel);
			break;
		case R.id.friends_search:
			setContentView(R.layout.activity_friends_add);
			friends_forgive = (Button) findViewById(R.id.friends_forgive);
			friends_forgive.setOnClickListener(this);
			friends_search = (Button) findViewById(R.id.friends_search);
			friends_search.setOnClickListener(this);
			find = friends_username.getText().toString().trim();
			userinfo = new String[500];
			try {
				ckey = URLEncoder.encode(find, "utf-8");

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			findfriends(1, tel + "&find=" + ckey);
			friends_username = (EditText) findViewById(R.id.friends_username);
			break;
		case R.id.pk_question_ButtonA:
			question_ButtonB.setClickable(false);
			question_ButtonC.setClickable(false);
			question_ButtonD.setClickable(false);
			socketServer.write(tel + ":queNum:" + enemyTel + ":" + num + "-A");
			if (trueResult.equals("A")) {
				confirmMyQue(true);
			} else {
				confirmMyQue(false);
			}
			break;
		case R.id.pk_question_ButtonB:
			question_ButtonA.setClickable(false);
			question_ButtonC.setClickable(false);
			question_ButtonD.setClickable(false);
			socketServer.write(tel + ":queNum:" + enemyTel + ":" + num + "-B");
			if (trueResult.equals("B")) {
				confirmMyQue(true);
			} else {
				confirmMyQue(false);
			}
			break;
		case R.id.pk_question_ButtonC:
			question_ButtonA.setClickable(false);
			question_ButtonB.setClickable(false);
			question_ButtonD.setClickable(false);
			socketServer.write(tel + ":queNum:" + enemyTel + ":" + num + "-C");
			if (trueResult.equals("C")) {
				confirmMyQue(true);
			} else {
				confirmMyQue(false);
			}

			break;
		case R.id.pk_question_ButtonD:
			question_ButtonA.setClickable(false);
			question_ButtonB.setClickable(false);
			question_ButtonC.setClickable(false);
			socketServer.write(tel + ":queNum:" + enemyTel + ":" + num + "-D");
			if (trueResult.equals("D")) {
				confirmMyQue(true);
			} else {
				confirmMyQue(false);
			}
			break;
		default:
			break;
		}
	}

	// X=1表示找人，2表示自己的列表
	public void findfriends(final int x, final String key) {

		new Thread() {
			String strUrl = "http://192.168.191.1/PoppyEnglish/friends?tel=" + key;
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
					e.printStackTrace();
				}
				InputStreamReader inputStreamReader = null;
				try {
					inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				BufferedReader buff = new BufferedReader(inputStreamReader);
				result = "";
				int k = 1;
				String readLine = null;
				try {
					while ((readLine = buff.readLine()) != null) {
						userinfo[k] = readLine;
						result += readLine;
						k++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				httpURLConnection.disconnect();
				if (x == 1)
					handler.sendEmptyMessage(0x123);
				if (x == 2)
					handler.sendEmptyMessage(0x456);
			}

			;
		}.start();
	}

	public void addfriend(String add) {
		try {
			ckey = URLEncoder.encode(add, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		findfriends(1, tel + "&add=" + ckey);
	}

	public void removefriend(String remove) {
		try {
			ckey = URLEncoder.encode(remove, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		findfriends(1, tel + "&remove=" + ckey);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				if (result.equals("NoUser")) {
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("注意！")
							.setContentText("您之前已经添加了该好友！").show();
				} else if (result.equals("Add")) {
					setContentView(R.layout.activity_friends);
					initView();
					userinfo = new String[500];
					findfriends(2, tel);
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("恭喜！")
							.setContentText("您已经成功添加该好友！").show();
				} else if (result.equals("Have")) {
					setContentView(R.layout.activity_friends);
					initView();
					userinfo = new String[500];
					findfriends(2, tel);
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("注意！")
							.setContentText("您之前已经添加了该好友！").show();
				} else if (result.equals("Remove")) {
					setContentView(R.layout.activity_friends);
					initView();
					userinfo = new String[500];
					findfriends(2, tel);
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("恭喜！")
							.setContentText("您已经成功删除了该好友！").show();

				} else if (result.equals("Error")) {
					Toast.makeText(FriendsActivity.this, "错误", 0).show();
				} else {
					int k = 1;
					while (userinfo[k] != null) {
						if (k % 5 == 0) {
							showfriends2(userinfo[k - 4], userinfo[k - 3], userinfo[k - 2], userinfo[k - 1],
									userinfo[k]);
						}
						k++;
					}
				}
			}
			if (msg.what == 0x456) {
				int k = 1;
				while (userinfo[k] != null) {
					if (k % 5 == 0) {
						showfriends(userinfo[k - 4], userinfo[k - 3], userinfo[k - 2], userinfo[k - 1], userinfo[k]);
					}
					k++;
				}
			}
		}
	};

	public void showfriends(final String name, String gender, String honor, String comment, final String removetel) {
		LinearLayout relative = (LinearLayout) findViewById(R.id.showuser);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View userLayout = layoutInflater.inflate(R.layout.userinfo, null);
		Button friends_remove = (Button) userLayout.findViewById(R.id.friends_remove);
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removefriend(removetel);
			}
		};
		friends_remove.setOnClickListener(listener);
		Button friends_pk = (Button) userLayout.findViewById(R.id.friends_pk);
		View.OnClickListener pklistener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pkfriend(removetel);
			}
		};
		friends_pk.setOnClickListener(pklistener);
		ImageView friends_head = (ImageView) userLayout.findViewById(R.id.friends_head);
		Picasso.with(getApplicationContext()).load("http://192.168.191.1/PoppyEnglish/../Pic/" + removetel + ".jpg")
				.into(friends_head);
		TextView friends_user_name = (TextView) userLayout.findViewById(R.id.friends_user_name);
		friends_user_name.setText(name);
		TextView friends_user_level = (TextView) userLayout.findViewById(R.id.friends_user_level);
		friends_user_level.setText(honor);
		TextView friends_user_comment = (TextView) userLayout.findViewById(R.id.friends_user_comment);
		friends_user_comment.setText(comment);
		relative.addView(userLayout);
	}

	public void showfriends2(final String name, String gender, String honor, String comment, final String addtel) {
		LinearLayout relative = (LinearLayout) findViewById(R.id.showuser2);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View userLayout = layoutInflater.inflate(R.layout.userinfo_add, null);
		Button friends_add = (Button) userLayout.findViewById(R.id.friends_add);
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addfriend(addtel);
			}
		};
		friends_add.setOnClickListener(listener);
		Button friends_pk = (Button) userLayout.findViewById(R.id.friends_pk);
		View.OnClickListener pklistener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pkfriend(addtel);
			}
		};
		ImageView friends_head = (ImageView) userLayout.findViewById(R.id.friends_head);
		Picasso.with(getApplicationContext()).load("http://192.168.191.1/PoppyEnglish/../Pic/" + addtel + ".jpg")
				.into(friends_head);
		friends_pk.setOnClickListener(pklistener);
		TextView friends_user_name = (TextView) userLayout.findViewById(R.id.friends_user_name);
		friends_user_name.setText(name);
		TextView friends_user_level = (TextView) userLayout.findViewById(R.id.friends_user_level);
		friends_user_level.setText(honor);
		TextView friends_user_comment = (TextView) userLayout.findViewById(R.id.friends_user_comment);
		friends_user_comment.setText(comment);
		relative.addView(userLayout);
	}

	@SuppressWarnings("deprecation")
	public void pkfriend(String pktel) {
		String msg = tel + ":friendmatch:" + pktel;
		socketServer.write(msg);
		if (false == friendThread.isAlive()) {
			friendThread.start();
		}
	}

	Thread friendThread = new Thread() {
		public Boolean ifStop = false;

		public void run() {
			while (!ifStop) {
				if (myContent.getIfReady().equals(true)) {
					content = myContent.getContent();
					if (content[1].equals("no")) {
						friendhandler.sendEmptyMessage(0x1212);
					}
					if (content[1].equals("noonline")) {
						friendhandler.sendEmptyMessage(0x1213);
					}
					if (content[1].equals("busy")) {
						friendhandler.sendEmptyMessage(0x1214);
					}
					if (content[1].equals("tel")) {
						enemyTel = content[2];
						enemyName = content[3];
						friendhandler.sendEmptyMessage(0x122);
					}
					if (content[1].equals("queID")) {
						queIDS = content[2].split("-");
						friendhandler.sendEmptyMessage(0x123);
					}
					if (content[1].equals("queNum")) {
						result = content[2];
						newResult = result.split("-");
						if (prenum + 1 == Integer.parseInt(newResult[0])) {
							prenum++;
							friendhandler.sendEmptyMessage(0x124);
						}
					}
				}
			}
		}
	};

	Handler friendhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x1212) {
				if (onlytwo < 1) {
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("该好友拒绝了您的挑战，请稍后再试~").show();
					onlytwo++;
				}
			}
			if (msg.what == 0x1213) {
				if (onlytwo < 1) {
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("Sorry")
							.setContentText("该好友尚未在线，请稍后再试~").show();
					onlytwo++;
				}
			}
			if (msg.what == 0x1214) {
				if (onlytwo < 1) {
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("该好友正在游戏中，请稍后再试~").show();
					onlytwo++;
				}
			}
			if (msg.what == 0x122) {
				if (onlythree < 1) {
					new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("匹配成功")
							.setContentText("开始和好友" + enemyName + "来PK吧").show();
					onlythree++;
				}
			}
			if (msg.what == 0x123) {
				if (num < 1) {
					num++;
					setContentView(R.layout.activity_pkquestion);
					setPkQuestion();
				}
			}
			if (msg.what == 0x124) {
				if (num < 11) {
					newResult = result.split("-");
					if (!newResult[0].equals((String.valueOf(num)))) {
						System.out.println("ID error");
					}
					if (trueResult.equals(newResult[1])) {
						// 显示敌人正确
						enemy_score += 10;
						pk_enemy_score.setText("总分：" + enemy_score);
						pk_enemy_now.setText("当前：正确");
					} else {
						// 显示敌人错误
						pk_enemy_now.setText("当前：错误");
					}
					if (myReady) {
						// 我也做好了
						if (num == 10) {
							socketServer.write("remove");
							final SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
								@SuppressWarnings("deprecation")
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									Intent intent = new Intent(FriendsActivity.this, IndexActivity.class);
									startActivity(intent);
								}
							};
							// 已经是最后一题了，显示成绩
							if (my_score > enemy_score) {
								new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
										.setTitleText("胜利！")
										.setContentText(
												"恭喜您以" + my_score + ":" + enemy_score + "战胜了您的好友" + enemyName + "!")
										.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
											@Override
											public void onClick(SweetAlertDialog sDialog) {
												SharedPreferences preferences = getSharedPreferences("userinfo",
														MODE_PRIVATE);
												SharedPreferences.Editor editor = preferences.edit();
												int level = Integer.parseInt(preferences.getString("honor", "等级"));
												level++;
												editor.remove("honor");
												editor.putString("honor", String.valueOf(level));
												editor.commit();
												if (honorChange.change(tel, "plus", "1")) {
													new SweetAlertDialog(FriendsActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("恭喜")
																	.setContentText("您的等级达到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										}).show();

							} else if (my_score < enemy_score) {
								new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.ERROR_TYPE)
										.setTitleText("失败！")
										.setContentText(
												"很遗憾，您以" + my_score + ":" + enemy_score + "惜败于好友" + enemyName + "!")
										.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
											@Override
											public void onClick(SweetAlertDialog sDialog) {
												SharedPreferences preferences = getSharedPreferences("userinfo",
														MODE_PRIVATE);
												SharedPreferences.Editor editor = preferences.edit();
												int level = Integer.parseInt(preferences.getString("honor", "等级"));
												level--;
												editor.remove("honor");
												editor.putString("honor", String.valueOf(level));
												editor.commit();
												if (honorChange.change(tel, "reduce", "1")) {
													new SweetAlertDialog(FriendsActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("抱歉")
																	.setContentText("您的等级退到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										}).show();

							} else {
								new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
										.setTitleText("战平！")
										.setContentText(
												"您以" + my_score + ":" + enemy_score + "与好友" + enemyName + "势均力敌，再接再厉！")
										.setConfirmClickListener(listener).show();
							}
						} else {
							// 继续做题
							num++;
							setQuestion(Integer.parseInt(queIDS[num - 1]));
							myReady = false;
							enemyReady = false;
						}
					} else {
						enemyReady = true;
					}
				}
			}
		}
	};

	public void setPkQuestion() {

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);

		title = (TextView) findViewById(R.id.pk_questiontitle);
		questiontime = (TextView) findViewById(R.id.pk_questiontime);
		pk_enemy_name = (TextView) findViewById(R.id.pk_enemy_name);
		pk_enemy_score = (TextView) findViewById(R.id.pk_enemy_score);
		pk_enemy_now = (TextView) findViewById(R.id.pk_enemy_now);
		pk_my_name = (TextView) findViewById(R.id.pk_my_name);
		pk_my_score = (TextView) findViewById(R.id.pk_my_score);
		pk_my_now = (TextView) findViewById(R.id.pk_my_now);
		question_content = (TextView) findViewById(R.id.pk_question_content);
		question_ButtonA = (RadioButton) findViewById(R.id.pk_question_ButtonA);
		question_ButtonB = (RadioButton) findViewById(R.id.pk_question_ButtonB);
		question_ButtonC = (RadioButton) findViewById(R.id.pk_question_ButtonC);
		question_ButtonD = (RadioButton) findViewById(R.id.pk_question_ButtonD);
		question_ButtonA.setOnClickListener(this);
		question_ButtonB.setOnClickListener(this);
		question_ButtonC.setOnClickListener(this);
		question_ButtonD.setOnClickListener(this);
		pk_enemy_name.setText("敌人：" + enemyName);
		pk_my_name.setText("我：" + preferences.getString("name", ""));
		setQuestion = new SetQuestion(getApplicationContext());
		setQuestion(Integer.parseInt(queIDS[num - 1]));
	}

	public void setQuestion(int i) {
		setQuestion.setPkQuestionByNum(i);
		title.setText("题目" + (num));
		question_content.setText(setQuestion.CONTENT);
		question_ButtonA.setText(setQuestion.A);
		question_ButtonB.setText(setQuestion.B);
		question_ButtonC.setText(setQuestion.C);
		question_ButtonD.setText(setQuestion.D);
		question_ButtonA.setChecked(false);
		question_ButtonA.setClickable(true);
		question_ButtonB.setChecked(false);
		question_ButtonB.setClickable(true);
		question_ButtonC.setChecked(false);
		question_ButtonC.setClickable(true);
		question_ButtonD.setChecked(false);
		question_ButtonD.setClickable(true);
		pk_my_score.setText("总分：" + my_score);
		pk_enemy_score.setText("总分：" + enemy_score);
		pk_my_now.setText("当前：思考");
		pk_enemy_now.setText("当前：思考");
		trueResult = setQuestion.RESULT;
	}

	public void confirmMyQue(boolean ifRight) {
		if (num < 11) {
			if (ifRight) {
				// 显示我正确
				my_score += 10;
				pk_my_score.setText("总分：" + my_score);
				pk_my_now.setText("当前：正确");
			} else {
				// 显示我错误
				pk_my_now.setText("当前：错误");
			}
			if (enemyReady) {
				// 敌人也做好了
				if (num == 10) {
					socketServer.write("remove");
					final SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
							Intent intent = new Intent(FriendsActivity.this, IndexActivity.class);
							startActivity(intent);

						}
					};
					// 已经是最后一题了，显示成绩
					if (my_score > enemy_score) {
						new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("胜利！")
								.setContentText("恭喜您以" + my_score + ":" + enemy_score + "战胜了好友" + enemyName + "!")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
										SharedPreferences.Editor editor = preferences.edit();
										int level = Integer.parseInt(preferences.getString("honor", "等级"));
										level++;
										editor.remove("honor");
										editor.putString("honor", String.valueOf(level));
										editor.commit();
										if (honorChange.change(tel, "plus", "1")) {
											new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("恭喜").setContentText("您的等级达到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();

					} else if (my_score < enemy_score) {
						new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("失败！")
								.setContentText("很遗憾，您以" + my_score + ":" + enemy_score + "惜败于好友" + enemyName + "!")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
										SharedPreferences.Editor editor = preferences.edit();
										int level = Integer.parseInt(preferences.getString("honor", "等级"));
										level--;
										editor.remove("honor");
										editor.putString("honor", String.valueOf(level));
										editor.commit();
										if (honorChange.change(tel, "reduce", "1")) {
											new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("抱歉").setContentText("您的等级退到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();

					} else {
						new SweetAlertDialog(FriendsActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
								.setTitleText("战平！")
								.setContentText("您以" + my_score + ":" + enemy_score + "与好友" + enemyName + "势均力敌，再接再厉！")
								.setConfirmClickListener(listener).show();
					}
				} else {
					// 继续做题
					num++;
					setQuestion(Integer.parseInt(queIDS[num - 1]));
					myReady = false;
					enemyReady = false;
				}
			} else {
				myReady = true;
			}
		}

	}
}
