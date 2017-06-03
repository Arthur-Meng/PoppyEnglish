package com.poppyenglish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IndexActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		Button button1 = (Button) findViewById(R.id.ladderbutton);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLadder = new Intent(IndexActivity.this, LadderActivity.class);
				toLadder.putExtras(bundle);
				startActivity(toLadder);
			}
		});
		Button button2 = (Button) findViewById(R.id.pkbutton);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toPk = new Intent(IndexActivity.this, PkActivity.class);
				toPk.putExtras(bundle);
				startActivity(toPk);
			}
		});
		Button button3 = (Button) findViewById(R.id.challengebutton);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toChallenge = new Intent(IndexActivity.this, ChallengeActivity.class);
				toChallenge.putExtras(bundle);
				startActivity(toChallenge);
			}
		});
		Button button4 = (Button) findViewById(R.id.llkbutton);
		button4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLlk = new Intent(IndexActivity.this, LlkActivity.class);
				toLlk.putExtras(bundle);
				startActivity(toLlk);
			}
		});
		Button button5 = (Button) findViewById(R.id.lotterybutton);
		button5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLottery = new Intent(IndexActivity.this, LotteryActivity.class);
				toLottery.putExtras(bundle);
				startActivity(toLottery);
			}
		});
		Button button6 = (Button) findViewById(R.id.jggbutton);
		button6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toJgg = new Intent(IndexActivity.this, JggActivity.class);
				toJgg.putExtras(bundle);
				startActivity(toJgg);
			}
		});
		Button button7 = (Button) findViewById(R.id.friendsbutton);
		button7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toFriends = new Intent(IndexActivity.this, FriendsActivity.class);
				toFriends.putExtras(bundle);
				startActivity(toFriends);
			}
		});
		Button button8 = (Button) findViewById(R.id.personalbutton);
		button8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toPersonal = new Intent(IndexActivity.this, PersonalActivity.class);
				toPersonal.putExtras(bundle);
				startActivity(toPersonal);
			}
		});
	}
}
