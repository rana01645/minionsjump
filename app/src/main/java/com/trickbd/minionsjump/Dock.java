package com.trickbd.minionsjump;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Dock {
	Bitmap docBitmap;
	int docHieght,docWidth;
	int leftMostPoint,rightMostPoint;
	

	
	Point topLeftPoint=new Point(0, 0),bottomcentrePoint;
	DrawingThread drawingThread;
	
	boolean movingLeftFlag=false;
	boolean movingRightFlag=false;
	
	public Dock(DrawingThread drawingThread,int bitmapid) {
		this.drawingThread=drawingThread;
		Bitmap tempBitmap=BitmapFactory.decodeResource(drawingThread.context.getResources(), bitmapid);
		tempBitmap=Bitmap.createScaledBitmap(tempBitmap, drawingThread.displayX/2, tempBitmap.getHeight(), true);
		docBitmap=tempBitmap;
		docHieght=docBitmap.getHeight();
		docWidth=docBitmap.getWidth();
		
		

		bottomcentrePoint=new Point((int)drawingThread.displayX/2, (int)drawingThread.displayY);
		topLeftPoint.y=bottomcentrePoint.y-docHieght;
		
		updateInfo();
	}

	private void updateInfo() {
		leftMostPoint=bottomcentrePoint.x-docWidth/2;
		rightMostPoint=bottomcentrePoint.x+docWidth/2;
		
		topLeftPoint.x=leftMostPoint;
		
	}
	
	public void moveDocktoLeft() {
		bottomcentrePoint.x-=4;
		updateInfo();
		
	}

	public void moveDocktoRight() {
		bottomcentrePoint.x+=4;
		updateInfo();

	}
	
	public void startMovingLeft() {
		Thread thread=new Thread(){
			@Override
			public void run() {
				movingLeftFlag=true;
				while (movingLeftFlag) {
					moveDocktoLeft();
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		thread.start();
	}
	
	public void stopMovingLeft() {
		movingLeftFlag=false;
	}
	
	public void startMovingRight() {
		Thread thread=new Thread(){
			@Override
			public void run() {
				movingRightFlag=true;
				while (movingRightFlag) {
					moveDocktoRight();
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		};
		thread.start();
	}
	
	public void stopMovingRight() {
		movingRightFlag=false;
	}
}
