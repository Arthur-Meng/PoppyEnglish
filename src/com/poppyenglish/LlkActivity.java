package com.poppyenglish;

import java.util.Timer;
import java.util.TimerTask;


import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LlkActivity extends Activity {
	String words="已发现：";
	int leftTime=90;
	public static LlkActivity llkActivity;
	public LockPatternView lockPatternView;
	public TextView showResult;
	public Button reNewGame;
	public TextView scoreText;
	public TextView timeText;
	Timer timer = new Timer(); 
	public LlkActivity() {
		llkActivity = this;
	}
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 runOnUiThread(new Runnable() { 
				 public void run() {
					leftTime--;    
					timeText.setText(String.valueOf(leftTime));    
		             if(leftTime < 0){
		            	 timer.cancel();
		            	 setContentView(R.layout.showresult);
		         		 showResult=(TextView)findViewById(R.id.showResult);
		                  for(String word:lockPatternView.words){
		                	  words=words+"\n"+word;
		                  }
		                  showResult.setText(String.valueOf(words));
		             } 
				 }
			 });
		}   
		 
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_llk);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.mywhite);//通知栏所需颜色
		
		Copy.copyFileFromAssets(this, "NotesList.sqlite3");
		lockPatternView=(LockPatternView)findViewById(R.id.lockPatternView);
		reNewGame=(Button)findViewById(R.id.buttonNewGame);
		scoreText=(TextView)findViewById(R.id.score);
		timeText=(TextView)findViewById(R.id.timeText);
		reNewGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lockPatternView.reGame();
				leftTime=90;
			}
		});
		timer.schedule(task, 0, 1000);
	}

	public void setScore(int score){
		scoreText.setText(String.valueOf(score));
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
