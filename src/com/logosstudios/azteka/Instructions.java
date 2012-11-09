package com.logosstudios.azteka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.logostudios.azteka.R;

public class Instructions extends Activity implements OnClickListener
{
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.instructions);
        
        Log.d("DEBUG", "CREATED");
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(this);
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
          super.onActivityResult(requestCode, resultCode, data);
          setResult(Activity.RESULT_OK);
          finish();
    }
	public void onClick(View v) 
	{
		Intent intent = new Intent(this, PowerUps.class);
		startActivityForResult(intent, 0);
	}

}
