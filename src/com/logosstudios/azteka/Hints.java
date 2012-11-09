package com.logosstudios.azteka;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.logostudios.azteka.R;

public class Hints extends Activity implements OnClickListener
{
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.hints);
        
        Button back = (Button) findViewById(R.id.button1);
        back.setOnClickListener(this);
    }

	public void onClick(View v) 
	{
		setResult(Activity.RESULT_OK);
		finish();
	}
}