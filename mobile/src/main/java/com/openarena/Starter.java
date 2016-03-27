package com.openarena;

import android.app.Application;

import com.openarena.controllers.Controller;

public class Starter extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Controller.init();
	}

}
