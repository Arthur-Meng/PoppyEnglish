package com.poppyenglish;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PkQuestionActivity extends Activity implements Button.OnClickListener {

	SocketServer socketServer = SocketServer.getInstance();
	String myTel;
	String enemyTel;
	String enemyName;
	boolean myReady = false;
	boolean enemyReady = false;
	int my_score = 0;
	int enemy_score = 0;
	final HonorChange honorChange = new HonorChange();
	MyContent myContent = MyContent.getInstance();
	String[] content;
	String[] queIDS;
	int onlyOne = 0;
	int num = 0;
	int prenum = 0;
	String result;
	String trueResult;
	String[] newResult;
	SetQuestion setQuestion;

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
	SharedPreferences preferences;
	SweetAlertDialog dialog1, dialog2, dialog3, dialog4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pkquestion);
		initview();
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		myTel = preferences.getString("tel", "");
		if (false == pkQuestionThread.isAlive()) {
			pkQuestionThread.start();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// int全部初始化
		onlyOne = 0;
		num = 0;
		prenum = 0;
		my_score = 0;
		enemy_score = 0;
		myReady = false;
		enemyReady = false;
	}

	public void initview() {
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);
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

	Handler pkQuestionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x122) {
				dialog1 = new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
						.setTitleText("匹配成功").setContentText("您的对手是:" + enemyName);
				dialog1.show();
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
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									Intent intent = new Intent(PkQuestionActivity.this, IndexActivity.class);
									// thread.interrupt();
									startActivity(intent);
								}
							};
							// 已经是最后一题了，显示成绩
							if (my_score > enemy_score) {
								dialog2 = new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
										.setTitleText("胜利！")
										.setContentText("恭喜您以" + my_score + ":" + enemy_score + "战胜了" + enemyName + "!")
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
												if (honorChange.change(myTel, "plus", "1")) {
													new SweetAlertDialog(PkQuestionActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("恭喜")
																	.setContentText("您的等级达到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										});
								dialog2.show();

							} else if (my_score < enemy_score) {
								dialog3 = new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.ERROR_TYPE)
										.setTitleText("失败！")
										.setContentText(
												"很遗憾，您以" + my_score + ":" + enemy_score + "惜败于" + enemyName + "!")
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
												if (honorChange.change(myTel, "reduce", "1")) {
													new SweetAlertDialog(PkQuestionActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("抱歉")
																	.setContentText("您的等级退到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										});
								dialog3.show();

							} else {
								dialog4 = new SweetAlertDialog(PkQuestionActivity.this,
										SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("战平！").setContentText(
												"您以" + my_score + ":" + enemy_score + "与" + enemyName + "势均力敌，再接再厉！")
												.setConfirmClickListener(listener);
								dialog4.show();
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
	Thread pkQuestionThread = new Thread() {
		public Boolean ifStop = false;

		public void run() {
			while (!ifStop) {
				if (myContent.getIfReady().equals(true)) {
					content = myContent.getContent();
					if (content[1].startsWith("tel")) {
						enemyTel = content[2];
						enemyName = content[3];
						pkQuestionHandler.sendEmptyMessage(0x122);
						myContent.setIfReady(false);
					}
					if (content[1].equals("queID")) {
						if (null == enemyTel || null == enemyName) {
							new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.ERROR_TYPE)
									.setTitleText("正在匹配").setContentText("亲，异常错误，请重启客户端~").show();
						} else {
							queIDS = content[2].split("-");
							pkQuestionHandler.sendEmptyMessage(0x123);
						}
					}
					if (content[1].equals("queNum")) {
						result = content[2];
						newResult = result.split("-");
						if (prenum + 1 == Integer.parseInt(newResult[0])) {
							prenum++;
							pkQuestionHandler.sendEmptyMessage(0x124);
						}
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pk_question_ButtonA:
			question_ButtonB.setClickable(false);
			question_ButtonC.setClickable(false);
			question_ButtonD.setClickable(false);
			socketServer.write(myTel + ":queNum:" + enemyTel + ":" + num + "-A");
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
			socketServer.write(myTel + ":queNum:" + enemyTel + ":" + num + "-B");
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
			socketServer.write(myTel + ":queNum:" + enemyTel + ":" + num + "-C");
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
			socketServer.write(myTel + ":queNum:" + enemyTel + ":" + num + "-D");
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

	public void setPkQuestion() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
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
							Intent intent = new Intent(PkQuestionActivity.this, IndexActivity.class);
							startActivity(intent);
						}
					};
					// 已经是最后一题了，显示成绩
					if (my_score > enemy_score) {
						new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("胜利！")
								.setContentText("恭喜您以" + my_score + ":" + enemy_score + "战胜了" + enemyName + "!")
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
										if (honorChange.change(myTel, "plus", "1")) {
											new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("恭喜").setContentText("您的等级达到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();

					} else if (my_score < enemy_score) {
						new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("失败！")
								.setContentText("很遗憾，您以" + my_score + ":" + enemy_score + "惜败于" + enemyName + "!")
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
										if (honorChange.change(myTel, "reduce", "1")) {
											new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("抱歉").setContentText("您的等级退到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();

					} else {
						new SweetAlertDialog(PkQuestionActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
								.setTitleText("战平！")
								.setContentText("您以" + my_score + ":" + enemy_score + "与" + enemyName + "势均力敌，再接再厉！")
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
