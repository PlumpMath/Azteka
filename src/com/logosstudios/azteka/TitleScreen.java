package com.logosstudios.azteka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.logostudios.azteka.R;

public class TitleScreen extends Activity implements OnClickListener{
	private CheckBox checkBox;
	
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.title);
        
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
		
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
    }
	public void onClick(View v)
	{
		if(v.getId() == R.id.button1)
		{
			if(!checkBox.isChecked())
			{
				Intent intent = new Intent(this, Game.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				//finish();
				//overridePendingTransition(0, 0);
			}
			else
			{
				Intent intent = new Intent(this, GameNight.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		}
		else if(v.getId() == R.id.button3)
		{
			Intent intent = new Intent(this, LeaderboardActivity.class);
			startActivity(intent);
		}
		else if(v.getId() == R.id.button2)
		{
			Intent intent = new Intent(this, Instructions.class);
			startActivity(intent);
		}
	}
}
