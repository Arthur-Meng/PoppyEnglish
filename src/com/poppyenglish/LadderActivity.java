package com.poppyenglish;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.app.AlertDialog;
import android.app.ProgressDialog;

public class LadderActivity extends Activity implements Button.OnClickListener {

	SocketServer socketServer = SocketServer.getInstance();
	String myTel;
	String enemyTel;
	String enemyName;
	boolean myReady = false;
	boolean enemyReady = false;
	int my_score = 0;
	int enemy_score = 0;
	GifView gf2;
	final HonorChange honorChange = new HonorChange();
	MyContent myContent = MyContent.getInstance();
	String[] content;
	String[] queIDS;
	static int onlyOne = 0, onlyTwo = 0;
	static int num = 0;
	static int prenum = 0;
	int time = 30;
	String result;
	String trueResult;
	String[] newResult;
	String[] rankInfo;
	SetQuestion setQuestion;

	private Button matchbutton;
	private TextView ladder_1;
	private TextView ladder_2;
	private TextView ladder_3;
	private TextView ladder_rank;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ladder);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);
		Intent intent = getIntent();
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		myTel = preferences.getString("tel", "");
		matchbutton = (Button) findViewById(R.id.ladder_matchbutton);
		matchbutton.setOnClickListener(this);
		// 显示排名
		RankInfo rank = new RankInfo();
		rankInfo = rank.rank(myTel);
		showRankInfo();

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

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x121) {
				if (onlyOne < 1) {
					new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("Sorry")
							.setContentText("目前没有其他匹配，请耐心等待~").show();
					onlyOne++;
				}
			}
			if (msg.what == 0x122) {
				if (onlyTwo < 1) {
					new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("匹配成功")
							.setContentText("您的对手是:" + enemyName).show();
					onlyTwo++;
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
							timehandler.removeMessages(1);
							final SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									Intent intent = new Intent(LadderActivity.this, IndexActivity.class);
									startActivity(intent);
									LadderActivity.this.finish();
								}
							};
							// 已经是最后一题了，显示成绩
							if (my_score > enemy_score) {
								new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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
												level++;
												editor.remove("honor");
												editor.putString("honor", String.valueOf(level));
												editor.commit();
												if (honorChange.change(myTel, "plus", "2")) {
													new SweetAlertDialog(LadderActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("恭喜")
																	.setContentText("您的等级达到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										}).show();

							} else if (my_score < enemy_score) {
								new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.ERROR_TYPE)
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
												level--;
												editor.remove("honor");
												editor.putString("honor", String.valueOf(level));
												editor.commit();
												if (honorChange.change(myTel, "reduce", "2")) {
													new SweetAlertDialog(LadderActivity.this,
															SweetAlertDialog.SUCCESS_TYPE).setTitleText("抱歉")
																	.setContentText("您的等级退到" + level + "级")
																	.setConfirmClickListener(listener).show();
												}
											}
										}).show();

							} else {
								new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
										.setTitleText("战平！")
										.setContentText(
												"您以" + my_score + ":" + enemy_score + "与" + enemyName + "势均力敌，再接再厉！")
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
	Thread thread = new Thread() {
		public Boolean ifStop = false;

		public void run() {
			while (!ifStop) {
				if (myContent.getIfReady().equals(true)) {
					content = myContent.getContent();
					if (content[1].equals("noothermatch")) {
						handler.sendEmptyMessage(0x121);
					}
					if (content[1].equals("tel")) {
						enemyTel = content[2];
						enemyName = content[3];
						handler.sendEmptyMessage(0x122);
					}
					if (content[1].equals("queID")) {
						queIDS = content[2].split("-");
						handler.sendEmptyMessage(0x123);
					}
					if (content[1].equals("queNum")) {
						result = content[2];
						newResult = result.split("-");
						if (prenum + 1 == Integer.parseInt(newResult[0])) {
							prenum++;
							handler.sendEmptyMessage(0x124);
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
		case R.id.ladder_matchbutton:
			String msg = myTel + ":laddermatch";
			socketServer.write(msg);
			new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("正在匹配")
					.setContentText("亲，请耐心等待~").show();
			thread.start();
			break;
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
		Intent intent = getIntent();
		pk_my_name.setText("我：" + preferences.getString("name", ""));
		setQuestion = new SetQuestion(getApplicationContext());
		setQuestion(Integer.parseInt(queIDS[num - 1]));

		Message message = timehandler.obtainMessage(1); // Message
		message.what = 1;
		timehandler.sendMessageDelayed(message, 1000);
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
		time = 30;

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
					timehandler.removeMessages(1);
					socketServer.write("remove");
					final SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
							Intent intent = new Intent(LadderActivity.this, IndexActivity.class);
							startActivity(intent);
							LadderActivity.this.finish();
						}
					};
					// 已经是最后一题了，显示成绩
					if (my_score > enemy_score) {
						new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("胜利！")
								.setContentText("恭喜您以" + my_score + ":" + enemy_score + "战胜了" + enemyName + "!")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
										SharedPreferences.Editor editor = preferences.edit();
										int level = Integer.parseInt(preferences.getString("honor", "等级"));
										level++;
										level++;
										editor.remove("honor");
										editor.putString("honor", String.valueOf(level));
										editor.commit();
										if (honorChange.change(myTel, "plus", "2")) {
											new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("恭喜").setContentText("您的等级达到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();
					} else if (my_score < enemy_score) {
						new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.ERROR_TYPE).setTitleText("失败！")
								.setContentText("很遗憾，您以" + my_score + ":" + enemy_score + "惜败于" + enemyName + "!")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										SharedPreferences preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
										SharedPreferences.Editor editor = preferences.edit();
										int level = Integer.parseInt(preferences.getString("honor", "等级"));
										level--;
										level--;
										editor.remove("honor");
										editor.putString("honor", String.valueOf(level));
										editor.commit();
										if (honorChange.change(myTel, "reduce", "2")) {
											new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
													.setTitleText("抱歉").setContentText("您的等级退到" + level + "级")
													.setConfirmClickListener(listener).show();
										}
									}
								}).show();

					} else {
						new SweetAlertDialog(LadderActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
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

	Handler timehandler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				time--;
				if (time >= 0) {
					Message message = timehandler.obtainMessage(1);
					timehandler.sendMessageDelayed(message, 1000);
					questiontime.setText("剩余：" + Integer.toString((int) time));
				} else {
					question_ButtonA.setClickable(false);
					question_ButtonB.setClickable(false);
					question_ButtonC.setClickable(false);
					question_ButtonD.setClickable(false);
					socketServer.write(myTel + ":queNum:" + enemyTel + ":" + num + "-E");
					confirmMyQue(false);
					time = 30;
				}
			}
			super.handleMessage(msg);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			timehandler.removeMessages(1);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showRankInfo() {
		ladder_1 = (TextView) findViewById(R.id.ladder_1);
		ladder_2 = (TextView) findViewById(R.id.ladder_2);
		ladder_3 = (TextView) findViewById(R.id.ladder_3);
		ladder_rank = (TextView) findViewById(R.id.ladder_rank);
		if (null != rankInfo[0] && !rankInfo[0].equals("")) {
			ladder_rank.setText("您的当前排名是：" + rankInfo[0]);
		}
		if (null != rankInfo[1] && !rankInfo[1].equals("")) {
			ladder_1.setText(rankInfo[1]);
		}
		if (null != rankInfo[3] && !rankInfo[3].equals("")) {
			ladder_2.setText(rankInfo[3]);
		}
		if (null != rankInfo[5] && !rankInfo[5].equals("")) {
			ladder_3.setText(rankInfo[5]);
		}
	}

}
