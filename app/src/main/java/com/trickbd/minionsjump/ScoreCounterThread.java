package com.trickbd.minionsjump;



public class ScoreCounterThread extends Thread {
	DrawingThread drawingThread;
	boolean runFlag=false;

	
	public ScoreCounterThread(DrawingThread drawingThread) {
		this.drawingThread=drawingThread;
	}
	
	@Override
	public void run() {

		runFlag=true;
		
		while (runFlag) {

			float tempMax=0;
			
			for (Minions minions:drawingThread.allminions) {
				if (minions.centerY<tempMax) {
					tempMax=minions.centerY; 
				}
			}
			
			drawingThread.maxScore=(int) (drawingThread.maxScore>(-tempMax)?drawingThread.maxScore:(-tempMax));
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void startCounter() {
		runFlag=true;
	}
	public void stopCounter() {
		runFlag=false;
	}
}
