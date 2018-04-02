package com.poppyenglish;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class IndexActivity extends Activity {
	GifView gf1;
	MyContent myContent = MyContent.getInstance();
	SocketServer socketServer = SocketServer.getInstance();
	String[] content;
	String myTel;
	SharedPreferences preferences;
	int onlyOne = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		preferences = getSharedPreferences("userinfo", MODE_PRIVATE);
		myTel = preferences.getString("tel", "");

		// 设置gif
		gf1 = (GifView) findViewById(R.id.gif1);
		// 设置Gif图片源
		gf1.setGifImage(R.drawable.gif1);
		// 设置显示的大小，拉伸或者压缩
		// gf1.setShowDimension(300,300);
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		gf1.setShowDimension((int) (dMetrics.widthPixels), (int) (getWallpaperDesiredMinimumHeight() * 1 / 3));
		// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
		gf1.setGifImageType(GifImageType.COVER);

		Button button1 = (Button) findViewById(R.id.ladderbutton);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLadder = new Intent(IndexActivity.this, LadderActivity.class);
				startActivity(toLadder);
			}
		});
		Button button2 = (Button) findViewById(R.id.pkbutton);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toPk = new Intent(IndexActivity.this, PkActivity.class);
				startActivity(toPk);
			}
		});
		Button button3 = (Button) findViewById(R.id.challengebutton);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toChallenge = new Intent(IndexActivity.this, ChallengeActivity.class);
				startActivity(toChallenge);
			}
		});
		Button button4 = (Button) findViewById(R.id.llkbutton);
		button4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLlk = new Intent(IndexActivity.this, LlkActivity.class);
				startActivity(toLlk);
			}
		});
		Button button5 = (Button) findViewById(R.id.lotterybutton);
		button5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLottery = new Intent(IndexActivity.this, LotteryActivity.class);
				startActivity(toLottery);
			}
		});
		Button button6 = (Button) findViewById(R.id.jggbutton);
		button6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toJgg = new Intent(IndexActivity.this, JggActivity.class);
				startActivity(toJgg);
			}
		});
		Button button7 = (Button) findViewById(R.id.friendsbutton);
		button7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toFriends = new Intent(IndexActivity.this, FriendsActivity.class);
				startActivity(toFriends);
			}
		});
		Button button8 = (Button) findViewById(R.id.personalbutton);
		button8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toPersonal = new Intent(IndexActivity.this, PersonalActivity.class);
				startActivity(toPersonal);
			}
		});

		socketServer.write("register:" + myTel);

		if (false == indexThread.isAlive()) {
			indexThread.start();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		onlyOne = 0;
	}

	Thread indexThread = new Thread() {
		public Boolean ifStop = false;

		public void run() {
			while (!ifStop) {
				if (myContent.getIfReady().equals(true)) {
					content = myContent.getContent();
					if (content[1].equals("ifpk")) {
						if (onlyOne < 1) {
							indexHandler.sendEmptyMessage(0x121);
							onlyOne++;
						}
					}
				}
			}
		}
	};

	Handler indexHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x121) {
				SweetAlertDialog.OnSweetClickListener nolistener = new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						socketServer.write(myTel + ":ifpkno:" + content[2]);
					}
				};
				SweetAlertDialog.OnSweetClickListener yeslistener = new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						socketServer.write(myTel + ":ifpkyes:" + content[2]);
						Intent toPkQuestionActivity = new Intent(IndexActivity.this, PkQuestionActivity.class);
						startActivity(toPkQuestionActivity);
					}
				};
				new SweetAlertDialog(IndexActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE).setTitleText("注意")
						.setContentText("您的好友" + content[3] + "想要挑战您").setCancelClickListener(nolistener)
						.setConfirmClickListener(yeslistener).show();
			}

		}
	};
}
