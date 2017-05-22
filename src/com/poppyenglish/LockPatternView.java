package com.poppyenglish;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LockPatternView extends View {

	public int score=0;
	private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint1=new Paint(Paint.ANTI_ALIAS_FLAG);
	public List<String> words=new ArrayList<String>();;
	private Point[][] points=new Point[4][4];
	private boolean isInit,isSelect,isFinish,movingNoPoint;
	private float width,height,bitmapR,movingX,movingY;
	public SQLiteDatabase db;
	private Bitmap pointNormal,pointPressed,pointError,pointSuccess,pointReget;
	
	private List<Point> pointList=new ArrayList<Point>();
	
	public LockPatternView(Context context){
		super(context);
	}
	public LockPatternView(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	public void onDraw(Canvas canvas){
		db=SQLiteDatabase.openOrCreateDatabase("data/data/" +"com.poppyenglish"+ "/databases/NotesList.sqlite3",null);
		if(!isInit){
			initPoints();
		}
		points2Canvas(canvas);
	}
	private void points2Canvas(Canvas canvas) {
		Rect src = new Rect(); 
		src. left = 0;
		src. top = 0;
		src. right = 100;
		src. bottom = 100;
		
		RectF dst= new RectF();
		
		// TODO Auto-generated method stub
		for(int i=0;i<points.length;i++){
			for(int j=0;j<points[i].length;j++){
				Point point=points[i][j];
				dst.left = point.x-bitmapR;
				dst. top = point.y-bitmapR;
				dst. right = point.x+bitmapR;
				dst. bottom = point.y+bitmapR;
				if(point.state==Point.STATE_PRESSED){
					canvas.drawBitmap(pointPressed, src,dst, paint);
					canvas.drawText(String.valueOf(points[i][j].index), point.x-25,point.y+20, paint1);
				}
				else if(point.state==Point.STATE_ERROR){
					canvas.drawBitmap(pointError,src,dst, paint);
					canvas.drawText(String.valueOf(points[i][j].index), point.x-25,point.y+20, paint1);
				}
				else if(point.state==Point.STATE_NORMAL){
					canvas.drawBitmap(pointNormal, src,dst, paint);
					paint1.setTextSize(80);
					paint1.setFakeBoldText(true);

					canvas.drawText(String.valueOf(points[i][j].index), point.x-25,point.y+20, paint1);
				}
				else if(point.state==Point.STATE_SUCCESS){
					canvas.drawBitmap(pointSuccess, src,dst, paint);
					canvas.drawText(String.valueOf(points[i][j].index), point.x-25,point.y+20, paint1);
				}
				else if(point.state==Point.STATE_REGET){
					canvas.drawBitmap(pointReget, src,dst, paint);
					canvas.drawText(String.valueOf(points[i][j].index), point.x-25,point.y+20, paint1);
				}
			}
		}	
	}
	
	public void initPoints() {
		// TODO Auto-generated method stub
		width=getWidth();
		height=getHeight();
		
		float offsetsX,offsetsY;
		if(width>height){
			offsetsX=(width-height)/2;
			offsetsY=0;
			width=height;
		}
		else{
			offsetsX=0;
			offsetsY=(height-width)/2;
			height=width;
		}
		
		pointNormal=BitmapFactory.decodeResource(getResources(),R.drawable.gesture_node_normal);
		pointPressed=BitmapFactory.decodeResource(getResources(),R.drawable.gesture_node_pressed);
		pointError=BitmapFactory.decodeResource(getResources(),R.drawable.gesture_node_error);
		pointSuccess=BitmapFactory.decodeResource(getResources(),R.drawable.gesture_node_success);
		pointReget=BitmapFactory.decodeResource(getResources(),R.drawable.gesture_node_reget);
		
		
		points[0][0]=new Point(offsetsX+(1*width)/8,offsetsY+1*width/8);
		points[0][1]=new Point(offsetsX+(3*width)/8,offsetsY+1*width/8);
		points[0][2]=new Point(offsetsX+(5*width)/8,offsetsY+1*width/8);
		points[0][3]=new Point(offsetsX+(7*width)/8,offsetsY+1*width/8);
		
		points[1][0]=new Point(offsetsX+width/8,offsetsY+3*width/8);
		points[1][1]=new Point(offsetsX+3*width/8,offsetsY+3*width/8);
		points[1][2]=new Point(offsetsX+(5*width)/8,offsetsY+3*width/8);
		points[1][3]=new Point(offsetsX+(7*width)/8,offsetsY+3*width/8);
		
		points[2][0]=new Point(offsetsX+width/8,offsetsY+(5*width)/8);
		points[2][1]=new Point(offsetsX+3*width/8,offsetsY+(5*width)/8);
		points[2][2]=new Point(offsetsX+(5*width)/8,offsetsY+(5*width)/8);
		points[2][3]=new Point(offsetsX+(7*width)/8,offsetsY+5*width/8);
		
		points[3][0]=new Point(offsetsX+width/8,offsetsY+(7*width)/8);
		points[3][1]=new Point(offsetsX+3*width/8,offsetsY+(7*width)/8);
		points[3][2]=new Point(offsetsX+(5*width)/8,offsetsY+(7*width)/8);
		points[3][3]=new Point(offsetsX+(7*width)/8,offsetsY+7*width/8);
		
		bitmapR=/*pointNormal.getHeight()/2;*/width/9;
		
		
		char index='A';
		for(Point[] points:this.points){
			for(Point point:points){
				index='A';
				index=(char)(index+(int)(Math.random()*26));
				point.index=index;
			}
		}
		
		isInit=true;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		isFinish=false;
		movingNoPoint=false;
		movingX=event.getX();
		movingY=event.getY();
		Point point = null;
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				resetPoint();
				point = checkSelectPoint();
				if(point!=null){
					isSelect=true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(isSelect){
					point = checkSelectPoint();
					if(point==null){
						movingNoPoint=true;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				isFinish=true;
				isSelect=false;
				break;
		}
		if(!isFinish && isSelect && point!=null){
			if(crossPoint(point)){
				movingNoPoint=true;
			}
			else{
				point.state=Point.STATE_PRESSED;
				pointList.add(point);
			}
		}
		if(isFinish){
			if(pointList.size()==1){
				resetPoint();
			}
			else {
				String password="";
				for(int i=0;i<pointList.size();i++){
					password+=pointList.get(i).index;
				}
				System.out.println(password);
				if(istrue(password)){
					if(!(words.contains(password))){
						words.add(password);
						successPoint();
					}
					else if(words.contains(password)){
						regetPoint();
					}
				}
				else{
					errorPoint();
				}
			}
		}
		postInvalidate();
		return true;	
	}
	private boolean istrue(String password){
		if(getDatebase(password)){
			return true;
		}
		else return false;
		
	}
	
	private boolean getDatebase(String password){
		Cursor cursor = db.rawQuery("select * from Note where words= '" + password + "' COLLATE NOCASE", null);
		if(cursor.getCount()==0){
			return false;
		}
		else return true;
	}
	
	private boolean crossPoint(Point point){
		if(pointList.contains(point)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void resetPoint(){
		for(int i=0;i<pointList.size();i++){
			Point point=pointList.get(i);
			point.state=Point.STATE_NORMAL;
		}
		pointList.clear();
	}
	
	
	
	public void errorPoint(){
		for(Point point:pointList){
			point.state=Point.STATE_ERROR;
		}
	}
	public void successPoint(){
		for(Point point:pointList){
			point.state=Point.STATE_SUCCESS;
		}
		score+=2*(pointList.size()-1);
		LlkActivity.llkActivity.setScore(score);
	}
	
	public void regetPoint(){
		for(Point point:pointList){
			point.state=Point.STATE_REGET;
		}
	}
	
	private Point checkSelectPoint(){
		for(int i=0;i<points.length;i++){
			for(int j=0;j<points[i].length;j++){
				Point point=points[i][j];
				if(Point.with(point.x, point.y, bitmapR, movingX, movingY)){
					return point;
				}
			}
		}
		return null;
	}
	
	public void reGame(){
		score=0;
		LlkActivity.llkActivity.setScore(score);
		words.clear();
		initPoints();
		invalidate();
	}
	
	
	private static class Point{
		public static int STATE_NORMAL=0;
		public static int STATE_PRESSED=1;
		public static int STATE_ERROR=2;
		public static int STATE_SUCCESS=3;
		public static int STATE_REGET=4;
		public float x,y;
		public int state=0;
		public char index;
		public Point(float x,float y){
			this.x=x;
			this.y=y;
		}

		public static boolean with(float pointX,float pointY,float r,float movingX,float movingY){
			if(movingX<pointX+r&&movingX>pointX-r&&movingY<pointY+r&&movingY>pointY-r){
				return true;
			}
			else return false;

		}
	}

}
