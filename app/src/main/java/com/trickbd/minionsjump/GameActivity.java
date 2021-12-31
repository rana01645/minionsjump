package com.trickbd.minionsjump;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends Activity {
	GameView gameView;
	SensorManager sensorManager;
	SensorEventListener sensorEventListener;
	Sensor accelerometerSensor;
	private static float gX,gY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        gameView=new GameView(this);
        
        initializeSensors();

		
		setContentView(R.layout.activity_game);
		gameView=(GameView) findViewById(R.id.myGameView);
        initializeButton();
	}

	private void initializeButton() {
		final Button moveleftButton=(Button) findViewById(R.id.leftButton);
		
		moveleftButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					gameView.drawingThread.dock.startMovingLeft();
					moveleftButton.getBackground().setAlpha(130);
					break;
					
				case MotionEvent.ACTION_UP:
					gameView.drawingThread.dock.stopMovingLeft();
					moveleftButton.getBackground().setAlpha(255);
					break;

				default:
					break;
				}
				return false;
			}
		});
		
		
		final Button moveRightButton=(Button) findViewById(R.id.rightButton);
		moveRightButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					gameView.drawingThread.dock.startMovingRight();
					moveRightButton.getBackground().setAlpha(130);
					break;
					
				case MotionEvent.ACTION_UP:
					gameView.drawingThread.dock.stopMovingRight();
					moveRightButton.getBackground().setAlpha(255);
					break;

				default:
					break;
				}
				return false;
			}
		});
		
	}

	public static float getgX() {
		return gX;
	}

	public static void setgX(float gX) {
		GameActivity.gX = gX;
	}

	public static float getgY() {
		return gY;
	}

	public static void setgY(float gY) {
		GameActivity.gY = gY;
	}

	private void initializeSensors() {
		sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
		sensorEventListener=new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
					gX=-event.values[0];
					gY=event.values[1];
					
					if (gY<0) {
						stopUsingSensors();
						gameView.drawingThread.animationThread.stopThread();
						gameView.drawingThread.scoreCounterThread.stopCounter();
						
						AlertDialog.Builder alertBuilder=new AlertDialog.Builder(GameActivity.this);
						alertBuilder.setTitle("Cheating Not Allowed!!");
						alertBuilder.setIcon(R.drawable.alert);
						alertBuilder.setMessage("You're Holding Your Phone's Upside Down For Increasing Score");
						alertBuilder.setPositiveButton("Restart Game", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								restartGame(null);
								
							}
						});
						
						alertBuilder.setNegativeButton("Exit Game", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								stopGame(null);
								
							}
						});
						alertBuilder.show();
					}
					
				}
				
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
		
		accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		startUsingsensors();
	}

	
	private void startUsingsensors() {
		sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	private void stopUsingSensors() {
		sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	protected void onPause() {
		stopUsingSensors();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		startUsingsensors();
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		stopUsingSensors();
		super.onStop();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void pauseGame(View view) {
		if (!gameView.drawingThread.pauseFlag) {
			gameView.drawingThread.animationThread.stopThread();
			gameView.drawingThread.pauseFlag=true;
			stopUsingSensors();
			
			view.setBackgroundResource(R.drawable.unlock);
		}else {
			gameView.drawingThread.animationThread=new AnimationThread(gameView.drawingThread);
			gameView.drawingThread.animationThread.start();
			view.setBackgroundResource(R.drawable.lock);
			gameView.drawingThread.pauseFlag=false;
			startUsingsensors();
		}
	}
	
	public void restartGame(View view) {
		stopUsingSensors();
		startUsingsensors();
		gameView.drawingThread.stopThread();
		gameView.drawingThread=new DrawingThread(gameView, this);
		gameView.drawingThread.start();
		
		Toast.makeText(this, "Game Restarted", Toast.LENGTH_SHORT).show();
	}
	
	public void stopGame(View view) {
		this.finish();
	}
	
	
}
