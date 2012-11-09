package com.logosstudios.azteka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.logostudios.azteka.R;
import com.scoreloop.client.android.core.controller.RequestController;
import com.scoreloop.client.android.core.controller.RequestControllerException;
import com.scoreloop.client.android.core.controller.RequestControllerObserver;
import com.scoreloop.client.android.core.controller.ScoreController;
import com.scoreloop.client.android.core.controller.UserController;
import com.scoreloop.client.android.core.model.Score;
import com.scoreloop.client.android.core.model.Session;
import com.scoreloop.client.android.core.model.User;

public class EndScreen extends Activity implements OnClickListener{
	
	
	static final int 	HIGHSCORE_PROGRESS		= 0;
	static final int 	HIGHSCORE_SUBMITTED		= 1;
	static final int 	HIGHSCORE_FAILED		= 2;
	
	String highscoreErrorMsg = "";
	
	private int score;
	private int taps;
	
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.end);
        
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.endLayout);
		AdView ads = new AdView(this, AdSize.BANNER, "a14f566af62da67");
		AdRequest ad = new AdRequest();
		ad.addTestDevice("259EE8790FFE66635A3787EE7F49C8C6");
		ad.addTestDevice("A30F22DD30DE44C40735E34F630B9BF9");
		layout.addView(ads);
		ads.loadAd(ad);
		
        Bundle extras = getIntent().getExtras();
        score = -1;
        taps = -1;
        if(extras != null)
        {
        	score = extras.getInt("score");
        	taps = extras.getInt("taps");
        }
        TextView scoreView = (TextView) findViewById(R.id.textView1);
		scoreView.setText("Numbers of hoops: " + score);
		TextView tapsView = (TextView) findViewById(R.id.textView2);
		tapsView.setText("Number of taps: " + taps);
		TextView finalScore = (TextView) findViewById(R.id.textView3);
		finalScore.setText("Final score: " + (score*100-taps*5));
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
          super.onActivityResult(requestCode, resultCode, data);
          if(resultCode == Activity.RESULT_OK)
          {
	          RequestControllerObserver observer = new RequestControllerObserver() {
	
					public void requestControllerDidFail(RequestController controller, Exception exception) 
					{
						dismissDialog(HIGHSCORE_PROGRESS);
						highscoreErrorMsg = exception.getLocalizedMessage();
						showDialog(HIGHSCORE_FAILED);
					}
					public void requestControllerDidReceiveResponse(RequestController controller)
					{
						dismissDialog(HIGHSCORE_PROGRESS);
						showDialog(HIGHSCORE_SUBMITTED);
					}
				};
				
				Score s = new Score((double) (score*100-taps*5), null);
				ScoreController scoreController = new ScoreController(observer);
				showDialog(HIGHSCORE_PROGRESS);
				scoreController.submitScore(s);
          }
          else if(resultCode == Activity.RESULT_CANCELED)
          {
        	  //I dunno lol.
          }
    }
	public void onClick(View v)
	{
		if(v.getId() == R.id.button1)
		{
			Intent intent = new Intent(this, Game.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);
		}
		if(v.getId() == R.id.button2)
		{
			Intent intent = new Intent(this, Username.class);
			startActivityForResult(intent, 0);
		}
	}
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case HIGHSCORE_PROGRESS:
			return ProgressDialog
					.show(EndScreen.this, "", "Submitting score...");
		case HIGHSCORE_SUBMITTED:
			return (new AlertDialog.Builder(this))
				.setMessage("Score submitted!")
				.setPositiveButton("OK", null)
				.create();
		case HIGHSCORE_FAILED:
			return (new AlertDialog.Builder(this))
					.setMessage(highscoreErrorMsg)
					.setPositiveButton("OK", null)
					.create();
		}
		return null;
	}
}
