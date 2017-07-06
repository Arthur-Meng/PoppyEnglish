package com.poppyenglish;

import android.app.Application;

public class MyApplication extends Application{

	private int gq;
	private int question;
	
	
	public void onCreare(){
		super.onCreate();
		setGq(1);
		setQuestion(1);
	}
	
	public int getQuestion()
	{
		return question;
	}
	public int getGq(){
		return gq;
		
	}
	
	public void setQuestion(int question){
		this.question=question;
	}
	public void setGq(int gq){
		this.gq=gq;
	}
}
