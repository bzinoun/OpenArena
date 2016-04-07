package com.openarena;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.openarena.controllers.Configs;
import com.openarena.controllers.Controller;

public class Starter extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Controller.init(getApplicationContext());
		ImageLoader.getInstance().init(Configs.getImageLoaderConfig(getApplicationContext()));
	}

}
