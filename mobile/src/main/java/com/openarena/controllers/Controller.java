package com.openarena.controllers;

import android.content.Context;
import android.os.Handler;

import com.openarena.model.PreferencesManager;
import com.openarena.util.Const;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Controller {

	private static final int NUMBERS_OF_CORES = Runtime.getRuntime().availableProcessors();
	private static ThreadPoolExecutor sExecutor;
	private static Handler sHandler;
	private static Context sContext;

	public static void init(Context context) {
		if (Controller.sContext == null) Controller.sContext = context;
		if (sHandler == null) sHandler = new Handler();
		if (sExecutor == null || sExecutor.isShutdown()) {
			sExecutor = new ThreadPoolExecutor(
					1,
					NUMBERS_OF_CORES,
					5000,
					TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>());
		}
	}

	public static boolean isFirstEnter() {
		return PreferencesManager.getInstance(sContext).getBoolean(Const.PREF_FIRST_ENTER, true);
	}

}
