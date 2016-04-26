package com.openarena;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.openarena.util.Configs;
import com.openarena.controllers.Controller;

public class Starter extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Context context = getApplicationContext();
		Controller.init(context);
		ImageLoader.getInstance().init(Configs.getImageLoaderConfig(context));
	}

}
