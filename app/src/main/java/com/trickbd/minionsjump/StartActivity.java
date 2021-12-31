package com.trickbd.minionsjump;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class StartActivity extends Activity {
	TextView topscore;
	TextView about;
	DrawingThread drawingThread;

    @SuppressLint("ClickableViewAccessibility") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        setContentView(R.layout.activity_start);
        final Button startButton;
        startButton=(Button)findViewById(R.id.startButton);
        startButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							startButton.getBackground().setAlpha(100);

							break;
						case MotionEvent.ACTION_UP:
							Intent intent=new Intent(StartActivity.this, GameActivity.class);
							startActivity(intent);
							startButton.getBackground().setAlpha(255);
							break;
						case MotionEvent.ACTION_MOVE:

						default:
							break;
						}
				
				
				
				
				
				
				return false;
			}
		});
        
        topscore=(TextView) findViewById(R.id.htp);
        topscore.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent=new Intent(StartActivity.this, HowToActivity.class);
				startActivity(intent);
				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
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
}
