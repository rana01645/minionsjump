package com.trickbd.minionsjump;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DrawingThread extends Thread {
	
	private Canvas canvas;
	GameView gameView;
	Context context;
	
	boolean threadFlag=false;
	boolean touchedFlag=false;
	boolean pauseFlag=false;
	
	Bitmap backgroundBitmap;
	int displayX,displayY;
	
	ArrayList<Minions> allminions;
	ArrayList<Bitmap> possibleMinions;
	
	AnimationThread animationThread;
	
	ScoreCounterThread scoreCounterThread;
	Paint scorPaint;
	
	Dock dock;
	
	int maxScore=0;
	
	
	
	public DrawingThread(GameView gameView, Context context) {
		super();
		this.gameView = gameView;
		this.context = context;
		
		initializeall();
	}






	private void initializeall() {
		WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay=windowManager.getDefaultDisplay();
		
		Point displayDimension=new Point();
		defaultDisplay.getSize(displayDimension);
		displayX=displayDimension.x;
		displayY=displayDimension.y;
		
		backgroundBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
		backgroundBitmap=Bitmap.createScaledBitmap(backgroundBitmap, displayX, displayY, true);
		
		initializeAllPossibleMinions();
		scoreCounterThread=new ScoreCounterThread(this);
		
		dock=new Dock(this, R.drawable.dock);
		
		scorPaint=new Paint();
		scorPaint.setColor(Color.LTGRAY);
		scorPaint.setTextAlign(Align.LEFT);
		scorPaint.setTextSize(displayX/15);
		
		
		
	}






	private void initializeAllPossibleMinions() {
		allminions=new ArrayList<Minions>();
		possibleMinions=new ArrayList<Bitmap>();
		
		possibleMinions.add(giveResizedMinionBitmap(R.drawable.minion1));
		possibleMinions.add(giveResizedMinionBitmap(R.drawable.minion2));
		possibleMinions.add(giveResizedMinionBitmap(R.drawable.minion3));
		possibleMinions.add(giveResizedMinionBitmap(R.drawable.minion4));
		possibleMinions.add(giveResizedMinionBitmap(R.drawable.minion5));
		
	}
	
	private Bitmap giveResizedMinionBitmap(int reSourceID) {
		Bitmap tempBitmap=BitmapFactory.decodeResource(context.getResources(), reSourceID);
		tempBitmap=Bitmap.createScaledBitmap(tempBitmap, displayX/5, (tempBitmap.getHeight()/tempBitmap.getWidth())*(displayX/5), true);
		return tempBitmap;
	}






	@Override
	public void run() {
		threadFlag=true;
		animationThread=new AnimationThread(this);
		animationThread.start();
		scoreCounterThread.start();
		while (threadFlag) {
			canvas=gameView.surfaceHolder.lockCanvas();
			
			try {
				synchronized (gameView.surfaceHolder) {
					
					updateDisplay();
					
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally{
				if (canvas!=null) {
					gameView.surfaceHolder.unlockCanvasAndPost(canvas);
				}
				
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		scoreCounterThread.stopCounter();
		animationThread.stopThread();
	}
	
	private void updateDisplay() {
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		drawDock();
		for (int i = 0; i < allminions.size(); i++) {
			Minions tempMinions=allminions.get(i);
			canvas.drawBitmap(tempMinions.minionBitmap,tempMinions.centerX-(tempMinions.width/2),tempMinions.centerY-(tempMinions.height/2), tempMinions.minionPaint);
			
		}
		
		if (pauseFlag) {
			drawPause();
		}
		//drawSensorData();
		
		drawScore();
		
	}






	private void drawPause() {
		Paint paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(100);
		paint.setAlpha(200);
		paint.setTextAlign(Align.CENTER);
		
		
		
		canvas.drawARGB(150, 0, 0, 0);
		canvas.drawText("Game Paused", displayX/2, displayY/2, paint);
		
	}






	public void stopThread() {
		threadFlag=false;
	}
	
	private void drawSensorData() {
		Paint paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(displayX/10);
		canvas.drawText("X axis: "+GameActivity.getgX(), 0, displayY/2, paint);
		canvas.drawText("Y axis: "+GameActivity.getgY(), 0, displayY/2+displayX/5, paint);
	}
	
	private void drawDock() {
		canvas.drawBitmap(dock.docBitmap, dock.topLeftPoint.x, dock.topLeftPoint.y, null);
		
	}
	
	public void drawScore() {
		if (maxScore>1000) {
			scorPaint.setColor(Color.GREEN);
			if (maxScore>10000) {
				scorPaint.setColor(Color.CYAN);
				if (maxScore>100000) {
					scorPaint.setColor(Color.RED);
				}
			}
		}
		canvas.drawText("Score: "+maxScore, 0, displayY/9, scorPaint);
	}
}
