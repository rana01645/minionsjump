package com.trickbd.minionsjump;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.VelocityTracker;

public class Minions {
	float centerX,centerY;
	int height,width;
	float velocityX,velocityY;
	Bitmap minionBitmap;
	Paint minionPaint;
	boolean miononFellDown=false;
	
	public Minions(Bitmap bitmap) {
		minionBitmap=bitmap;
		centerX=centerY=0;
		velocityX=velocityY=0;
		height=minionBitmap.getHeight();
		width=minionBitmap.getWidth();
		minionPaint=new Paint();
		
	}
	
	public Minions(Bitmap bitmap,int CX,int CY) {
		this(bitmap);
		centerX=CX;
		centerY=CY;
		
	}
	
	public Minions(Bitmap bitmap,Point center) {
		this(bitmap, center.x, center.y);
	}
	
	public void setCenter(Point centerPoint) {
		centerX=centerPoint.x;
		centerY=centerPoint.y;
	}
	
	public void setVelocity(VelocityTracker velocityTracker) {
		velocityX=velocityTracker.getXVelocity();
		velocityY=velocityTracker.getYVelocity();
	}

}
