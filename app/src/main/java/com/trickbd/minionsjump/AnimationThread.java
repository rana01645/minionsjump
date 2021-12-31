package com.trickbd.minionsjump;

public class AnimationThread extends Thread {
	ScoreCounterThread scoreCounterThread;
	private boolean flag=false;
	
	float gravityX,gravityY;
	DrawingThread drawingThread;
	float timeConstant=0.3f;
	float retardationRatio=-0.7f;
	int width,height;
	int left,right,top,bottom;
	
	
	
	public AnimationThread(DrawingThread drawingThread) {
		super();
		this.drawingThread = drawingThread;
		updateDimensions();
	}

	private void updateDimensions() {
		width=drawingThread.possibleMinions.get(0).getWidth();
		height=drawingThread.possibleMinions.get(0).getHeight();
		
		left=width/2;
		top=height/2;
		right=drawingThread.displayX-(width/2);
		bottom=drawingThread.displayY-(height/2);
	}

	@Override
	public void run() {
		flag=true;
		while (flag) {
			updatePositions();
			
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updatePositions() {
		gravityX=GameActivity.getgX();
		gravityY=GameActivity.getgY();
		
		if (drawingThread.touchedFlag) {
			for (int i = 0; i < drawingThread.allminions.size()-1; i++) {
				updateMionionsPosition(drawingThread.allminions.get(i),i);
				//we don't animate last minion 
				
			}
		}else {
			for (int i = 0; i < drawingThread.allminions.size(); i++) {
				updateMionionsPosition(drawingThread.allminions.get(i),i);
				
			}
		}
		
		
		
	}

	private void updateMionionsPosition(Minions minions,int position) {
		minions.centerX+=(minions.velocityX*timeConstant)+0.5*gravityX*timeConstant*timeConstant;
		minions.velocityX+=gravityX*timeConstant;
		
		minions.centerY+=(minions.velocityY*timeConstant)+0.5*gravityY*timeConstant*timeConstant;
		minions.velocityY+=gravityY*timeConstant;
		constrainPosition(minions,position);
		
	}

	private void constrainPosition(Minions minions,int position) {
		if (minions.centerX<left) {
			minions.centerX=left;
			minions.velocityX*=retardationRatio;
		}else if (minions.centerX>right) {
			minions.centerX=right;
			minions.velocityX*=retardationRatio;
		}
		
//		if (minions.centerY<top) {
//			minions.centerY=top;
//			minions.velocityY*=retardationRatio;
//		}else 
//			
			if (minions.centerY>bottom) {
				if (minionsOutsideDock(minions)) {
					minions.miononFellDown=true;
					
					if (minions.centerY>bottom+height) {
						drawingThread.allminions.remove(position);
						drawingThread.maxScore=0;
					}
				}
				
				if (!minions.miononFellDown) {
					minions.centerY=bottom;
					minions.velocityY*=retardationRatio;
				}

		}
		
	}

	public void stopThread() {
		flag=false;
	}
	
	private boolean minionsOutsideDock(Minions minions) {
		return (minions.centerX + (width / 2)) < drawingThread.dock.leftMostPoint || (minions.centerX - (width / 2)) > drawingThread.dock.rightMostPoint;

	}
}
