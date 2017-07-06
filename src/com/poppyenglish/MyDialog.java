package com.poppyenglish;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;

import android.view.View;

public class MyDialog extends Dialog {

	private MyView myView;

	private final View key[] = new View[27];

	private MyView1 myView1;

	public MyDialog(Context context, MyView myView) {
		super(context);
		this.myView = myView;
	}

	public MyDialog(Context context, MyView1 myView1) {
		super(context);
		this.myView1 = myView1;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("请选择一个字母填入");
		setContentView(R.layout.dialog);
		findviews();

		setListeners();

	}

	private void setListeners() {
		for (int i = 0; i < key.length; i++) {
			final int t = i;
			key[i].setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					returnresult(t);
				}
			});
		}
	}

	private void returnresult(int tile) {

		myView.setSelectedTile(tile);

		dismiss();
	}

	private void findviews() {
		key[0] = findViewById(R.id.a);
		key[1] = findViewById(R.id.b);
		key[2] = findViewById(R.id.c);
		key[3] = findViewById(R.id.d);
		key[4] = findViewById(R.id.e);
		key[5] = findViewById(R.id.f);
		key[6] = findViewById(R.id.g);
		key[7] = findViewById(R.id.h);
		key[8] = findViewById(R.id.i);
		key[9] = findViewById(R.id.j);
		key[10] = findViewById(R.id.k);
		key[11] = findViewById(R.id.l);
		key[12] = findViewById(R.id.m);
		key[13] = findViewById(R.id.n);
		key[14] = findViewById(R.id.o);
		key[15] = findViewById(R.id.p);
		key[16] = findViewById(R.id.q);
		key[17] = findViewById(R.id.r);
		key[18] = findViewById(R.id.s);
		key[19] = findViewById(R.id.t);
		key[20] = findViewById(R.id.u);
		key[21] = findViewById(R.id.v);
		key[22] = findViewById(R.id.w);
		key[23] = findViewById(R.id.x);
		key[24] = findViewById(R.id.y);
		key[25] = findViewById(R.id.z);
		key[26] = findViewById(R.id.kong);
	}
}

