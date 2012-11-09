package com.logosstudios.azteka;

import android.app.Application;

import com.scoreloop.client.android.core.model.Client;

public class Azteka extends Application{
	
	public void onCreate()
	{
		Client.init(this, "IXfu3/rwn0UD3QTwlxwF5WaU833zpgxO6maEX/32Lb9sDGGzIrnH2g==", null);
	}

}