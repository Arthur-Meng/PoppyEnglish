package com.poppyenglish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

@SuppressLint("ClickableViewAccessibility")
public class MyView extends View {
	private float width;
	private float height;
	private Game game = new Game();
	private EditText English;
	private String vc;
	int selectX;
	int selectY;
	MyApplication app;

	public MyView(Context context) {
		super(context);
		app = (MyApplication) context.getApplicationContext();

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.width = w / 9f;
		this.height = h / 10f;
		super.onSizeChanged(w, h, oldw, oldh);
		game.ccc(app.getQuestion());

	}

	protected void onDraw(Canvas canvas) {
		Paint backgroundPaint = new Paint();
		backgroundPaint.setColor(getResources().getColor(R.color.background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
		Paint darkPaint = new Paint();
		darkPaint.setColor(getResources().getColor(R.color.dark));

		Paint hilitePaint = new Paint();
		hilitePaint.setColor(getResources().getColor(R.color.dark));

		Paint lightPaint = new Paint();
		lightPaint.setColor(getResources().getColor(R.color.light));

		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, lightPaint);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilitePaint);
			canvas.drawLine(i * width, 0, i * width, getHeight() - height, lightPaint);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight() - height, hilitePaint);

		}

		for (int i = 0; i < 10; i++) {
			if (i % 3 != 0) {
				continue;
			}
			canvas.drawLine(0, i * height, getWidth(), i * height, darkPaint);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilitePaint);
			canvas.drawLine(i * width, 0, i * width, getHeight() - height, darkPaint);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight() - height, hilitePaint);

		}
		Paint englishPaint = new Paint();
		englishPaint.setColor(Color.GRAY);
		englishPaint.setStyle(Paint.Style.FILL);
		englishPaint.setTextSize(height * 0.75f);
		englishPaint.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = englishPaint.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - (fm.ascent + fm.descent) / 2;

		Paint englishPaint1 = new Paint();
		englishPaint1.setColor(Color.GRAY);
		englishPaint1.setStyle(Paint.Style.FILL);
		englishPaint1.setTextSize(height * 0.4f);

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				canvas.drawText(game.wjs(i, j), (i * width + x), (j * height + y), englishPaint);
			}

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				if (game.huahei(i, j) == 1) {
					canvas.drawRect(i * width, j * height, (i + 1) * width + 1, (j + 1) * height + 1, englishPaint);

				}
			}

		canvas.drawText("提示1：吃的，快餐", 0, (9.4f * height), englishPaint1);

		if (app.getQuestion() == 2) {
			canvas.drawText("", 0, (9.4f * height), englishPaint1);
		}

		super.onDraw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}

		selectX = (int) (event.getX() / width);
		selectY = (int) (event.getY() / height);

		if ((selectY * 9 + selectX) < 81 && game.hua(selectX, selectY) == 1) {
			MyDialog myDialog = new MyDialog(getContext(), this);
			myDialog.show();

		}

		return true;
	}

	public void setSelectedTile(int tile) {

		if (game.shx(selectX, selectY, tile)) {
			invalidate();
		}
		if (game.pd()) {
			if (app.getGq() <= app.getQuestion()) {
				int h = app.getQuestion() + 1;
				app.setGq(h);
			}
			SuccessDialog success = new SuccessDialog(getContext(), this);
			success.show();

		}

	}
}
