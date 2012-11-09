package com.logosstudios.azteka;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.logostudios.azteka.R;
import com.scoreloop.client.android.core.controller.RequestController;
import com.scoreloop.client.android.core.controller.RequestControllerException;
import com.scoreloop.client.android.core.controller.RequestControllerObserver;
import com.scoreloop.client.android.core.controller.UserController;
import com.scoreloop.client.android.core.model.Session;
import com.scoreloop.client.android.core.model.User;

public class Username extends Activity implements OnClickListener, RequestControllerObserver
{
	static final int	USERNAME_PROGRESS		= 0;
	static final int	USERNAME_SUBMITTED 		= 1;
	static final int	USERNAME_FAILED			= 2;
	
	String usernameErrorMsg = "";
	
	private EditText username;
	private EditText email;
	
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.username);
        
        username = (EditText) findViewById(R.id.editText1);
		email = (EditText) findViewById(R.id.editText2);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.usernameLayout);
		AdView ads = new AdView(this, AdSize.BANNER, "a14f566af62da67");
		AdRequest ad = new AdRequest();
		ad.addTestDevice("259EE8790FFE66635A3787EE7F49C8C6");
		ad.addTestDevice("A30F22DD30DE44C40735E34F630B9BF9");
		layout.addView(ads);
		
		RequestControllerObserver observer = new RequestControllerObserver()
		{
			public void requestControllerDidReceiveResponse(RequestController requestController) 
			{
				UserController userController = (UserController)requestController;
				
				User user = userController.getUser();
				username.setText(user.getLogin());
				email.setText(user.getEmailAddress());
			}
			public void requestControllerDidFail(RequestController aRequestController, Exception anException) 
			{
				
			}
		};
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
    }

	public void onClick(View arg0) 
	{
		User user = Session.getCurrentSession().getUser();
		user.setLogin(username.getText().toString());
		user.setEmailAddress(email.getText().toString());
		
		UserController userController = new  UserController(this);
		userController.setUser(user);
		showDialog(USERNAME_PROGRESS);
		userController.submitUser();
	}
	public void requestControllerDidReceiveResponse(RequestController aRequestController)
	{
		dismissDialog(USERNAME_PROGRESS);
		showDialog(USERNAME_SUBMITTED);
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	public void requestControllerDidFail(RequestController controller, Exception exception) 
	{
		dismissDialog(USERNAME_PROGRESS);
		RequestControllerException ctrlException = null;
		if(exception instanceof RequestControllerException)
		{
			ctrlException = (RequestControllerException) exception;
			if(ctrlException.hasDetail(RequestControllerException.DETAIL_USER_UPDATE_REQUEST_EMAIL_TAKEN))
			{
				usernameErrorMsg += "Email already taken.";
			}
			else if(ctrlException.hasDetail(RequestControllerException.DETAIL_USER_UPDATE_REQUEST_INVALID_EMAIL))
			{
				usernameErrorMsg += "Invalid email.";
			}
			if(ctrlException.hasDetail(RequestControllerException.DETAIL_USER_UPDATE_REQUEST_USERNAME_TAKEN)) 
			{
				usernameErrorMsg += "Username taken.";
			}
			else if(ctrlException.hasDetail(RequestControllerException.DETAIL_USER_UPDATE_REQUEST_USERNAME_TOO_SHORT)) 
			{
				usernameErrorMsg += "Username too short.";
			}
			else if(ctrlException.hasDetail(RequestControllerException.DETAIL_USER_UPDATE_REQUEST_INVALID_USERNAME))
			{
				usernameErrorMsg += "Invalid username.";
			}
		}
		else 
		{
			usernameErrorMsg = exception.getLocalizedMessage();
		}
		showDialog(USERNAME_FAILED);
		
		User user = ((UserController)controller).getUser();
		username.setText(user.getLogin());
		email.setText(user.getEmailAddress());
		//setResult(Activity.RESULT_CANCELED);
		//finish();
	}
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case USERNAME_PROGRESS:
			return ProgressDialog
				.show(Username.this, "", "Submitting username...");
		case USERNAME_SUBMITTED:
			return (new AlertDialog.Builder(this))
				.setMessage("Username and email submitted!")
				.setPositiveButton("OK", null)
				.create();
		case USERNAME_FAILED:
			return (new AlertDialog.Builder(this))
				.setMessage(usernameErrorMsg)
				.setPositiveButton("OK", null)
				.create();
		}
		return null;
	}
	
}
