package com.poppyenglish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SuccessDialog extends Dialog{
	
	private final View keys[]=new View[1];	
	
	public SuccessDialog(Context context,MyView mView) {
		super(context);
		
	}
	public SuccessDialog(Context context, MyView1 myView1) {
		super(context);
	}


	protected void onCreate(Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		setTitle("恭喜你成功了！");
		setContentView(R.layout.successful);
	    findView();
		setListeners();
	}


	private void findView() {
		keys[0]=findViewById(R.id.qd);
	}


	private void setListeners() {
		keys[0].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.qd:
					v.getContext().startActivity(new Intent(v.getContext(),JggActivity.class));
				break;
				}
			}
		});
	}



}
